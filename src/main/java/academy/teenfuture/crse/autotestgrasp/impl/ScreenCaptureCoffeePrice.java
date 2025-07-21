/*
 * Screen Capture Coffee Price
 */
package org.example.lib.impl;

import java.util.HashMap;
import java.util.Map;

import org.example.lib.AbstractPlayWrightWebScraper;
import org.example.lib.Pipe;

import com.microsoft.playwright.Page;
import java.nio.file.Paths;

public class ScreenCaptureCoffeePrice extends AbstractPlayWrightWebScraper<String, String> {


	public ScreenCaptureCoffeePrice(Pipe<String, String> pipe) {
		super(pipe);
	}

	public void onPageLoaded(Page page) {
    page.navigate("https://www.4freeapp.com/2019/08/2019_18.html");
    page.screenshot(new Page.ScreenshotOptions()
      .setPath(Paths.get("screenshot.png")));
	}

}
