/** @author Alex Heerding
    @version 1.0
    Type racing game client. Clients will boot up and prompt the user for a
    username. Clients will store that username and attempt to connect to the
    Server. Once the server sends over a typeRace object, the client will
    display the paragraph. Once the user presses "Finish" the text in the
    textArea will be added and sent back to the server to be evaluated.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.ConnectException;
import java.util.Vector;

public class Client {

  //attributes
  private String name = "";
  private final String IP_ADDRESS = "192.168.0.11"; //MUST CHANGE FOR NEW MACHINE
  private final int PORT = 16789;
  private InputStream in = null;
  private ObjectInputStream obIn = null;
  private OutputStream out = null;
  private ObjectOutputStream obOut = null;
  private TypeRace tr = null;
  private final String PARAGRAPH = "Mr. and Mrs. Dursley, of number four, " +
    "Privet Drive, were proud to say that they were perfectly normal, thank " +
    "you very much. They were the last people you'd expect to be involved in " +
    "anything strange or mysterious, because they just didn't hold with such" +
    " nonsense.";
  private InnerThread it = null;

  //login window
  private JFrame jfLogin = null; // initial login JFrame
  private JTextField jtfName = null;
  private JButton jbConnect = null;

  //racing window
  private JFrame jfRace = null; // JFrame that will display the racing minigame
  private JTextArea jtaPtoType = null;
  private JTextArea jtaPTyped = null;
  private JButton jbFinished = null; // button displayed at the bottom of the window - once user clicks it, retrieve paragraph and send to

  //Waiting Window
  private JFrame jfWait = null;
  private JPanel jpWait = null;
  private JLabel jlResults = null;

  //Results window
  private JFrame jfResults = null;
  private JPanel jpResults = null;

  //messaging window
  private JFrame jfChat = null;
  private JTextArea jtaMessages = null;
  private JPanel jpSend = null;
  private JTextField jtfSend = null;
  private JButton jbSend = null;
  private Boolean serverConnection = true;
  private Boolean hasFinished = false;

  //show rankings Window
  private JFrame jfShowRank = null;
  private JPanel jpShowRank = null;

  public static void main(String[] args) {
    new Client();
  } // end main

  public Client () {
    it = new InnerThread();
    jfChat = new JFrame();
    jtaMessages = new JTextArea(20, 10);
    jtaMessages.setEditable(false);
    jfChat.add(jtaMessages, BorderLayout.CENTER);

    jpSend = new JPanel();
    jtfSend = new JTextField(75);
    jtfSend.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Message m = new Message(name, jtfSend.getText());
            //System.out.println(m.toString());
            try{
                obOut.writeObject(m);
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
            jtfSend.setText("");
        }
    });
    jbSend = new JButton("Send");
    jbSend.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Message m = new Message(name, jtfSend.getText());
            //System.out.println(m.toString());
            try{
                obOut.writeObject(m);
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
            jtfSend.setText("");
        }
    });
    jpSend.add(jtfSend);
    jpSend.add(jbSend);

    jfChat.add(jpSend, BorderLayout.SOUTH);
    //display login box asking user for userName
    jfLogin = new JFrame();
    JPanel jpLogin = new JPanel(new BorderLayout());
    JPanel jpName = new JPanel(new BorderLayout());
    JLabel jlLogin = new JLabel("<html>Welcome to TypeRacer!<br>Once 4 people log in, you will be given a paragraph to type.<br>"
    + "Try to enter it as fast as you can with the fewest errors!<br>" + "Enter a username:<br></html>", SwingConstants.CENTER);
    jtfName = new JTextField(10);
    jbConnect = new JButton("Connect");

    jbConnect.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e){
        //make sure the user entered a valid username
        if(jtfName.getText().equals("")){
          JOptionPane.showMessageDialog(jtfName, "Please enter a username");
        } else {
          //store userName
          name = jtfName.getText();

          //connect to server
          connect();
        }
      }
    });

    //set up login window
    jpName.add(jlLogin, BorderLayout.CENTER);
    jpName.add(jtfName, BorderLayout.SOUTH);
    jpLogin.add(jpName, BorderLayout.CENTER);
    jpLogin.add(jbConnect, BorderLayout.SOUTH);
    jfLogin.add(jpLogin);

    jfLogin.pack();
    jfLogin.setSize(500, 200);
    jfLogin.setLocationRelativeTo(null);
    jfLogin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jfLogin.setVisible(true);
  } // end constructor

  //attempts a connection with the server
  public void connect() {
    //try to connect to serve
    try {
      //System.out.println("In connect");
      Socket s = new Socket(IP_ADDRESS, PORT);

      in = s.getInputStream();
      obIn = new ObjectInputStream(in);

      out = s.getOutputStream();
      obOut = new ObjectOutputStream(out);

      it.start();

      //hide login Window
      jfLogin.setVisible(false);

      //display GUI
      showRaceFrame();


      //wait for server to send back the victory list

      //display victory list
    } catch(IOException e) {
      //display message if no server is running
      JOptionPane.showMessageDialog(jbConnect, "There was an error connecting." +
      " Please try again later.");

      //For testing purposes, TODO: comment out before final presentation
      e.printStackTrace();
    }
  } // end connect


  //set up and show the racing window
  public void showRaceFrame() {
    //System.out.println("In showRaceFrame"); // testing purposes
    jfRace = new JFrame();
    JPanel jpRace = new JPanel(new BorderLayout());
    JPanel jpToType = new JPanel();
    JPanel jpTyped = new JPanel();
    JLabel jlToType = new JLabel("Text to type: ");
    jtaPtoType = new JTextArea(5, 40);
    jtaPtoType.setLineWrap(true);
    jtaPtoType.setWrapStyleWord(true);
    jtaPtoType.setEditable(false);
    JLabel jlType = new JLabel("Your typing: ");
    jtaPTyped = new JTextArea(5, 40);
    jtaPTyped.setLineWrap(true);
    jtaPTyped.setWrapStyleWord(true);
    JButton jbFinished = new JButton("Finish");

    //TODO: add actionlistener to send the typeRace object back
    jbFinished.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        hasFinished = true;
        try{
          TypeRace finished = new TypeRace("");
          finished.setpTyped(jtaPTyped.getText());
          finished.setName(name);
          obOut.writeObject(finished);
          obOut.flush();
          showWinner();
        } catch(IOException ioe){

        }

      }
    });

    jpToType.add(jlToType);
    JScrollPane jspToType = new JScrollPane(jtaPtoType);
    jpToType.add(jspToType);
    jpRace.add(jpToType, BorderLayout.NORTH);

    jpTyped.add(jlType);
    JScrollPane jspTyped = new JScrollPane(jtaPTyped);
    jpTyped.add(jspTyped);
    jpRace.add(jpTyped, BorderLayout.CENTER);
    jpRace.add(jbFinished, BorderLayout.SOUTH);

    jfRace.add(jpRace);

    jfRace.pack();
    jfRace.setSize(700, 350);
    jfRace.setLocationRelativeTo(null);
    jfRace.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jfRace.setVisible(true);

    jfChat.pack();
    jfChat.setLocationRelativeTo(null);
    jfChat.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jfChat.setVisible(true);
  } // end showRaceFrame

  //close the racing window and display a waiting message until the results come in
  public void showWinner() {
    jfRace.setVisible(false);
    jfWait = new JFrame();
    jpWait = new JPanel(new BorderLayout());
    jlResults =  new JLabel("Waiting for other players...");
    jpWait.add(jlResults, BorderLayout.CENTER);
    jfWait.add(jpWait);
    jfWait.pack();
    jfWait.setSize(200, 200);
    jfWait.setLocationRelativeTo(null);
    jfWait.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jfWait.setVisible(true);
  }

  public void showRankings() {
    jfShowRank = new JFrame();
    jfShowRank.add(jpShowRank);

    jfWait.setVisible(false);
    jfShowRank.pack();
    jfShowRank.setSize(200, 200);
    jfShowRank.setLocationRelativeTo(null);
    jfShowRank.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jfShowRank.setVisible(true);
  }

  //closes all connections
  public void disconnect() {
    // close all open connections
  } //end disconnect

  public class InnerThread extends Thread{
      @Override
      public void run(){
            Object ob = null;
            try {
              while((ob=obIn.readObject()) != null){
                //System.out.println(ob);
                if(ob instanceof Message){
                      Message m = (Message) ob;
                      jtaMessages.append("\n" + m.toString());
                      //System.out.println(m.toString());
                } else if(ob instanceof TypeRace){
                  TypeRace t =  (TypeRace) ob;
                  if(hasFinished == false){
                    //wait for server to send over the typeRace object
                    tr = (TypeRace)ob;
                    //display pToType in textArea
                    jtaPtoType.setText(PARAGRAPH);
                  } else {
                    //hide playing JFrame


                    TypeRace trDone = (TypeRace) ob;
                    //get winners

                    Vector<?> winners = (Vector<?>) trDone.getWinners();

                    //get winners and display it on the JFrame
                    //convert to string array and set it as a JList
                    //System.out.println(winners);
                    jpShowRank = new JPanel(new GridLayout(4, 0));
                    for(int i = 0; i < winners.size(); i++){
                      jpShowRank.add(new JLabel(i + ". " + (String)winners.get(i)));
                    }
                    showRankings();
                    //jpResults = new JPanel(new BorderLayout());
                    //JList jlWinners = new JList(winners);
                    //jlResults.setText("");
                    //jpResults.add(jlWinners, BorderLayout.CENTER);
                  }
                }
              }
            }
            catch(ClassNotFoundException | IOException ioe){
              ioe.printStackTrace();
            }

          }
  }

} // end class Client
