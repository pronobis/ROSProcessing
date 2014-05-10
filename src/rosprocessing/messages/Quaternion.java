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

import processing.core.PVector;


public class Quaternion
{ 
  /** First canonical vector (coordinates: 1, 0, 0). */
  public static transient final Vector3 PLUS_I = new Vector3(1.0, 0.0, 0.0);

  /** Second canonical vector (coordinates: 0, 1, 0). */
  public static transient final Vector3 PLUS_J = new Vector3(0.0, 1.0, 0.0);

  /** Third canonical vector (coordinates: 0, 0, 1). */
  public static transient final Vector3 PLUS_K = new Vector3(0.0, 0.0, 1.0);

  private double x;
  private double y;
  private double z; 
  private double w;

  public Quaternion() {
    this.x=0.0;
    this.y=0.0;
    this.z=0.0;
    this.w=0.0;
  }

  public Quaternion(double x, double y, double z, double w) {
    this.x=x;
    this.y=y;
    this.z=z;
    this.w=w;
  }
  
  public Quaternion(final Quaternion q) {
    this.x=q.x;
    this.y=q.y;
    this.z=q.z;
    this.w=q.w;
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

  public double getW() {
    return w;
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

  public float getW32() {
    return (float)w;
  }
  
  public void print(String name) {
    System.out.println(name+": ["+
                       Double.toString(x)+", "+
                       Double.toString(y)+", "+
                       Double.toString(z)+", "+
                       Double.toString(w)+"]");   
  }

  public double getYaw() {
    return -getAngles()[2];
  }

  public float getYaw32() {
    return (float)-getAngles()[2];
  }

  public Vector3 apply(final Vector3 u) {
    double x = u.getX();
    double y = u.getY();
    double z = u.getZ();

    double s = this.x * x + this.z * y + this.z * z;

    double nx = 2 * (this.w * (x * this.w - (this.y * z - this.z * y)) + s * this.x) - x;
    double ny = 2 * (this.w * (y * this.w - (this.z * x - this.x * z)) + s * this.y) - y;
    double nz = 2 * (this.w * (z * this.w - (this.x * y - this.y * x)) + s * this.z) - z;

    return new Vector3(nx,ny,nz);
  }

  public PVector apply(final PVector u) {
    float x = u.x;
    float y = u.y;
    float z = u.z;

    float s = (float)this.x * x + (float)this.z * y + (float)this.z * z;

    float nx = 2 * ((float)this.w * (x * (float)this.w - ((float)this.y * z - (float)this.z * y)) + s * (float)this.x) - x;
    float ny = 2 * ((float)this.w * (y * (float)this.w - ((float)this.z * x - (float)this.x * z)) + s * (float)this.y) - y;
    float nz = 2 * ((float)this.w * (z * (float)this.w - ((float)this.x * y - (float)this.y * x)) + s * (float)this.z) - z;

    return new PVector(nx,ny,nz);
  }

  public Vector3 applyInverse(final Vector3 u) {
    double x = u.getX();
    double y = u.getY();
    double z = u.getZ();

    double s = this.x * x + this.y * y + this.z * z;
    double m0 = -this.w;

    double nx = 2 * (m0 * (x * m0 - (this.y * z - this.z * y)) + s * this.x) - x;
    double ny = 2 * (m0 * (y * m0 - (this.z * x - this.x * z)) + s * this.y) - y;
    double nz = 2 * (m0 * (z * m0 - (this.x * y - this.y * x)) + s * this.z) - z;
		
    return new Vector3(nx, ny, nz);
  }
  
  public PVector applyInverse(final PVector u) {
    float x = u.x;
    float y = u.y;
    float z = u.z;

    float s = (float)this.x * x + (float)this.y * y + (float)this.z * z;
    float m0 = -(float)this.w;

    float nx = 2 * (m0 * (x * m0 - ((float)this.y * z - (float)this.z * y)) + s * (float)this.x) - x;
    float ny = 2 * (m0 * (y * m0 - ((float)this.z * x - (float)this.x * z)) + s * (float)this.y) - y;
    float nz = 2 * (m0 * (z * m0 - ((float)this.x * y - (float)this.y * x)) + s * (float)this.z) - z;
		
    return new PVector(nx, ny, nz);
  }


  public double[] getAngles() {
    Vector3 v1 = apply(PLUS_K);
    Vector3 v2 = applyInverse(PLUS_I);
    if  ((v2.getZ() < -0.9999999999) || (v2.getZ() > 0.9999999999)) {
      return null;
    }
    return new double[] {
      Math.atan2(-(v1.getY()), v1.getZ()),
      Math.asin(v2.getZ()),
      Math.atan2(-(v2.getY()), v2.getX())
    };
  }

  /** Combine this rotation with another rotation. */
  public static Quaternion combine(final Quaternion q1, final Quaternion q2) {
    return new Quaternion(q2.x * q1.w +  q2.w * q1.x + (q2.y * q1.z - q2.z * q1.y),
                          q2.y * q1.w +  q2.w * q1.y + (q2.z * q1.x - q2.x * q1.z),
                          q2.z * q1.w +  q2.w * q1.z + (q2.x * q1.y - q2.y * q1.x),
                          q2.w * q1.w - (q2.x * q1.x +  q2.y * q1.y + q2.z * q1.z));
  }
  
  /** Combine this rotation with another rotation. */
  public void combine(final Quaternion q) {
    this.x = q.x * this.w +  q.w * this.x + (q.y * this.z - q.z * this.y);
    this.y = q.y * this.w +  q.w * this.y + (q.z * this.x - q.x * this.z);
    this.z = q.z * this.w +  q.w * this.z + (q.x * this.y - q.y * this.x);
    this.w = q.w * this.w - (q.x * this.x +  q.y * this.y + q.z * this.z);
  } 
  
}

