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

public class Client {
  private String name = "";
  private JButton jbFinished; // button displayed at the bottom of the window - once user clicks it, retrieve paragraph and send to server
  public static void main(String[] args) {

  } // end main

  public Client () {
    //display login box asking user for userName

    //store userName

    //connect to server

  } // end constructor

  //attempts a connection with the server
  public void connect() {
    //hide login Window

    //try to connect to server

      //display message if no server is running

    //display GUI with a waiting for opponents message in textArea

    //wait for server to send over the typeRace object

    //display pToType in textArea

    // once user clicks the finished button get text from the textArea and send to server

    //wait for server to send back the victory list

    //display victory list
  }

  //closes all connections
  public void disconnect() {
    // close all open connections
  }

} // end class Client
