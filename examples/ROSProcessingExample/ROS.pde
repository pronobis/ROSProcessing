// ----------------------------------------------------------
// ROSprocessing initialization and related stuff 
// ----------------------------------------------------------

import rosprocessing.*;
ROSProcessing rp; 

void setupRos() {
  //rp = new ROSProcessing(this);
  rp = new ROSProcessing(this, "192.168.56.101");

  rp.connect();
  rp.listenTransforms("/tf_processing");
  rp.subscribe("/rgbd_camera/rgb/image", "newImage");   
  rp.subscribe("/map", "newMap");
  rp.subscribe("/scan", "newScan");
}

Transform getMapToRobot() {
  return rp.lookupTransform("map", "base_link");
}

Transform getMapToRobot(Time t) {
  return rp.lookupTransform("map", "base_link", t);
}

Transform getRobotToLaser()
{
  return rp.lookupTransform("base_link", "laser_link");
}
