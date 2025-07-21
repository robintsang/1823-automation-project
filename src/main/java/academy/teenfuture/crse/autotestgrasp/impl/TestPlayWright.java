package org.example.lib.impl;

import java.util.HashMap;
import java.util.Map;

import org.example.lib.AbstractPlayWrightWebScraper;
import org.example.lib.Pipe;

import com.microsoft.playwright.Page;

public class TestPlayWright extends AbstractPlayWrightWebScraper<String, HashMap<String, String>> {


	public TestPlayWright(Pipe<String, HashMap<String, String>> pipe) {
		super(pipe);
	}

	public void onPageLoaded(Page page) {
		Pipe<String, HashMap<String, String>> pipe = this.getPipe();
		this.setPipe(pipe);
		pipe.setInput("可可瑪奇朵");
		String coffeeName = pipe.getInput();
		page.navigate("https://www.4freeapp.com/2019/08/2019_18.html");
	    
		int rowCount = page.locator("//table//tbody//tr").count();
	    HashMap<String, String> map = new HashMap<>();
	    for (int i=1;i<rowCount; i++) {
	    	map.put(page.locator("//table//tbody//tr").nth(i).locator("//td").nth(0).textContent(),
	    			page.locator("//table//tbody//tr").nth(i).locator("//td").nth(1).textContent());
	    }
		for (Map.Entry<String, String> set :
            map.entrySet()) {

           // Printing all elements of a Map
           System.out.println(set.getKey() + " = "
                              + set.getValue());
       }
	    System.out.println("completed");
	    this.getPipe().setOutput(map);
	}

	@Override
	public Pipe<String, HashMap<String, String>> getPipe() {
		return super.getPipe();
	}

	@Override
	public void setPipe(Pipe<String, HashMap<String, String>> pipe) {
		super.setPipe(pipe);
	}


}
