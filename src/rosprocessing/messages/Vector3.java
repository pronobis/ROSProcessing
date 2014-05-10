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

public class Vector3
{
  private double x;
  private double y;
  private double z;

  public Vector3() {
    this.x=0.0;
    this.y=0.0;
    this.z=0.0;
  }

  public Vector3(double x, double y, double z) {
    this.x=x;
    this.y=y;
    this.z=z;
  }

  public Vector3(Vector3 v) {
    this.x=v.x;
    this.y=v.y;
    this.z=v.z;
  } 

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getZ() {
    return z;
  }
  
  public float getX32() {
    return (float)x;
  }

  public float getY32() {
    return (float)y;
  }

  public float getZ32() {
    return (float)z;
  }

  public PVector toPVector() {
    return new PVector((float)x,(float)y,(float)z);    
  }

  public void add(final Vector3 v) {
    x+=v.x;
    y+=v.y;
    z+=v.z;
  }

  public static Vector3 add(final Vector3 v1, final Vector3 v2) {
    Vector3 out = new Vector3(v1);
    out.x+=v2.x;
    out.y+=v2.y;
    out.z+=v2.z;
    return out;
  }
  
  public void print(String name) {
    System.out.println(name+": ["+
                       Double.toString(x)+", "+
                       Double.toString(y)+", "+
                       Double.toString(z)+"]");   
  }
}
