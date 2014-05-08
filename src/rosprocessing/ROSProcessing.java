/*********************************************************************
* Software License Agreement (BSD License)
*
*  Copyright (c) 2014, Andrzej Pronobis <andrzej@pronobis.pro>
*  All rights reserved.
*
*  Redistribution and use in source and binary forms, with or without
*  modification, are permitted provided that the following conditions
*  are met:
*
*   * Redistributions of source code must retain the above copyright
*     notice, this list of conditions and the following disclaimer.
*   * Redistributions in binary form must reproduce the above
*     copyright notice, this list of conditions and the following
*     disclaimer in the documentation and/or other materials provided
*     with the distribution.
*   * Neither the name of the Willow Garage nor the names of its
*     contributors may be used to endorse or promote products derived
*     from this software without specific prior written permission.
*
*  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
*  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
*  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
*  FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
*  COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
*  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
*  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
*  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
*  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
*  LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
*  ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
*  POSSIBILITY OF SUCH DAMAGE.
*********************************************************************/

package rosprocessing;

import processing.core.*;

import java.lang.reflect.*;
import java.util.*;
import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.*;


/**
 * The main library class.
 */
public class ROSProcessing {

  /** Stores information about the event callback. */
  class EventInfo  {
    Object object;
    Method method;

    public EventInfo(Object object, Method method) {
      this.object=object;
      this.method=method;
    }
  };

  // Private variables
  private PApplet parent;
  private WebSocketClient webSocket;
  private String hostname;
  private int port;
  private boolean isConnected;

  /** List of all events and their associated topic names */
  private Map<String, EventInfo> events;

  /** Listener for TF */
  private TransformListener transformListener;

  
  /** Initialization */
  public ROSProcessing(PApplet parent, String hostname, int port) {
    this.parent = parent;
    this.webSocket = null;
    this.hostname = hostname;
    this.port = port;
    this.isConnected = false;
    this.events = new HashMap<String, EventInfo>();
    this.transformListener = null;
    
    this.parent.registerMethod("dispose", this);
  }

  /** Initialization */
  public ROSProcessing(PApplet parent, String hostname) {
    this(parent, hostname, 9090);
  }

  /** Initialization */
  public ROSProcessing(PApplet parent) {
    this(parent, "localhost");
  }

  
  /** Anything in here will be called automatically when the parent sketch shuts down.
      For instance, this might shut down a thread used by this library. */
  public void dispose() {
    disconnect();
  }


  /** Displays an error in the console. */
  void logError(String msg) {
    this.parent.println("[ROSProcessing] ERROR: " + msg);
  }

  
  /** Displays an info in the console. */
  void logInfo(String msg) {
    this.parent.println("[ROSProcessing] " + msg);
  }
  
  
  /** Connects to the server using websocket */
  public boolean connect() {

    // Check if websocket exists already
    if (this.webSocket==null)
    {
      // Get URI
      URI uri;
      try {
        uri = new URI( "ws://" + this.hostname + ":" + Integer.toString(this.port) );
      } 
      catch ( URISyntaxException ex ) {
        logError("Incorrect hostname (" + this.hostname + ") or port (" + this.port + ").");
        return false;
      }

      // Initialize the websocket
      this.webSocket = new WebSocketClient(uri) { 
          @Override
          public void onMessage( String message ) {
            processIncoming(message);
          }
        
          @Override
          public void onOpen( ServerHandshake handshake ) {
            logInfo( "Connected to " + getURI());
          }
        
          @Override
          public void onClose( int code, String reason, boolean remote ) {
            logInfo( "Disconnected from " + getURI());          
          }

          @Override
          public void onError( Exception ex ) {            
            logError( ex.toString() );
          }
        };
    }

    // Connect
    try{
      this.isConnected = this.webSocket.connectBlocking();
    } catch(InterruptedException e) {}

    return this.isConnected;
  }


  /** Disconnect from the server. */
  public void disconnect() {
    if ((this.webSocket!=null) && (this.isConnected))
    {
      try{
        this.webSocket.closeBlocking();
        this.isConnected = false;
      } catch(InterruptedException e) {}
    }
  }

 
  /** Subscribe to a topic. */
  void subscribe(String topic, Object obj, String event) {
    if (!this.isConnected)
      return;

    logInfo("Subscribing to "+topic);

    // Get the method associated with the event
    Method method=null;
    Method[] methods = obj.getClass().getDeclaredMethods();
    for (int i=0; i<methods.length; ++i) {
      if (methods[i].getName().equals(event))
        method = methods[i];
    }
    if (method==null) {
      logError("No such event: "+event);
      return;
    }

    // Save the topic and method in a map
    this.events.put(topic, new EventInfo(obj, method));

    // RosBridge command
    this.webSocket.send("{\"op\": \"subscribe\"" +
                    ", "+
                    "\"topic\": \""+ topic +"\"" +
                    "}");
  }
  

  /** Subscribe to a topic. */
  public void subscribe(String topic, String event) {
    subscribe(topic, this.parent, event);
  }


  /** Starts a TF transform listener. */
  public void listenTransforms() {
    this.transformListener = new TransformListener(this);
  }
  

  // /** Get the most recent transform between two frames. */
  public TransformStamped lookupTransform(String parent, String child) {
    if (this.transformListener == null)
      return null;
    return this.transformListener.lookupTransform(parent,child);
  }

  /** Prints a list of transforms received so far. */ 
  public void printTransforms() {
    this.transformListener.printTransforms();
  }

  
  /** Processes incoming data. */
  private void processIncoming(String data) {
    //logInfo(data);
    
    // Parse the string
    JsonParser parser = new JsonParser();
    JsonObject object = parser.parse(data).getAsJsonObject();

    // Get Op
    JsonElement opEl = object.get("op");
    if (opEl==null) {
      logError("Incorrect response:\n" + data);
      return;
    }
    String op;
    try {
       op = opEl.getAsString();
    } catch(Exception ex) {
      logError("Incorrect response:\n" + data);
      return;
    } 
    
    // Handle various operations
    if (op.equals("publish")) {
      // Get topic
      JsonElement topicEl = object.get("topic");
      if (topicEl==null) {
        logError("Incorrect response:\n" + data);
          return;
      }
      String topic;
      try {
        topic = topicEl.getAsString();
      } catch(Exception ex) {
        logError("Incorrect response:\n" + data);
        return;
      } 
      // Get msg
      JsonElement msgEl = object.get("msg");
      if (topicEl==null) {
        logError("Incorrect response:\n" + data);
          return;
      }

      // Processs
      processPublish(topic, msgEl);
    } else {
      logError("Unknown operation: "+op);
      return;
    }   
  }

  
  /** Processes the received published incoming data */
  private void processPublish(String topic, JsonElement data) {
    // Get the event
    EventInfo eventInfo = this.events.get(topic);
    if (eventInfo==null)
    {
      logError("No event defined for topic "+topic);
      return;
    }
    Class<?>[] params = eventInfo.method.getParameterTypes();
    Class param = params[0];

    // Parse the json
    Object msg;
    Gson gson = new Gson();
    try {
      msg = gson.fromJson(data, param);
    } catch(JsonSyntaxException ex) {     
      logError("Cannot match the class to JSON: " + ex.getMessage());
      return;
    }
    
    // Execute method
    try {
      eventInfo.method.invoke(eventInfo.object, msg);
    } catch (Exception ex) {
      logError("Cannot execute the event method: "+ ex + "\n");
      ex.printStackTrace();
    }    
  } 
  
};
