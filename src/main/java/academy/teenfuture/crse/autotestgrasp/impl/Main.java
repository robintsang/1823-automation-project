package org.example.lib.impl;

import org.example.lib.Pipe;
import org.example.lib.WebScraper;
import java.util.HashMap;

public class Main {
	public static void main(String[] argv) {
		WebScraper<String, HashMap<String, String>> scraper = new TestPlayWright(new PipeImpl<String, HashMap<String, String>>());
		/*WebScraper<String, String> scraper = new LoginPlayWright(new PipeImpl<String, String>());*/
		scraper.start();
		WebScraper<String, HashMap<String, String>> scraper2 = new TestSelenium(new PipeImpl<String,HashMap<String, String>>());
		scraper2.start();
	}
}
