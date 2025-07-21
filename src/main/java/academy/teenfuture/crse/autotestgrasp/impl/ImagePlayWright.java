package org.example.lib.impl;

import java.util.regex.Pattern;

import org.example.lib.AbstractPlayWrightWebScraper;
import org.example.lib.Pipe;
//import org.junit.Assert;
import org.springframework.beans.factory.annotation.Value;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import java.util.*;


public class ImagePlayWright extends AbstractPlayWrightWebScraper<String, byte[]>{

	public ImagePlayWright(Pipe<String, byte[]> pipe) {
		super(pipe);
	}
	
	@Override
	public void onPageLoaded(Page page) {
		page.navigate("https://free-images.com/");
    List<ElementHandle> elements = page.querySelectorAll("img");
    byte[] bytes = elements.get(0).screenshot();
    this.getPipe().setOutput(bytes);
	}
	
}
