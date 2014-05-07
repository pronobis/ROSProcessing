package rosprocessing;


public class PointCloud2
{
  public Header header;

  public int height;
  public int width;

  public PointField[] fields;

  public boolean is_bigendian;
  public int  point_step; 
  public int  row_step; 
  public String data; 
  public boolean is_dense;
};

