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
import javax.xml.bind.DatatypeConverter;


public class OccupancyGrid
{
  private Header header;
  private MapMetaData info;
  private byte [] data;

  public Header getHeader() {
    return header;
  }

  public Time getTime() {
    return header.getStamp();
  }
  
  public MapMetaData getInfo() {
    return info;
  }

  public byte [] getData() {
    return data;
  }

  public Pose getOrigin() {
    return info.getOrigin();
  }

  public float getWidthMeters() {
    return info.getWidthMeters();
  }

  public float getHeightMeters() {
    return info.getHeightMeters();
  }
  
  public PImage toPImage(final PApplet parent) {

    // Create a new PImage
    PImage pImage = parent.createImage(info.getWidth(), info.getHeight(), PImage.RGB);

    int pixNum=info.getWidth()*info.getHeight();
    for (int i=0; i<pixNum; ++i) {
      if (data[i]<0)
        pImage.pixels[i] = 150<<16 | 150<<8 | 255;
      else      
        pImage.pixels[i] =
          ((200-(int)data[i]*2)<<16) |
          ((200-(int)data[i]*2)<<8 ) |
           (200-(int)data[i]*2);
    }
    
    return pImage;
  }

};
