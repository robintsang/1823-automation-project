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

import academy.teenfuture.crse.autotestgrasp.UserAgentData;

public class SearchEnginePlayWright extends AbstractPlayWrightWebScraper<String, String> {


	public SearchEnginePlayWright(Pipe<String, String> pipe) {
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
    page.navigate("https://hk.yahoo.com/");
    page.locator("#header-search-input").fill("Hi");
	  page.keyboard().press("Enter");
    page.screenshot(new Page.ScreenshotOptions()
      .setPath(Paths.get("search_engine_screenshot.png")));
    context.tracing().stop(new Tracing.StopOptions()
      .setPath(Paths.get("search_engine_trace.zip")));
      
	}

}
