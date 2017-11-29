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

public class Client {

  //attributes
  private String name = "";
  private final String IP_ADDRESS = "129.21.123.1"; //MUST CHANGE FOR NEW MACHINE
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

  public static void main(String[] args) {
    new Client();
  } // end main

  public Client () {
    //display login box asking user for userName
    jfLogin = new JFrame();
    JPanel jpLogin = new JPanel();
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
    jpLogin.add(jlLogin);
    jpLogin.add(jtfName);
    jpLogin.add(jbConnect);
    jfLogin.add(jpLogin);

    jfLogin.pack();
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

      //display GUI with a waiting for opponents message in textArea
      showRaceFrame();
      //wait for server to send over the typeRace object
      TypeRace tr = (TypeRace) obIn.readObject();
      //display pToType in textArea
      jtaPtoType.setText(tr.getpToType());
      // once user clicks the finished button get text from the textArea and send to server

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

  }

  //set up and show the racing window
  public void showRaceFrame() {
    System.out.println("In showRaceFrame"); // testing purposes
    jfRace = new JFrame();
    JPanel jpRace = new JPanel();
    JPanel jpToType = new JPanel();
    JPanel jpTyped = new JPanel();
    JLabel jlToType = new JLabel("Text to type: ");
    jtaPtoType = new JTextArea(30, 20);
    jtaPtoType.setEditable(false);
    JLabel jlType = new JLabel("Your typing: ");
    jtaPTyped = new JTextArea(30, 20);
    JButton jbFinished = new JButton("Finish");

    //TODO: add actionlistener to send the typeRace object back
    jbFinished.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        sendFinished();
      }
    });

    jpToType.add(jlToType);
    jpToType.add(jtaPtoType);
    jpRace.add(jpToType, BorderLayout.NORTH);

    jpTyped.add(jlType);
    jpTyped.add(jtaPTyped);
    jpRace.add(jpTyped, BorderLayout.CENTER);
    jpRace.add(jbFinished, BorderLayout.SOUTH);

    jfRace.add(jpRace);

    jfRace.pack();
    jfRace.setSize(1000, 500);
    jfRace.setLocationRelativeTo(null);
    jfRace.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jfRace.setVisible(true);
  } // end showRaceFrame

  //closes all connections
  public void disconnect() {
    // close all open connections
  } //end disconnect

} // end class Client
