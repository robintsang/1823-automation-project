package org.example.lib;
import com.microsoft.playwright.*;


public interface PlayWrightLifeCycle {
	void onBrowserLoaded(Browser browser);
	void onBrowserContextLoaded(BrowserContext browserContext);
	void onPageLoaded(Page page);
  void onBrowserContextClosed(BrowserContext browserContext);
	void onBrowserClosed(Browser browser);
}
