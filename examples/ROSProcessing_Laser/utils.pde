void drawFrameRate() {
  pushStyle();
  fill(255);
  noStroke();
  text(frameRate+"", 100, 100);
 // println(frameRate);
  popStyle();
}

void normalizeScene() {
  translate(width/2, height/2);
  scale(width/WIDTH_METERS, -width/WIDTH_METERS);
  strokeWeight(0.05);
  stroke(0, 0, 0);
}
