package org.example.lib.impl;

import org.example.lib.Pipe;

public class PipeImpl<T, K> implements Pipe<T, K>{

	T input;
	K output;
	
	@Override
	public void setInput(T input) {
		this.input = input;
	}

	@Override
	public void setOutput(K output) {
		this.output = output;
	}

	@Override
	public T getInput() {
		return this.input;
	}

	@Override
	public K getOutput() {
		return this.output;
	}

}
