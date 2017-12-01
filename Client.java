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
  private final String IP_ADDRESS = "129.21.122.14"; //MUST CHANGE FOR NEW MACHINE
  private final int PORT = 16789;
  private InputStream in = null;
  private ObjectInputStream obIn = null;
  private OutputStream out = null;
  private ObjectOutputStream obOut = null;
  private TypeRace tr = null;

  //login window
  private JFrame jfLogin = null; // initial login JFrame
  private JTextField jtfName = null;
  private JButton jbConnect = null;

  //racing window
  private JFrame jfRace = null; // JFrame that will display the racing minigame
  private JTextArea jtaPtoType = null;
  private JTextArea jtaPTyped = null;
  private JButton jbFinished = null; // button displayed at the bottom of the window - once user clicks it, retrieve paragraph and send to server

  //Results window
  private JFrame jfResults = null;
  private JPanel jpResults = null;
  private JLabel jlResults = null;
  public static void main(String[] args) {
    new Client();
  } // end main

  public Client () {
    //display login box asking user for userName
    jfLogin = new JFrame();
    JPanel jpLogin = new JPanel(new BorderLayout());
    JPanel jpName = new JPanel();
    JLabel jlLogin = new JLabel("Enter a username:");
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
    jpName.add(jlLogin);
    jpName.add(jtfName);
    jpLogin.add(jpName, BorderLayout.CENTER);
    jpLogin.add(jbConnect, BorderLayout.SOUTH);
    jfLogin.add(jpLogin);

    jfLogin.pack();
    jfLogin.setSize(500, 150);
    jfLogin.setLocationRelativeTo(null);
    jfLogin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jfLogin.setVisible(true);
  } // end constructor

  //attempts a connection with the server
  public void connect() {
    //try to connect to server
    try {
      Socket s = new Socket(IP_ADDRESS, PORT);

      in = s.getInputStream();
      obIn = new ObjectInputStream(in);

      out = s.getOutputStream();
      obOut = new ObjectOutputStream(out);

      //hide login Window
      jfLogin.setVisible(false);

      //display GUI
      showRaceFrame();
      //wait for server to send over the typeRace object
      tr = (TypeRace) obIn.readObject();
      //display pToType in textArea
      jtaPtoType.setText(tr.getpToType());

      //wait for server to send back the victory list

      //display victory list
    } catch(IOException e) {
      //display message if no server is running
      JOptionPane.showMessageDialog(jbConnect, "There was an error connecting." +
      " Please try again later.");

      //For testing purposes, TODO: comment out before final presentation
      e.printStackTrace();
    } catch (ClassNotFoundException ce) {
      JOptionPane.showMessageDialog(null, "An error occured");
      ce.printStackTrace();
    }
  } // end connect

  //add the text from jtaPTyped to the typeRace object and send it back to the server
  public void sendFinished() {
    try {
      tr.setpTyped(jtaPTyped.getText());
      tr.setName(name);
      obOut.writeObject(tr);
      obOut.flush();

      //hide playing JFrame
      showWinner();

      tr = (TypeRace) obIn.readObject();
      //get winners
      Vector<String> winners = tr.getWinners();
      //System.out.println(winners);
      //get winners and display it on the JFrame
      //convert to string array and set it as a JList
      //String allWinners = "";
      //for(String s : winners){
      //  allWinners += s + "\n";
      //}
      JList jlWinners = new JList(winners);
      jlResults.setText("");
      jpResults.add(jlWinners, BorderLayout.CENTER);
      //System.out.println(allWinners);
      //jlResults.setText(allWinners);
    } catch(ClassNotFoundException | IOException ioe) {
      System.out.println("An error occured.");
      ioe.printStackTrace();
    }
  }

  //set up and show the racing window
  public void showRaceFrame() {
    System.out.println("In showRaceFrame"); // testing purposes
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
        sendFinished();
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
  } // end showRaceFrame

  //close the racing window and display a waiting message until the results come in
  public void showWinner() {
    jfRace.setVisible(false);
    jfResults = new JFrame();
    jpResults = new JPanel(new BorderLayout());
    jlResults =  new JLabel("Waiting for other players...");
    jpResults.add(jlResults, BorderLayout.CENTER);
    jfResults.add(jpResults);
    jfResults.pack();
    jfResults.setLocationRelativeTo(null);
    jfResults.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jfResults.setVisible(true);
  }

  //closes all connections
  public void disconnect() {
    // close all open connections
  } //end disconnect

} // end class Client
