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

public class VideoPlayWright extends AbstractPlayWrightWebScraper<String, String> {


	public VideoPlayWright(Pipe<String, String> pipe) {
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
    page.navigate("https://www.4freeapp.com/2019/08/2019_18.html");
    int rowCount = page.locator("//table//tbody//tr").count();
	    HashMap<String, String> map = new HashMap<>();
	    for (int i=1;i<rowCount; i++) {
	    	map.put(page.locator("//table//tbody//tr").nth(i).locator("//td").nth(0).textContent(),
	    			page.locator("//table//tbody//tr").nth(i).locator("//td").nth(1).textContent());
	    }
		for (Map.Entry<String, String> set :
            map.entrySet()) {

           // Printing all elements of a Map
           System.out.println(set.getKey() + " = "
                              + set.getValue());
       }
	    System.out.println("completed");
    page.screenshot(new Page.ScreenshotOptions()
      .setPath(Paths.get("screenshot.png")));
    context.tracing().stop(new Tracing.StopOptions()
      .setPath(Paths.get("trace2.zip")));
	}

}
