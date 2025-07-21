
package org.example.lib.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.lib.AbstractSeleniumWebScraper;
import org.example.lib.Pipe;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;

import org.apache.commons.io.FileUtils;

import org.openqa.selenium.OutputType;

import org.openqa.selenium.TakesScreenshot;

import org.openqa.selenium.WebDriver;
import java.io.IOException;

public class ScreenCaptureCoffeePriceSelenium extends AbstractSeleniumWebScraper<String, String> {


	public ScreenCaptureCoffeePriceSelenium(Pipe<String, String> pipe) {
		super(pipe);
	}

	@Override
	public void onWebDriverLoaded(WebDriver webDriver) {
		HashMap<String, String> map = new HashMap<>();
		webDriver.get("https://www.4freeapp.com/2019/08/2019_18.html");
    TakesScreenshot scrShot =((TakesScreenshot)webDriver);
    File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
    File DestFile=new File("screenshot2.png");
    try{
      FileUtils.copyFile(SrcFile, DestFile);
    } catch (IOException exception) {
      System.out.println("Some I/O went wrong");
    }
	}
}