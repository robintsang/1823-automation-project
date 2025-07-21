package org.example.lib;

public abstract class AbstractWebScraper<T, K> implements WebScraper<T, K> {
		
	protected Pipe<T,K> pipe;
	
	public AbstractWebScraper(Pipe<T, K> pipe) {
		this.pipe = pipe;
	}
	
	@Override
	public void start() {
		this.onInit();
		this.OnDestroy();
	}

	@Override
	public Pipe<T, K> getPipe() {
		return this.pipe;
	}

	@Override
	public void setPipe(Pipe<T, K> pipe) {
		this.pipe = pipe;
	}


	
}
