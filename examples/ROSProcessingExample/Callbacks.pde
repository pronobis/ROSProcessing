// ----------------------------------------------------------
// The callbacks receive data from the ROS topics
// You can perform all data processing inside those callbacks
// and save the results in global variables. This way,
// the processin will happen only once when new data arrive.
// ----------------------------------------------------------

PImage _image;
void newImage(Image image) {
  _image=image.toPImage(this);
}

OccupancyGrid _map;
void newMap(OccupancyGrid map) {
  println("Received new map!");
  _map = map;
}

LaserScan _scan;
void newScan(LaserScan scan) {
  _scan = scan;
}
