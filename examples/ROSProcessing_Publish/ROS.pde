// ----------------------------------------------------------
// ROSprocessing initialization and related stuff 
// ----------------------------------------------------------

import rosprocessing.*;
ROSProcessing rp; 
JSONObject json;

void setupRos() {
  
  //It would be nice if every message object have a "toJson()" method.
  //For the moment we use JSONObject instead.
  json = new JSONObject();
  json.setString("data", "Greetings from processing!");
  
  try {
    println("ROSProcessing connecting");
    
    ROSProcessing rp; 
    rp = new ROSProcessing(this, "192.168.1.161", 8080);
    rp.connect();
    rp.advertise("/chatter", "std_msgs/String");
    rp.publish("/chatter", json.toString());

  }catch(Exception e) {
    println(e);
  }
  
}
