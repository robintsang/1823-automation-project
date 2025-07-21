package org.example.lib.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.lib.AbstractSeleniumWebScraper;
import org.example.lib.Pipe;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.io.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageSelenium extends AbstractSeleniumWebScraper<String, byte[]> {


	public ImageSelenium(Pipe<String, byte[]> pipe) {
		super(pipe);
	}

  @Override
	public void onWebDriverLoaded(WebDriver driver) {
		String url = "https://free-images.com/";
    driver.get(url);
    WebElement image = driver.findElement(By.tagName("img"));
    File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    Point point = image.getLocation();
    int xcordinate = point.getX();
    int ycordinate = point.getY();
    int imageWidth = image.getSize().getWidth();
    //Retrieve height of element.
    int imageHeight = image.getSize().getHeight();
    //Reading full image screenshot.
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
    BufferedImage img = ImageIO.read(screen);
    //cut Image using height, width and x y coordinates parameters.
    BufferedImage destination = img.getSubimage(xcordinate, ycordinate, imageWidth, imageHeight);
    ImageIO.write(destination, "png", baos); 
    }catch(IOException exception){
      System.out.println(exception);
    }
    byte[] bytes = baos.toByteArray();
    this.getPipe().setOutput(bytes);
	}
}