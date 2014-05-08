/*********************************************************************
* Software License Agreement (BSD License)
*
*  Copyright (c) 2014, Andrzej Pronobis <andrzej@pronobis.pro>
*  All rights reserved.
*
*  Redistribution and use in source and binary forms, with or without
*  modification, are permitted provided that the following conditions
*  are met:
*
*   * Redistributions of source code must retain the above copyright
*     notice, this list of conditions and the following disclaimer.
*   * Redistributions in binary form must reproduce the above
*     copyright notice, this list of conditions and the following
*     disclaimer in the documentation and/or other materials provided
*     with the distribution.
*   * Neither the name of the Willow Garage nor the names of its
*     contributors may be used to endorse or promote products derived
*     from this software without specific prior written permission.
*
*  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
*  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
*  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
*  FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
*  COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
*  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
*  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
*  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
*  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
*  LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
*  ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
*  POSSIBILITY OF SUCH DAMAGE.
*********************************************************************/

package rosprocessing;

import java.util.*;


/**
 * Listens to /tf and provides transforms.
 */
public class TransformListener
{
  /** Number of seconds for which we keep the old
      stuff in the buffer compared to the timestamp of the new
      incoming TF message. */
  private static double BUFFER_SIZE_SECONDS = 2.0;

  /** Minimum time difference to consider the TF time match. */
  private static double TF_MATCH_THRESHOLD = 0.1;

  
  private ROSProcessing parent;
  
  /**
   * Represents information about a transform.
   */
  class TransformInfo {

    private final String parent;
    private final String child;

    public TransformInfo(String parent, String child) {
        this.parent = parent;
        this.child = child;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransformInfo)) return false;
        TransformInfo ti = (TransformInfo) o;
        return parent.equals(ti.parent) && child.equals(ti.child);
    }

    @Override
    public int hashCode() {
      return ('p' + this.parent + ":c" + this.child).hashCode();
    } 
  }

 
  /** Stores the most recent transform between two frames. */
  Map<TransformInfo, LinkedList<TransformStamped>> tf;

  
  public TransformListener(ROSProcessing parent) {
    this.parent = parent;
    this.tf = new HashMap<TransformInfo, LinkedList<TransformStamped>>();
    
    // Subscribe to the topic
    this.parent.subscribe("/tf", this, "newTF");
  }
  
  
  /** Receives a new TF message. */
  void newTF(TFMessage msg) {
    // Mutex
    synchronized(this.tf) {
     
      // Process all incoming transforms
      for (int i=0; i<msg.transforms.length; ++i) {
        TransformStamped ts = msg.transforms[i];
        // Update the frame names to be absolute
        if (!ts.header.frame_id.substring(0,1).equals("/"))
          ts.header.frame_id="/"+ts.header.frame_id;
        if (!ts.child_frame_id.substring(0,1).equals("/"))
          ts.child_frame_id="/"+ts.child_frame_id;
      
        // Get the saved transforms list
        TransformInfo tInfo =
          new TransformInfo(ts.header.frame_id, ts.child_frame_id);
        LinkedList<TransformStamped> tsList = this.tf.get(tInfo);
        if (tsList==null)
        {
          tsList = new LinkedList<TransformStamped>();
          this.tf.put(tInfo, tsList);
        }

        // Add the transform to the top of the list
        tsList.addFirst(ts);

        // Should we remove stuff from the list?
        while (ts.header.stamp.diff(tsList.peekLast().header.stamp).toDouble()>this.BUFFER_SIZE_SECONDS)
          tsList.removeLast();

        // this.parent.logInfo("Queue size: " + tsList.size());
      }

    }
  }


  /** Get the most recent transform between two frames. */
  TransformStamped lookupTransform(String parent, String child) {
    // Mutex
    synchronized(this.tf) {

      TransformInfo tInfo =
        new TransformInfo(parent, child);
      LinkedList<TransformStamped> tsList = this.tf.get(tInfo);

      if (tsList==null)
        return null;

      // Find the most recent transform in the list
      TransformStamped mr = null;
      for(TransformStamped ts : tsList) {
        if ((mr==null) || (ts.header.stamp.compareTo(mr.header.stamp)>0))
          mr = ts;
      }

      return mr;

    }
  }


  /** Get the transform between two frames corresponding to the given time. */
  TransformStamped lookupTransform(String parent, String child, Time time) {
    // Mutex
    synchronized(this.tf) {

      TransformInfo tInfo =
        new TransformInfo(parent, child);
      LinkedList<TransformStamped> tsList = this.tf.get(tInfo);

      if (tsList==null)
        return null;

      // Find the closest transform to the given time
      TransformStamped closest = null;
      double diff = 1.0e9;
      for(TransformStamped ts : tsList) {
        // Calculate time difference
        double d = Math.abs(ts.header.stamp.diff(time).toDouble());
        if (d<diff)
        {
          diff = d;
          closest = ts;
        }
      }

      // Return
      if ((closest==null) || (diff>this.TF_MATCH_THRESHOLD))
        return null;
      else
        return closest;

    }
  }


  /** Prints a list of transforms stored in the map. */
  void printTransforms() {
    // Mutex
    synchronized(this.tf) {

      for(TransformInfo ti : this.tf.keySet()) {
        System.out.println(ti.parent+" -> "+ti.child);
      }

    }
  }

}
