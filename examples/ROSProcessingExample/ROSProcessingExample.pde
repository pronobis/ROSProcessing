// -----------------------------------------------
// Main file
// -----------------------------------------------

void setup() {
  size(1400, 800, P2D);
  frameRate(10);

  setupRos();
}

void draw() {
  // Collect data from global variables
  PImage image = _image;
  OccupancyGrid map = _map;
  LaserScan scan = _scan; 

  // Setup
  int widthM = 60;
  int heightM = widthM * height/width;
  setupCanvas(widthM);
   
  // Draw map and center
  if (map!=null)
    drawMap(map);
  drawCenter();

  // Draw robot and image
  Transform mapToRobotNow = getMapToRobot();
  if (mapToRobotNow!=null) {
    if (image!=null) {
      float imgWidth = 10;
      drawImage(image, widthM/2 - imgWidth, heightM/2, 0, 0, 0, imgWidth);
      drawImage(image, 
                mapToRobotNow.getTranslation().getX32(), 
                mapToRobotNow.getTranslation().getY32(), 
                mapToRobotNow.getRotation().getYaw32()-(float)Math.PI/2, 
                -1.5, 0, 3);
    }
    drawRobot(mapToRobotNow.getTranslation().getX32(), 
              mapToRobotNow.getTranslation().getY32(),
              mapToRobotNow.getRotation().getYaw32());
  }

  // Draw laser scan
  if (scan!=null) {
    Transform robotToLaser = getRobotToLaser();
    Transform mapToRobotScan = getMapToRobot(scan.getTime());
    Transform mapToLaserScan = Transform.combine(mapToRobotScan, robotToLaser);

    if (mapToLaserScan!=null) { 
      drawScan(scan,
               mapToLaserScan.getTranslation().getX32(), 
               mapToLaserScan.getTranslation().getY32(),
               mapToLaserScan.getRotation().getYaw32());
    }
  }
}

