package rosprocessing;


public class PointField
{
  // Consts
  public static transient byte INT8    = 1;
  public static transient byte UINT8   = 2;
  public static transient byte INT16   = 3;
  public static transient byte UINT16  = 4;
  public static transient byte INT32   = 5;
  public static transient byte UINT32  = 6;
  public static transient byte FLOAT32 = 7;
  public static transient byte FLOAT64 = 8;

  // Variables
  public String name;
  public int offset;
  public byte datatype;
  public int count;
};
