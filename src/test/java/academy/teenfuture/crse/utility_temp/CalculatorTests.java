package academy.teenfuture.crse.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import academy.teenfuture.crse.utility.Calculator;

/**
 * Do a Calculator Unit Test Demo
 *
 */
class CalculatorTests {

  /**
   * Test the addition
   */
  @Test
  void addsTwoNumbers1() {
    Calculator calculator = new Calculator();
    assertEquals(2.5f, calculator.dAdd(1.2f, 1.3f), "1.2 + 1.3 should equal 2.5");
  }

  @Test
  void addsTwoNumbers2() {
    Calculator calculator = new Calculator();
    assertEquals(0, calculator.dAdd(-1.5f, 1.5f), "-1.5 + 1.5 should equal 0.0");
  }

  @Test
  void minusTwoNumbers1() {
    Calculator calculator = new Calculator();
    assertEquals(13.0, calculator.dMinus(39.0, 26.0), "39.0 - 26.0 should equal 13.0");

  }

  @Test
  void minusTwoNumbers2() {
    Calculator calculator = new Calculator();
    assertEquals(0.0, calculator.dMinus(1.5f, 1.5f), "1.5 - 1.5 should equal 0.0");
  }

  @Test
  void multipleTwoNumbers() {
    Calculator calculator = new Calculator();
    assertEquals(1.44f, calculator.multiple(1.2f, 1.2f), "1.2 * 1.2 should equal 1.44");
  }

  @Test
  void divisionTwoNumbers1() {
    Calculator calculator = new Calculator();
    assertEquals(1.0f, calculator.division(1.2f, 1.2f), "1.2 / 1.2 should equal 1.0");
  }

  @Test
  void divisionTwoNumbers2() {
    Calculator calculator = new Calculator();
    assertEquals(0.5f, calculator.division(1.2f, 2.4f), "1.2 / 2.4 should equal 0.5");
  }

  @Test
  void getExponentofTwoNumbers() {
    Calculator calculator = new Calculator();
    assertEquals(8.0, calculator.dExponent(2.0, 3.0), "2.0 ^ 3.0 should equal 8.0");
  }



}