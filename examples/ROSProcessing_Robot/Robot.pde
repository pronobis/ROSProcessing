class Robot {

  Transform location;
  PShape s;
  Float yaw = 0.0;
  Float x = 0.0;
  Float y = 0.0;

  Robot() {
    s = loadShape("sara_uw2.obj");
  }

  void render() {

    location = rp.lookupTransform("map", "base_link");

    x = location.getTranslation().getX32(); 
    y = location.getTranslation().getY32();

    // Float pitch = location.getRotation().getPitch32();
    // Float roll = location.getRotation().getRoll32();
    yaw = location.getRotation().getYaw32();

    pushStyle();
    pushMatrix();

    noStroke();
    fill(128, 255, 255);

    translate(x, y, 0);
    rotateX(HALF_PI);
    rotateY(yaw);
    // rotateY(yaw);
    //ellipse(x, y, .5, .5);
    shape(s, 0, 0);

    popMatrix();
    popStyle();
  }
}
