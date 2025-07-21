package academy.teenfuture.crse.utility;

/**
 * Do a String Works Demo
 *
 */
public class StringWork {

    /**
     * Do Addition
     * @param a string
     * @throws IllegalArgumentException due to In-valid String input 
     * @return true or false for input string same to "abc"
     */
    public boolean testStr(String a) {
      if (a==null) {
        throw new IllegalArgumentException("In-valid String(s) input");
      } else 
        return a == "abc";
    }

}