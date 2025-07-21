package academy.teenfuture.crse.service; 

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.microsoft.playwright.*;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;
import academy.teenfuture.crse.modal.*;
import academy.teenfuture.crse.repository.*;

import org.example.lib.WebScraper;
import org.example.lib.Pipe;
import org.example.lib.impl.PipeImpl;
import org.example.lib.impl.TestPlayWright;
import org.example.lib.impl.TestSelenium;
import org.example.lib.impl.CoffeePricePlayWright;
import org.example.lib.impl.CoffeePriceSelenium;
import org.example.lib.impl.ScreenCaptureCoffeePrice;
import org.example.lib.impl.ScreenCaptureCoffeePriceSelenium;
import org.example.lib.impl.LoginSelenium;
import org.example.lib.impl.LoginPlayWright;
import org.example.lib.impl.VideoPlayWright;
import org.example.lib.impl.VideoPlayWright2;
import org.example.lib.impl.ImagePlayWright;
import org.example.lib.impl.ImageSelenium;
import org.example.lib.impl.SearchEnginePlayWright;

import java.io.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import academy.teenfuture.crse.autotestgrasp.ImgRepository;
import academy.teenfuture.crse.autotestgrasp.ImgData;

@Service
public class CoffeePriceService {
  /* 
  private final CoffeePriceRepository repository;
  private final ImgRepository imgRepository;
  @Value("${scrap.login_email}")
  private String email;
  @Value("${scrap.login_password}")
  private String password;  
  @Value("${scrap.login_page}")
  private String loginPage;

  public CoffeePriceService(CoffeePriceRepository repository, ImgRepository imgRepository){
    this.repository = repository;
    this.imgRepository = imgRepository;
  }
  
  public String fetchCoffeePrice(String coffeeName) {      
      CoffeePrice coffeePrice = repository.findByCoffeeName(coffeeName); 
      if (null==coffeePrice) {
        WebScraper<String,HashMap<String, String>> scraper = new TestPlayWright(new PipeImpl<String, HashMap<String, String>>());
        Pipe pipe = scraper.getPipe();
        pipe.setInput(coffeeName);                

        //WebScraper<String,HashMap<String, String>> scraper = new CoffeePricePlayWright(new PipeImpl<String, HashMap<String, String>>());
        scraper.start();
        HashMap<String, String> map = scraper.getPipe().getOutput();
        for (Map.Entry<String, String> set :
        map.entrySet()) {
          CoffeePrice coffeePrice2 = new CoffeePrice();
          coffeePrice2.setCoffeeName(set.getKey());
          coffeePrice2.setCoffeePrice(set.getValue());
          this.repository.save(coffeePrice2);          
        }
      }
      return repository.findByCoffeeName(coffeeName).getCoffeePrice();
  }
  */
  /*
  1) install specific version of googlechrome according to chromedriver. 
    wget --no-verbose -O /tmp/chrome.deb https://dl.google.com/linux/chrome/deb/pool/main/g/google-chrome-stable/google-chrome-stable_114.0.5735.198-1_amd64.deb \
  && apt install -y /tmp/chrome.deb \
  && rm /tmp/chrome.deb
  2) addArguments below
  3) install WebDriverManager
  */
  /* 
  public String fetchCoffeePriceSelenium(String coffeeName) {
      CoffeePrice coffeePrice = repository.findByCoffeeName(coffeeName); 
      if (null==coffeePrice) {   
        WebScraper<String,HashMap<String, String>> scraper = new TestSelenium(new PipeImpl<String, HashMap<String, String>>());
        Pipe pipe = scraper.getPipe();
        pipe.setInput(coffeeName);                
        //WebScraper<String,HashMap<String, String>> scraper = new CoffeePriceSelenium(new PipeImpl<String, HashMap<String, String>>());
        scraper.start();
        HashMap<String, String> map = scraper.getPipe().getOutput();
        for (Map.Entry<String, String> set :
        map.entrySet()) {
            CoffeePrice coffeePrice2 = new CoffeePrice();
            coffeePrice2.setCoffeeName(set.getKey());
            coffeePrice2.setCoffeePrice(set.getValue());
            this.repository.save(coffeePrice2);       
        }
      }
      return repository.findByCoffeeName(coffeeName).getCoffeePrice();
    }

    public void screenCapture() {
      WebScraper<String, String> scraper = new ScreenCaptureCoffeePrice(new PipeImpl<String, String>());
      scraper.start();      
    }

    public void screenCapture2() {
      WebScraper<String, String> scraper = new ScreenCaptureCoffeePriceSelenium(new PipeImpl<String, String>());
      scraper.start();      
    }

    public void login2(){
      WebScraper<String, String> scraper = new LoginSelenium(new PipeImpl<String, String>());
      scraper.start();        
    }

    public void login(){
      WebScraper<String, String> scraper = new LoginPlayWright(new PipeImpl<String, String>(), email,password,loginPage);
      scraper.start();
    }

    public void tracing(){
      WebScraper<String, String> scraper = new VideoPlayWright(new PipeImpl<String, String>());
      scraper.start();
    }

    public void tracing2(){
      WebScraper<String, String> scraper = new VideoPlayWright2(new PipeImpl<String, String>());
      scraper.start();
    }

    public void image(){
      WebScraper<String, byte[]> scraper = new ImagePlayWright(new PipeImpl<String, byte[]>());
      scraper.start(); 
      byte[] bytes = scraper.getPipe().getOutput(); 
      try{
        ByteArrayInputStream inStreambj = new ByteArrayInputStream(bytes); 
        BufferedImage newImage = ImageIO.read(inStreambj);
        ImageIO.write(newImage, "png", new File("final.png"));
        System.out.println("Image generated from the byte array.");
        ImgData imgData = new ImgData();
        imgData.setImg(bytes);
        this.imgRepository.save(imgData);             
      }catch(IOException exception){
          System.out.println(exception);
        }    
    }
    public void imageSelenium(){
      WebScraper<String, byte[]> scraper = new ImageSelenium(new PipeImpl<String, byte[]>());
      scraper.start(); 
      byte[] bytes = scraper.getPipe().getOutput(); 
      try{
        ByteArrayInputStream inStreambj = new ByteArrayInputStream(bytes); 
        BufferedImage newImage = ImageIO.read(inStreambj);
        ImageIO.write(newImage, "png", new File("final2.png"));
        System.out.println("Image generated from the byte array.");
        ImgData imgData = new ImgData();
        imgData.setImg(bytes);
        this.imgRepository.save(imgData);             
      }catch(IOException exception){
          System.out.println(exception);
        }  
    }

    public void searching(){
      WebScraper<String, String> scraper = new SearchEnginePlayWright(new PipeImpl<String, String>());
      scraper.start();
    }
    */
}