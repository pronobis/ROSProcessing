/*
** Basic Laser Display from ROS
*/

import peasy.*;
PeasyCam cam;

Map worldMap;
Robot robot;
Laser laser;
int WIDTH_METERS = 50;

void setup() {
  size(displayWidth, displayHeight, P3D);
  noCursor();
  colorMode(HSB);
  ellipseMode(CENTER);
  rectMode(CENTER);
  
  cam = new PeasyCam(this, 1000);
  cam.setMinimumDistance(1);
  cam.setMaximumDistance(10000);

  setupRos();
  worldMap = new Map();
  robot = new Robot();
  laser = new Laser();
}

void draw() {
  background(#EEEEFF);
  
  pushMatrix();
  normalizeScene();
  
  ambientLight(0,0,128);
  pointLight(0, 0, 255, robot.x, robot.y, 3);
  
  worldMap.render();

  //fill(255);
  //rect(0,0,500,500);

  robot.render();
  laser.render();
  popMatrix();

  cam.beginHUD();
  drawFrameRate();
  cam.endHUD();
}
