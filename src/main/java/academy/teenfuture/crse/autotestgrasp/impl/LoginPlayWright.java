package org.example.lib.impl;

import java.util.regex.Pattern;

import org.example.lib.AbstractPlayWrightWebScraper;
import org.example.lib.Pipe;
//import org.junit.Assert;
import org.springframework.beans.factory.annotation.Value;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPlayWright extends AbstractPlayWrightWebScraper<String, String>{

  protected String email;
  
  protected String password;
  
  protected String loginPage;

	public LoginPlayWright(Pipe<String, String> pipe,String email, String password, String loginPage) {
		super(pipe);
    this.email = email;
    this.password = password;
    this.loginPage = loginPage;
	}
	
	@Override
	public void onPageLoaded(Page page) {
    	System.out.println(loginPage);
		page.navigate(loginPage);
		page.getByPlaceholder("Email").fill(email);
		page.getByPlaceholder("Password").fill(password);
		//page.locator("#login-button").click();
		//page.getByRole(AriaRole.BUTTON).click();
		page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName(
                Pattern.compile("Login", Pattern.CASE_INSENSITIVE))).click();
		page.waitForSelector("#falconStartTestButton");
		//assertEquals("Welcome - LambdaTest", page.title());
	}
	
}
