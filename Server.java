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
  private final int NUM_CLIENTS = 2; // desired number of competitors
  private final String PARAGRAPH = "Mr. and Mrs. Dursley, of number four, \n" +
    "Privet Drive, were proud to say that they were perfectly normal, thank \n" +
    "you very much. They were the last people you'd expect to be involved in \n" +
    "anything strange or mysterious, because they just didn't hold with such\n" +
    " nonsense.";
  private InputStream in = null;
  private ObjectInputStream obIn = null;
  private OutputStream out = null;
  private ObjectOutputStream obOut = null;

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
      } while (clientCount < NUM_CLIENTS); // continue accepting clients until we have enough connected
    } catch(IOException ioe){
      System.out.println("Something went wrong with connection");
      ioe.printStackTrace();
    }
  } // end Server constructor

  class ThreadedServer extends Thread {
    private Socket cs = null; // Client Socket

    public ThreadedServer(Socket _cs) {
      cs = _cs;

      //make sure you actually hit the run method

    } // end ThreadedServer constructor

    @Override
    public void run() {
      //set up IO
      System.out.println("In run");
      try {
        out = cs.getOutputStream();
        obOut = new ObjectOutputStream(out);

        in = cs.getInputStream();
        obIn = new ObjectInputStream(in);

        //create typeRace object
        TypeRace tr = new TypeRace(PARAGRAPH);

        obOut.writeObject(tr);
        obOut.flush();

        //retrieve typeRace object
        tr = (TypeRace) obIn.readObject();

        // test

        //compare the pargraphs
        String typed = tr.getPTyped();

        int count = 0; // count the number of errors

        //try to compare each character in the string
        //if the typed paragraph is longer, increment the num errors by the number of extra characters
        if(typed.length() > PARAGRAPH.length()){
          //compare each character in the string
          for(int i = 0; i < PARAGRAPH.length(); i++){
            char c1 = PARAGRAPH.charAt(i);
            char c2 = typed.charAt(i);
            if(c1 != c2){
              count++;
            }
          count += (typed.length() - PARAGRAPH.length()); //add the difference
        } else {
          for(int i = 0; i < typed.length(); i++){
            char c1 = PARAGRAPH.charAt(i);
            char c2 = typed.charAt(i);
            if(c1 != c2){
              count++;
            }
          count += (PARAGRAPH.length() - typed.length());
        }

        tr.setNumErrors(count);
        System.out.println(count);


      } catch (ClassNotFoundException | IOException ioe) {
        System.out.println("Error connecting to client");
        ioe.printStackTrace();
      }

      //send over the typeRace object

      // recieve typeRace object from client

      //compare the pToType and pTyped

        //tally up errors

      //send list of winners back to client

      //close connections

    } // end run
  } // end class ThreadedServer
} // end class Server
