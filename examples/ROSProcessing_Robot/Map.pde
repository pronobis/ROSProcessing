public class Map {

  OccupancyGrid currentMap;

  Map() {
    println("map instantiated");
    rp.subscribe("/map", this, "newMap");
  }

  public void newMap(OccupancyGrid _newMap) {
    if (_newMap !=null) {
      println("new map");
      currentMap = _newMap;
    }
  }

  void render() {
    if (currentMap!=null) {

      MapMetaData info = currentMap.getInfo();
      float[] data = parseByte(currentMap.getData());

      int w = info.getWidth();
      int h = info.getHeight();
      int l = w*h;

      pushStyle();
      pushMatrix();
      translate(-w*.5, -h*.5);

      noStroke();
      for (int y=0; y<h; y++ ) {
        for (int x=0; x<w; x++ ) {
          float v = data[x*y];
          //println(v);
          if (v>=0) {
            fill(255);
            rect(x, y, 1, 1);
          }
        }
      }

      popMatrix();
      popStyle();
    }
  }
}
