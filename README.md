ROSProcessing
=============

ROSBridge client for Processing.

Installation
------------

Just clone the repo to the `libraries` folder inside your Processing sketchbook and restart Processing.

Usage Example
-------------

```
import rosprocessing.*;

ROSProcessing rp; 
PImage pImage;

void setup() {
  size(640,480,P2D);
  
  rp = new ROSProcessing(this);
  rp.connect();
  rp.subscribe("/rgbd_camera/rgb/image", "newImage");   
}

void draw() {
  if (pImage!=null)
    image(pImage,0,0);
}

void newImage(Image image) {
  pImage=image.getPImage(this);
}

```
