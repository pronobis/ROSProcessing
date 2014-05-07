package rosprocessing;

import processing.core.*;
import javax.xml.bind.DatatypeConverter;


public class Image
{
  public Header header;
  public int height;
  public int width;
  public String encoding;
  public byte is_bigendian;
  public int step;
  public String data;


  public PImage getPImage(PApplet parent) {
    // Decode the buffer

    // Create a new PImage
    PImage pImage = parent.createImage(width, height, PImage.RGB);
    byte [] byteData = DatatypeConverter.parseBase64Binary(data);

    int pixNum=width*height;
    for (int i=0; i<pixNum; ++i) {
      pImage.pixels[i] =
        (((int)byteData[i*4  ] & 0x000000FF)<<16) |
        (((int)byteData[i*4+1] & 0x000000FF)<<8 ) |
         ((int)byteData[i*4+2] & 0x000000FF);
    }

    return pImage;
  }
  
}
