void drawFrameRate() {
  pushStyle();
  fill(255);
  noStroke();
  textSize(22 );
  text(frameRate+"", 10, 30);
 // println(frameRate);
  popStyle();
}

void normalizeScene() {
  //translate(width/2, height/2);
  scale(width/WIDTH_METERS, -width/WIDTH_METERS, width/WIDTH_METERS);
  translate(-robot.x, -robot.y);
  
  rotateX(-HALF_PI);
  //rotate(-robot.yaw);
  
  //cam.lookAt(robot.x,robot.y, .5);
}
