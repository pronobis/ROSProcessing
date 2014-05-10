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

public class Time implements Comparable<Time>
{
  private int secs;
  private int nsecs;

  public Time() {
    this.secs=0;
    this.nsecs=0;
  }

  public Time(int secs, int nsecs) {
    this.secs=secs;
    this.nsecs=nsecs;
  }

  public int getSecs() {
    return secs;
  }

  public int getNSecs() {
    return nsecs;
  }
  
  public int compareTo(Time t) {
    if (this.secs>t.secs)
      return 1;
    else if (this.secs<t.secs)
      return -1;
    else
    {
      if (this.nsecs>t.nsecs)
        return 1;
      else if (this.nsecs<t.nsecs)
        return -1;
      else
        return 0;
    }
  }

  public static Time diff(Time t1, Time t2) {
    Time out = new Time(t1.secs, t1.nsecs);
    out.secs-=t2.secs;
    out.nsecs-=t2.nsecs;
    if (out.nsecs<0)
    {
      out.secs-=1;
      out.nsecs+=1e9;
    }
    return out;
  }
  
  public void print(String name) {
    System.out.println(name+": " +
                       Integer.toString(secs) + "." +
                       Integer.toString(nsecs));
  }

  public double toDouble() {
    return this.secs + (double)this.nsecs/1.0e9;
  }
 
}
