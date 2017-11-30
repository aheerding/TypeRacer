/** @author Alex Heerding
    @version 1.0
    Typerace object holds all the data needed for TypeRacer game. One object is
    generated per player and is sent bewteen the server and client.
*/
import java.io.Serializable;
import java.util.ArrayList;

public class TypeRace implements Serializable{

  //attributes
  private String pToType = ""; // paragraph sent out for clients to type
  private String pTyped = ""; //player's typed paragraph - this is grabbed from the textArea in the client
  private String name = ""; //player's name - set when the server sends over the original typeRace object
  private int numErrors = 0;
  private ArrayList<String> winners; // store the winners names
  private static final long serialVersionUID = -883901947013771075L;

  public TypeRace (String _pToType) {
    pToType = _pToType;
    winners = new ArrayList<String>();
  } // end constructor

  //Getters
  public String getpToType () {
    return pToType;
  } // end getpToType

  public String getPTyped() {
    return pTyped;
  }

  public int getNumErrors() {
    return numErrors;
  }

  public ArrayList getWinners() {
    return winners;
  }

  public String getName() {
    return name;
  }

  //Setters
  public void setpTyped(String _pTyped) {
    pTyped = _pTyped;
  } // end setpTyped

  public void setName(String _name) {
    name = _name;
  }

  public void setNumErrors(int _numErrors) {
    numErrors = _numErrors;
  }

  public void addWinner(String name){
    winners.add(name);
  }

  public static void main(String[] args) { } // main for testing purposes
} // end class
