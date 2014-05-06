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

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;


/**
 * The main library class.
 */
public class ROSProcessing {

  PApplet _parent;
  WebSocketClient _webSocket;
  String _hostname;
  int _port;
  boolean _isConnected;
  
  
  /** Initialization */
  public ROSProcessing(PApplet parent, String hostname, int port) {
    _parent = parent;
    _webSocket = null;
    _hostname = hostname;
    _port = port;
    _isConnected = false;
    
    _parent.registerMethod("dispose", this);
  }

  
  /** Anything in here will be called automatically when the parent sketch shuts down.
      For instance, this might shut down a thread used by this library. */
  public void dispose() {
    disconnect();
  }


  /** Displays an error in the console. */
  private void logError(String msg) {
    _parent.println("ROSProcessing ERROR: " + msg + "\n");
  }

  
  /** Displays an info in the console. */
  private void logInfo(String msg) {
    _parent.println("ROSProcessing INFO: " + msg + "\n");
  }
  
  
  /** Connects to the server using websocket */
  public boolean connect() {

    // Check if websocket exists already
    if (_webSocket==null)
    {
      // Get URI
      URI uri;
      try {
        uri = new URI( "ws://" + _hostname + ":" + Integer.toString(_port) );
      } 
      catch ( URISyntaxException ex ) {
        logError("Incorrect hostname (" + _hostname + ") or port (" + _port + ").");
        return false;
      }

      // Initialize the websocket
      _webSocket = new WebSocketClient(uri) { 
          @Override
          public void onMessage( String message ) {
            logInfo( "Received: " + message );
          }
        
          @Override
          public void onOpen( ServerHandshake handshake ) {
            logInfo( "Connected to: " + getURI());
          }
        
          @Override
          public void onClose( int code, String reason, boolean remote ) {
            logInfo( "Disconnected from: " + getURI() + "; Code: " + code + " " + reason);          
          }

          @Override
          public void onError( Exception ex ) {
            logError( "WebSocket Exception occurred!\n" + ex );
            // ex.printStackTrace();
          }
        };
    }

    // Connect
    try{
      _isConnected = _webSocket.connectBlocking();
    } catch(InterruptedException e) {}

    return _isConnected;
  }


  /** Disconnect from the server. */
  public void disconnect() {
    if ((_webSocket!=null) && (_isConnected))
    {
      try{
        _webSocket.closeBlocking();
        _isConnected = false;
      } catch(InterruptedException e) {}
    }
  }


  /** Subscribe to a topic. */
  public void subscribe() {
    if (_isConnected)
    {
      _webSocket.send("{\"op\": \"subscribe\", \"topic\": \"/odom\", \"type\": \"nav_msgs/Odometry\"}");
    }
  }
  
}























