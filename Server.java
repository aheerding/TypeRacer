/** @author Alex Heerding
    @version 1.0
    Type racing game server. This threaded server will start up and wait for 4
    clients to connect. Then it will send out a typeRace object to each client.
    Once the client sends the object back, the server will rank them and end the
    race.
*/
import java.io.*;
import java.net.*;
import java.util.Vector;

public class Server {
  //attributes
  private int clientCount = 0; // keeps track of number of connected clients
  private final int NUM_CLIENTS = 4; // desired number of competitors
  private final String PARAGRAPH = "Mr. and Mrs. Dursley, of number four, " +
    "Privet Drive, were proud to say that they were perfectly normal, thank " +
    "you very much. They were the last people you'd expect to be involved in " +
    "anything strange or mysterious, because they just didn't hold with such" +
    " nonsense.";
  private Vector<ObjectOutputStream> outputs;
  private Vector<String> winners; //store userNames of all logged in members
  private Vector<ThreadedServer> threads = null;

  public static void main(String[] args) {
    new Server();
  } // end main

  public Server () {
    //try and set up a server
    outputs =  new Vector<ObjectOutputStream>();
    winners = new Vector<String>();
    threads = new Vector<ThreadedServer>();
    try {
      //set up a port
      ServerSocket ss = new ServerSocket(16789);

      Socket cs = null; // client socket

      //wait for client connection
      while(true){
        System.out.println("Waiting for a connection..."); // mostly so we can see that the server is actually running
        cs = ss.accept();
        ThreadedServer ts = new ThreadedServer(cs);
        ts.start();
      }
    } catch(IOException ioe){
      System.out.println("Something went wrong with connection");
      ioe.printStackTrace();
    }
  } // end Server constructor

  class ThreadedServer extends Thread {
    private Socket cs = null; // Client Socket
    private InputStream in = null;
    private ObjectInputStream obIn = null;
    private OutputStream out = null;
    private ObjectOutputStream obOut = null;

    public ThreadedServer(Socket _cs) {
      cs = _cs;
    } // end ThreadedServer constructor

    @Override
    public void run() {
      //set up IO
      System.out.println("In run");
      try {
        in = cs.getInputStream();
        obIn = new ObjectInputStream(in);

        out = cs.getOutputStream();
        obOut = new ObjectOutputStream(out);

        outputs.add(obOut); // store all players streams

        //create typeRace object
        TypeRace tr = new TypeRace(PARAGRAPH);

        obOut.writeObject(tr);
        obOut.flush();

        Object ob = null;
        //while((ob=obIn.readObject()) != null){
        while(true){
          ob=obIn.readObject(); // TODO solve error found here - java.io.StreamCorruptedException: invalid type code: 00

          if(ob instanceof TypeRace){
            tr = (TypeRace) ob;

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
              }
              count += (typed.length() - PARAGRAPH.length()); //add the difference
            } else {
              for(int i = 0; i < typed.length(); i++){
                char c1 = PARAGRAPH.charAt(i);
                char c2 = typed.charAt(i);
                if(c1 != c2){
                  count++;
                }
              }
              count += (PARAGRAPH.length() - typed.length());
            }

            tr.setNumErrors(count);
            //System.out.println(count);
            String win = tr.getName() + " " + count;

            //add the winner to the vector
            winners.add(win);
            System.out.println(winners);
            //if the winners list is full, send it out to all the Clients
            if(winners.size() == 4){
              TypeRace raceOver = new TypeRace("");
              raceOver.setWinners(winners);
              for(ObjectOutputStream o : outputs){
                o.writeObject(raceOver);
                o.flush();
              }
            }
          } else if(ob instanceof Message){
                Message msg = (Message)ob;
                if(msg == null){
                    for(ObjectOutputStream o : outputs){
                        o.writeObject(new Message("Server", "A client has disconnected"));
                    }
                }
                for(ObjectOutputStream o : outputs){
                    System.out.println("Sending message from server.");
                    o.writeObject(msg);
                    o.flush();
                }
          }
        }
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
