package controller;




import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;




public class WebController {
	static WebDriver driver;
	static WebElement element;
	static String OS;
	static String WebDriverPath;
	int statusCode;
	
	public WebController(String url)
	{
		chrome(url);
	}
	
	public void chrome(String url) 
	{
		File file = new File("");
		String currentPath = file.getAbsolutePath();
		//System.out.println(currentPath);
		String WebDriverId = "webdriver.chrome.driver";
		WebDriverPath = currentPath+"/lib/chromedriver_83.0.4103.39";
		System.setProperty(WebDriverId, WebDriverPath);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("headless");
		driver = new ChromeDriver(options);
		driver.get(url);
		String webTitle = driver.getTitle();
		if(webTitle.length() == 0 
				&& driver.getPageSource().compareTo("<html><head></head><body></body></html>") == 0)
		{
			System.out.println("[ERROR]"+Thread.currentThread().getStackTrace()[1].getFileName()+":"+Thread.currentThread().getStackTrace()[1].getLineNumber());
			assertTrue(false,"[ERROR] 페이지연결 실패");

		}
		else 
		{
			System.out.println();
			System.out.println();
			System.out.println("[SystemLog] WebTitle : [ "+webTitle+" ]");
			System.out.println();
			System.out.println();
			//System.out.println("[SystemLog]webSource:");
			//System.out.println(driver.getPageSource());
		}
		driver.quit();
	}
	
	public void setURL(String url)
	{
		driver.get(url);
	}
	
	public void checkConnection () 
	{
		String webTitle = driver.getTitle();
		
		if(webTitle.length() == 0 
				&& driver.getPageSource().compareTo("<html><head></head><body></body></html>") == 0)
		{
			System.out.println("[ERROR]"+Thread.currentThread().getStackTrace()[1].getFileName()+":"+Thread.currentThread().getStackTrace()[1].getLineNumber());
			assertTrue(false, "[ERROR] 페이지연결 실패");
		}
		else 
		{
			System.out.println("[SystemLog]Title:"+driver.getTitle());
			System.out.println("[SystemLog]webSource:");
			System.out.println(driver.getPageSource());
		}
	}
	
	public void quitDriver()
	{
		driver.quit();
	}
	
	public void firefox(String url)
	{
		FirefoxBinary firefoxBinary = new FirefoxBinary();
		firefoxBinary.addCommandLineOptions("--headless");
		File file = new File("");
		String currentPath = file.getAbsolutePath();
		System.setProperty("webdriver.gecko.driver", currentPath+"/lib/geckodriver");
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setBinary(firefoxBinary);
		
		FirefoxDriver driver = new FirefoxDriver(firefoxOptions);
		
		try {
			driver.get("https://www.naver.com");
			driver.manage().timeouts().implicitlyWait(4,
			          TimeUnit.SECONDS);
			
			String webTitle = driver.getTitle();
			if(webTitle.length() == 0 
					&& driver.getPageSource().compareTo("<html><head></head><body></body></html>") == 0)
			{
				System.out.println("[ERROR]"+Thread.currentThread().getStackTrace()[1].getFileName()+":"+Thread.currentThread().getStackTrace()[1].getLineNumber());
				assertTrue(false,"[ERROR] 페이지연결 실패");
			}
			else 
			{
				System.out.println("[SystemLog]Title:"+driver.getTitle());
				System.out.println("[SystemLog]webSource:");
				System.out.println(driver.getPageSource());
			}
		}finally {
			driver.quit();
		}
	}
	
	
	public String getOS()
	{
		String returnMsg = null;
		OS = System.getProperty("os.name").toLowerCase();
		System.out.println("[SystemLog] OS : "+OS);
		
		if(OS.indexOf("win") >= 0)
		{
			returnMsg = "/lib/chromedriver.exe";
		}
		else if(OS.indexOf("mac") >= 0 )
		{
			returnMsg = "/lib/chromedriver";
		}
		else if( OS.indexOf("nix") >= 0 || OS.indexOf("nux") >=0 || OS.indexOf("aix") >=0 )
		{
			returnMsg = "/lib/chromedriver_83.0.4103.14";
		}
		else if(OS.indexOf("su") >= 0)
		{
			assertTrue(false, "[ERROR] 지원하지 않는 운영체제" );
		}
		return returnMsg;
	}
	
}
