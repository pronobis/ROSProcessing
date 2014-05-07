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
  Map<TransformInfo, TransformStamped> tf;

  
  public TransformListener(ROSProcessing parent) {
    this.parent = parent;
    this.tf = new HashMap<TransformInfo, TransformStamped>();
    
    // Subscribe to the topic
    this.parent.subscribe("/tf", this, "newTF");
  }
  
  
  /** Receives a new TF message. */
  void newTF(TFMessage msg) {
    // Process all incoming transforms
    for (int i=0; i<msg.transforms.length; ++i) {
      TransformStamped newTS = msg.transforms[i];
      // Update the frame names to be absolute
      if (!newTS.header.frame_id.substring(0,1).equals("/"))
        newTS.header.frame_id="/"+newTS.header.frame_id;
      if (!newTS.child_frame_id.substring(0,1).equals("/"))
        newTS.child_frame_id="/"+newTS.child_frame_id;
      
      // Get the saved transform
      TransformInfo tInfo =
        new TransformInfo(newTS.header.frame_id, newTS.child_frame_id);
      TransformStamped oldTS = this.tf.get(tInfo);
      // Is the new one newer?
      if ((oldTS==null) ||
          (newTS.header.stamp.compareTo(oldTS.header.stamp)>0))
      {
        // Put it to the map
        this.tf.put(tInfo, newTS);
//        this.parent.logInfo("Adding transform between " +
//                            tInfo.parent + " and " + tInfo.child  );
      }
    }
  }


  /** Get the most recent transform between two frames. */
  TransformStamped lookupTransform(String parent, String child) {
    TransformInfo tInfo =
      new TransformInfo(parent, child);
    
    return this.tf.get(tInfo);
  }



}
