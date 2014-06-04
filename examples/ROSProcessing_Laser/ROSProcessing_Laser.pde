/*
** Basic Laser Display from ROS
*/


Laser laser;
int WIDTH_METERS = 50;

void setup() {
  size(displayWidth, displayHeight, P3D);
  noCursor();
  colorMode(HSB);

  setupRos();
  laser = new Laser();

  /*
  laser = new Laser(){
    public void render() {
      println("hey" );
    }
  };
  */

}

void draw() {
  background(0);

  pushMatrix();
  normalizeScene();
  laser.render();
  popMatrix();



  drawFrameRate();
}
