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


public class LaserScan
{
  private Header header;

  private float angle_min;
  private float angle_max;
  private float angle_increment;

  private float time_increment;
  private float scan_time;

  private float range_min;
  private float range_max;

  private float[] ranges;
  private float[] intensities;  

  public Header getHeader() {
    return header;
  }

  public Time getTime() {
    return header.getStamp();
  }
  
  public float getAngleMin() {
    return angle_min;
  }

  public float getAngleMax() {
    return angle_max; 
  }
  
  public float getAngleIncrement() {
    return angle_increment;
  }
  
  public float getRangeMin() {
    return range_min;
  }
  
  public float getRangeMax() {
    return range_max;
  }
  
  public float [] getRanges() {
    return ranges;
  }
  
  public void print(String name) {
    header.print(name+":header");
    System.out.println(name+":angle_min: " + Float.toString(angle_min));
    System.out.println(name+":angle_max: " + Float.toString(angle_max));
    System.out.println(name+":angle_increment: " + Float.toString(angle_increment));
    System.out.println(name+":time_increment: " + Float.toString(time_increment));
    System.out.println(name+":scan_time: " + Float.toString(scan_time));
    System.out.println(name+":range_min: " + Float.toString(range_min));
    System.out.println(name+":range_max: " + Float.toString(range_max));
    if (ranges!=null && ranges.length>0)
    {
      String rangesStr="["+Float.toString(ranges[0]);
      for (int i=1; i<ranges.length; ++i)
        rangesStr+=","+Float.toString(ranges[i]);
      System.out.println(name+":ranges: " + rangesStr);
    }
    if (intensities!=null && intensities.length>0)
    {
      String intensitiesStr="["+Float.toString(intensities[0]);
      for (int i=1; i<intensities.length; ++i)
        intensitiesStr+=","+Float.toString(intensities[i]);
      System.out.println(name+":intensities: " + intensitiesStr);
    }
  }
}
