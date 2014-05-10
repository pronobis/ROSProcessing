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

import processing.core.*;

public class Transform
{
  private Vector3 translation;
  private Quaternion rotation;

  public Transform() {
    translation = new Vector3();
    rotation = new Quaternion();
  }

  public Transform(final Transform t) {
    translation = new Vector3(t.getTranslation());
    rotation = new Quaternion(t.getRotation());
  }

  public Transform(final Vector3 trans, final Quaternion rot) {
    translation = new Vector3(trans);
    rotation = new Quaternion(rot);
  }
  
  public Vector3 getTranslation() {
    return translation;
  }

  public Quaternion getRotation() {
    return rotation;
  } 
  
  public void print(final String name) {
    translation.print(name+":translation");
    rotation.print(name+":rotation");
  }

  public static Transform combine(final Transform t1, final Transform t2) {
    if (t1==null || t2==null)
      return null;
    
    Vector3 trans = new Vector3(t1.getTranslation());
    Quaternion rot = new Quaternion(t1.getRotation());

    trans.add(rot.apply(t2.getTranslation()));
    rot.combine(t2.getRotation());

    return new Transform(trans, rot);
  }

}
