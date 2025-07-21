package academy.teenfuture.crse.utility;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;

/*
 * Your test for PlayWright
 */
public class PlayWrightTests {

  @BeforeAll
  public static void setup() {
  }

  @Test
  public void testCase1() {

    MakeVideosPlayWright makeVideosPlayWright = new MakeVideosPlayWright();
    makeVideosPlayWright.start();
    
    // your code for testCase1
    System.out.println("testCase1");
    
    makeVideosPlayWright.stop();    
  }

  @Test
  public void testCase2() {
  
    MakeVideosPlayWright makeVideosPlayWright = new MakeVideosPlayWright();
    makeVideosPlayWright.start();
  
    // your code for testCase2
    System.out.println("testCase2");
  
    makeVideosPlayWright.stop();    
  }
  
  @AfterAll
  public static void bye() {
  }

}
