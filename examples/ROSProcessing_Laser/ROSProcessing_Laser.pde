// -----------------------------------------------
// Main file
// -----------------------------------------------

Laser laser;
int WIDTH_METERS = 50;

void setup() {
  size(displayWidth, displayHeight, P2D);
  noCursor();
  colorMode(HSB);

  setupRos();
  laser = new Laser();
}

void draw() {
  println("tick");
  background(0);

  pushMatrix();
  normalizeScene();
  laser.render();
  popMatrix();

  drawFrameRate();
}
