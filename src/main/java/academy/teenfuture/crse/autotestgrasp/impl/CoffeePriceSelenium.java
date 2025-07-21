package org.example.lib.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.lib.AbstractSeleniumWebScraper;
import org.example.lib.Pipe;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CoffeePriceSelenium extends AbstractSeleniumWebScraper<String, HashMap<String, String>> {


	public CoffeePriceSelenium(Pipe<String, HashMap<String, String>> pipe) {
		super(pipe);
	}

	@Override
	public void onWebDriverLoaded(WebDriver webDriver) {
		HashMap<String, String> map = new HashMap<>();
		webDriver.get("https://www.4freeapp.com/2019/08/2019_18.html");
    	List<WebElement> tableRows = webDriver.findElements(By.tagName("tr"));
    	for(int i=0;i<tableRows.size();i++) {
    		List<WebElement>  tableCells = tableRows.get(i).findElements(By.tagName("td"));
    		String value = "";
    		try{
    			value = tableCells.get(1).getText();
    		}catch (IndexOutOfBoundsException ex) {
    			value = "";
    		}finally {
    			map.put(tableCells.get(0).getText(), value);
    		}
    	}
    	for (Map.Entry<String, String> set :
            map.entrySet()) {

           // Printing all elements of a Map
           System.out.println(set.getKey() + " = "
                              + set.getValue());
           //if(set.getKey().equals(this.pipe.getInput()))
              //this.getPipe().setOutput(set.getValue());
       }
      this.getPipe().setOutput(map);
    	System.out.println("complete");
    }

	

}
