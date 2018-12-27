package org.checkstatus;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AttendanceStatus {
	
	public static WebDriver driver;
	static Properties prop = new Properties();
	static FileInputStream input = null;
	static List<WebElement> element = null;
	
	public static void getTime() {
		String statusDetails = null;
		int index = 0;
		try {
		readPropertiesFile();
		
		System.setProperty("webdriver.chrome.driver", "D:\\RAMA\\Selenium\\Common Jars\\chromedriver.exe");
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);

		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("--start-maximized");
		options.addArguments("--test-type");
		options.addArguments("--disable-extensions");
		
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.MINUTES);
		
		driver.get(prop.getProperty("url"));
		
		driver.findElement(By.xpath("//input[@title='Login']")).sendKeys(prop.getProperty("userName"));
		driver.findElement(By.xpath("//input[@title='Password']")).sendKeys("*****");
		
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		Thread.sleep(3000);
		driver.findElement(By.xpath("//img[@id='my_image']")).click();
		driver.findElement(By.xpath("//a[@href='/group/synportal/leave-and-attendance']")).click();
		
		Thread.sleep(3000);
		driver.navigate().refresh();
		
		WebDriverWait wait = new WebDriverWait(driver, 60);// 1 minute 
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@cellspacing='0']/tbody/tr")));
		
		element = driver.findElements(By.xpath("//table[@cellspacing='0']/tbody/tr"));
		index = element.size();
		for (int i = 0; i < index; i++) {
			statusDetails = element.get(i).getText();
		}
	/*	
		driver.findElement(By.xpath("//input[@name='ctl00$PlaceHolderMain$txt_from_dt']")).sendKeys("12/13/2018");
		driver.findElement(By.xpath("//input[@name='ctl00$PlaceHolderMain$txt_to_dt']")).sendKeys("12/15/2018");
		driver.findElement(By.xpath("//input[@name='ctl00$PlaceHolderMain$btnSubmit']")).click();
//		driver.findElement(By.className("#PlaceHolderMain_myBtn")).click();
		driver.findElement(By.id("PlaceHolderMain_myBtn")).click();
//		driver.findElement(By.xpath("//button[@onclick='showDialog();return false;']")).click();
//		driver.findElement(By.xpath("//button[@id='PlaceHolderMain_myBtn']")).click();
		*/
		driver.quit();
		}
		catch(Exception e) {
			driver.quit();
			e.printStackTrace();
		}
	}
	public static String decodePassword() {
		byte[] decoded = Base64.decodeBase64(prop.getProperty("password"));
		String decodedPassword = new String(decoded);
		System.out.println(decodedPassword);
		return decodedPassword;
	}
	public static void readPropertiesFile() {
		
		String propertiesFilename = "D:\\RAMA\\Selenium\\Common Jars\\mail.properties";
		try {
			
			input = new FileInputStream(propertiesFilename);
			prop.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {

		AttendanceStatus.getTime();
	}

}
