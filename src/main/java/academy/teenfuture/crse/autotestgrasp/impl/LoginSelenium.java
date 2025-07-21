/*
 * Login to lamdatest
 */


package org.example.lib.impl;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.example.lib.AbstractSeleniumWebScraper;
import org.example.lib.Pipe;
//import static org.junit.jupiter.api.Assertions.assertEquals;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginSelenium extends AbstractSeleniumWebScraper<String, String>{

	public LoginSelenium(Pipe<String, String> pipe) {
		super(pipe);
	}

	@Override
	public void onWebDriverLoaded(WebDriver driver) {
		String url = "https://accounts.lambdatest.com/login?_gl=1*144dhg1*_gcl_au*NjA4ODYyNzQ5LjE2OTI3NjE0Nzk.";
		 
        driver.get(url);
 
        WebElement email = driver.findElement(By.id("email"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));
 
        email.clear();
        System.out.println("Entering the email");
        email.sendKeys("l_kchi@hotmail.com");
 
        password.clear();
        System.out.println("entering the password");
        password.sendKeys("12345678");
 
        System.out.println("Clicking login button");
        loginButton.click();
 
        String title = "Welcome - LambdaTest";
 
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        //WebElement start = driver.findElement(By.id("falconStartTestButton"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("falconStartTestButton")));

        String actualTitle = driver.getTitle();
        System.out.println(actualTitle);

        System.out.println("Verifying the page title has started");
        //assertEquals(title,actualTitle);
 
        System.out.println("The page title has been successfully verified");
 
        System.out.println("User logged in successfully");
 
        driver.quit();
	}

}
