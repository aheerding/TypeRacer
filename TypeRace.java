/** @author Alex Heerding
    @version 1.0
    Typerace object holds all the data needed for TypeRacer game. One object is
    generated per player and is sent bewteen the server and client.
*/
import java.io.Serializable;

public class TypeRace implements Serializable{

  //attributes
  private String pToType = ""; // paragraph sent out for clients to type
  private String pTyped = ""; //player's typed paragraph - this is grabbed from the textArea in the client
  private String name = ""; //player's name - set when the server sends over the original typeRace object
  private static final long serialVersionUID = -883901947013771075L;

  public TypeRace (String _pToType) {
    pToType = _pToType;
  } // end constructor

  //Getters
  public String getpToType () {
    return pToType;
  } // end getpToType

  //Setters
  public void setpTyped(String _pTyped) {
    pTyped = _pTyped;
  } // end setpTyped

  public void setName(String _name) {
    name = _name;
  }

  public static void main(String[] args) { } // main for testing purposes
} // end class
