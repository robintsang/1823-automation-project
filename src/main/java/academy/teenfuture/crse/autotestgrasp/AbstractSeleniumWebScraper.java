package org.example.lib;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public abstract class AbstractSeleniumWebScraper<T, K> extends AbstractWebScraper<T, K> implements SeleniumWebScraper <T, K>{

	public AbstractSeleniumWebScraper(Pipe<T, K> pipe) {
		super(pipe);
	}

	@Override
	public void onChromeOptionsLoaded(ChromeOptions options) {
    options.addArguments("--window-size=1920,1920"); // capture image   
		options.addArguments("--headless=new");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--remote-allow-origins=*");
    //options.addArguments("--start-maximized");    
	}

	public abstract void onWebDriverLoaded(WebDriver webDriver);

	@Override
	public void onInit() {
    System.out.println("Selenium is created");
    WebDriverManager.chromedriver().setup();   
		ChromeOptions options = new ChromeOptions();
		this.onChromeOptionsLoaded(options);
		WebDriver webDriver = new ChromeDriver(options);
		this.onWebDriverLoaded(webDriver);
	}
	
	@Override
	public void OnDestroy() {
		System.out.println("Selenium is destroyed");
	}
	
}
