package rosprocessing;

import processing.core.*;
import javax.xml.bind.DatatypeConverter;


public class OccupancyGrid
{
  public Header header;
  public MapMetaData info;
  public byte [] data;

  public PImage getPImage(PApplet parent) {

    // Create a new PImage
    PImage pImage = parent.createImage(info.width, info.height, PImage.RGB);

    int pixNum=info.width*info.height;
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
