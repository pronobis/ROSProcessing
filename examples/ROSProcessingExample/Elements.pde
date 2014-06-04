// ----------------------------------------------------------
// Methods drawing single elements
// ----------------------------------------------------------

void setupCanvas(int widthMeters) {
  translate(width/2,  height/2);
  scale(width/widthMeters, -width/widthMeters);
  strokeWeight(0.05);
  stroke(0,0,0); 
  background(100,100,100);
}

void drawCenter() {
  strokeWeight(0.1);
  stroke(0,0,0);
  fill(0);
  ellipse(0, 0, 0.5, 0.5);
  stroke(255,0,0);
  line(0,0,2,0);
  stroke(0,255,0);
  line(0,0,0,2);
}

void drawMap(OccupancyGrid map) {
  pushMatrix();
  rotate(-(float)_map.getOrigin().getOrientation().getYaw()); 
  image(_map.toPImage(this), 
        _map.getOrigin().getPosition().getX32(), 
        _map.getOrigin().getPosition().getY32(), 
        _map.getWidthMeters(), _map.getHeightMeters());
  popMatrix();
}

void drawRobot(float x, float y, float theta) {
  pushMatrix();
  fill(255,0,0);
  strokeWeight(0.05);
  stroke(0,0,0);
  translate(x, y);   
  rotate(theta); 
  ellipse(0,0, 0.5, 0.5);
  strokeWeight(0.1);
  stroke(0,0,0);
  line(0,0,1,0);  
  popMatrix(); 
}

void drawImage(PImage image, float x, float y, float theta, float sx, float sy, float imgWidth) {
  float imgHeight=imgWidth*(float)_image.height/(float)_image.width;
  pushMatrix();
  translate(x,y);
  rotate(theta); 
  translate(sx,sy);
  scale(1, -1);
  image(_image, 0, 0, 
        imgWidth, imgHeight);
  popMatrix(); 
}

void drawScan(LaserScan scan, float x, float y, float theta) {
  pushMatrix();
  translate(x,y);
  rotate(theta); 
  fill(255,0,0);
  strokeWeight(0.05);
  stroke(0,0,0);
  float angle = _scan.getAngleMin();
  for (int i=0; i<_scan.getRanges().length; ++i) {  
    float xx = _scan.getRanges()[i]*cos(angle);
    float yy = _scan.getRanges()[i]*sin(angle);
    ellipse(xx, yy, 0.3, 0.3);
    angle+=_scan.getAngleIncrement();
  }
  popMatrix(); 
}
