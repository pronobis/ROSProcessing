public class Laser {

  LaserScan currentScan;
  float x, y, theta;

  Laser() {
    println("new laser!");
    rp.subscribe("/scan", this, "newScan");
    
    
  }


  Transform getMapToRobot(Time t) {
    return rp.lookupTransform("map", "base_link", t);
  }

  Transform getRobotToLaser()
  {
    return rp.lookupTransform("base_link", "laser_link");
  }


  public void newScan(LaserScan newScan) {

    if (newScan!=null) {

      //println("new scan!");
      Transform robotToLaser = getRobotToLaser();
      Transform mapToRobotScan = getMapToRobot(newScan.getTime());
      Transform mapToLaserScan = Transform.combine(mapToRobotScan, robotToLaser);

      if (mapToLaserScan!=null) { 
        currentScan = newScan;

        x = mapToLaserScan.getTranslation().getX32();
        y = mapToLaserScan.getTranslation().getY32(); 
        theta = mapToLaserScan.getRotation().getYaw32();
      }
    }
  }

  void render() {
    if (currentScan!=null) {
      pushStyle();
      pushMatrix();
      
      translate(x, y);
      rotate(theta); 

      fill(0, 255, 255, 32);
      strokeWeight(.01 );
      stroke(0, 255, 255, 16);

      float angle = currentScan.getAngleMin();
      beginShape(QUAD_STRIP);
      for (int i=0; i<currentScan.getRanges().length; ++i) {  
        //println(currentScan.getRanges()[i]);
        float xx = currentScan.getRanges()[i]*cos(angle);
        float yy = currentScan.getRanges()[i]*sin(angle);
        vertex(xx, yy, 0);
        vertex(xx, yy, 2.5);
        angle+=currentScan.getAngleIncrement();
      }
      endShape();
      
      popMatrix();
      popStyle();
    } else {
      fill(255, 0, 0);
      textSize(55);
      text("no laser yet", 100, 100);
    }
  }
}
