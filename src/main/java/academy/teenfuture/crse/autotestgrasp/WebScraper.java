package org.example.lib;


public interface WebScraper<T, K> extends WebScraperLifeCycle {
	void start();
	Pipe<T, K> getPipe();
	void setPipe(Pipe<T, K> pipe);
}
