// ----------------------------------------------------------
// ROSprocessing initialization and related stuff 
// ----------------------------------------------------------

import rosprocessing.*;
ROSProcessing rp; 

void setupRos() {
  try {
    println("ROSProcessing connecting");

    // it would be great if this didn't hang when there's no connection
    rp = new ROSProcessing(this, "192.168.56.101");
    rp.connect();
    rp.listenTransforms("/tf_processing");
    
    println("ROSProcessing connected");
  } 
  catch(Exception e) {
    println(e);
  }
}
