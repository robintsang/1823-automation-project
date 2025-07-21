package org.example.lib;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public abstract class AbstractPlayWrightWebScraper<T, K> extends AbstractWebScraper<T, K> implements PlayWrightWebScraper<T, K> {
	
	protected Browser browser;
	protected BrowserContext context;
	
	public AbstractPlayWrightWebScraper(Pipe<T, K> pipe) {
		super(pipe);
	}

	@Override
	public void onInit() {
		Playwright playwright = Playwright.create();
		browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
		        .setHeadless(true));
		this.onBrowserLoaded(browser); 
		this.onBrowserContextLoaded(context);
	}

	@Override
	public void OnDestroy() {
		this.onBrowserContextClosed(context);
		this.onBrowserClosed(browser);
		System.out.println("PlayWright is destroyed");
	}

	@Override
	public void onBrowserLoaded(Browser browser) {
		 context = browser.newContext();
		  
	}

	@Override
	public void onBrowserContextLoaded(BrowserContext browserContext) {
		Page page = browserContext.newPage();
		this.onPageLoaded(page);
	}

	@Override
	public void onBrowserContextClosed(BrowserContext context) {
		context.close();
		
	}
	
	@Override
	public void onBrowserClosed(Browser browser) {
		browser.close();
	}
}
