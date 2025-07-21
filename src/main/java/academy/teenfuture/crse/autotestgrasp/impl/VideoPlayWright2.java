/*
 * Screen Capture Coffee Price
 */
package org.example.lib.impl;

import java.util.HashMap;
import java.util.Map;

import org.example.lib.AbstractPlayWrightWebScraper;
import org.example.lib.Pipe;

import com.microsoft.playwright.*;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import org.example.lib.AbstractPlayWrightWebScraper;
import org.example.lib.Pipe;

import com.microsoft.playwright.options.AriaRole;

import academy.teenfuture.crse.autotestgrasp.UserAgentData;

public class VideoPlayWright2 extends AbstractPlayWrightWebScraper<String, String> {


	public VideoPlayWright2(Pipe<String, String> pipe) {
		super(pipe);
	}

  @Override
	public void onBrowserLoaded(Browser browser) {
    UserAgentData userAgentData = new UserAgentData();
    String userAgent = userAgentData.getRandomUserAgent();
		 context = browser.newContext(new Browser.NewContextOptions()
     .setUserAgent(userAgent)
     .setRecordVideoDir(Paths.get("videos/"))
     .setRecordVideoSize(1920, 1080));
	}

  @Override
	public void onPageLoaded(Page page) {
    context.tracing().start(new Tracing.StartOptions()
      .setScreenshots(true)
      .setSnapshots(true)
      .setSources(true));
      page.navigate("https://accounts.lambdatest.com/login?_gl=1*144dhg1*_gcl_au*NjA4ODYyNzQ5LjE2OTI3NjE0Nzk.");
      page.getByPlaceholder("Email").fill("l_kchi@hotmail.com");
      page.getByPlaceholder("Password").fill("12345678");
      //page.locator("#login-button").click();
      //page.getByRole(AriaRole.BUTTON).click();
      page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName(
                  Pattern.compile("Login", Pattern.CASE_INSENSITIVE))).click();
      page.waitForSelector("#falconStartTestButton");   
	    System.out.println("completed");
    page.screenshot(new Page.ScreenshotOptions()
      .setPath(Paths.get("screenshot.png")));
    context.tracing().stop(new Tracing.StopOptions()
      .setPath(Paths.get("trace3.zip")));
	}

}
