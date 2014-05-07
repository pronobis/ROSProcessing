package rosprocessing;

import processing.core.*;
import javax.xml.bind.DatatypeConverter;


public class CompressedImage
{
  public Header header;
  public String format;
  public String data;
  private transient byte [] byteData;
  
  public byte [] getByteData() {
    if (byteData==null)
      byteData = DatatypeConverter.parseBase64Binary(data);
    return byteData; 
  }
  
}
