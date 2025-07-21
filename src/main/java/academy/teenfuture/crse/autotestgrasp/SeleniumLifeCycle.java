package org.example.lib;


import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebDriver;

public interface SeleniumLifeCycle extends WebScraperLifeCycle {
	void onChromeOptionsLoaded(ChromeOptions options);
	void onWebDriverLoaded(WebDriver webDriver);
}
