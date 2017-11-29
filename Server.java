/** @author Alex Heerding
    @version 1.0
    Type racing game server. This threaded server will start up and wait for 4
    clients to connect. Then it will send out a typeRace object to each client.
    Once the client sends the object back, the server will rank them and end the
    race.
*/
import java.io.*;
import java.net.*;

public class Server {
  //attributes
  private int clientCount = 0; // keeps track of number of connected clients

  public static void main(String[] args) {
    new Server();
  } // end main

  public Server () {
    //try and set up a server
    try {
      //set up a port
      ServerSocket ss = new ServerSocket(16789);

      Socket cs = null; // client socket

      //wait for client connection
      do {
        System.out.println("Waiting for a connection..."); // mostly so we can see that the server is actually running
        cs = ss.accept();
        ThreadedServer ts = new ThreadedServer(cs);
        ts.start();
        clientCount++;
      } while (clientCount < 4) // continue accepting clients until we have 4 connected
    } catch(IOException ioe){
      System.out.println("Something went wrong with connection");
      ioe.printStackTrace();
    }
  } // end Server constructor

  class ThreadedServer extends Thread {
    private Socket cs = null; // Client Socket

    public ThreadedServer(_cs) {
      cs = _cs;
    } // end ThreadedServer constructor

    @Override
    public void run() {
      //send over the typeRace object

      // recieve typeRace object from cluent

      //compare the pToType and pTyped

        //tally up errors

      //send list of winners back to client

      //close connections
      
    } // end run
  } // end class ThreadedServer
} // end class Server
