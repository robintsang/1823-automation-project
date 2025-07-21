package org.example.lib;

public interface Pipe<T, K> {
	void setInput(T input);
	void setOutput(K output);
	T getInput();
	K getOutput();
}
