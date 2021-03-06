ROSProcessing
=============

ROSBridge client for Processing.

Installation
------------

Just clone the repo to the `libraries` folder inside your Processing sketchbook and restart Processing.

Basic Usage
-----

1. Import the library
    ```
    import rosprocessing.*;
    ```

2. Create a global variable for the main library class
    ```
    ROSProcessing rp; 
    ```

3. Initialize the library and connect to rosbridge inside your `setup()`
    ```
    void setup() {
      ...
      rp = new ROSProcessing(this);
      rp.connect();
      ...
    }
    ```
    You can specify the hostname and port like this:
    ```
    rp = new ROSProcessing(this, "192.168.1.1", 8080);
    ```
    If not specified, "localhost" and port 9090 are used.

4. Subscribe to your topics using the `subscribe` method (e.g. also in your `setup()`).  
    ```
    rp.subscribe("/image", "newImage");   
    ```
   The arguments are the topic name and the name of the event callback to be called when new data arrive.

5. Create a callback function that will receive the data and save it in a global variable. 
    ```
    PImage pImage;
    void newImage(Image image) {
      pImage=image.toPImage(this);
    }
    ```
    The event function must take an argument of a class corresponding to the message type of the ROS topic. Currently implemented message classes are storred in the `src/rosprocessing/messages` folder. Sometimes, those message classes provide convenience functions for parsing the raw ROS data, as in the example above where we convert ROS data to a `PImage`.

6. Draw!
    ```
    void draw() {
      if (pImage!=null)
        image(pImage,0,0);
    }
    ```

For additional features, see the example for now.


Example
-------

You can find an example in the `examples` folder.
