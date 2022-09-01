package listeners;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportDemo {
	public WebDriver gcdriver;
	public ExtentHtmlReporter htmlreporter;
	public ExtentReports extent;
	public ExtentTest test;
	
	@BeforeTest()
	public void setextent()
	{
		htmlreporter=new ExtentHtmlReporter("D:/EclipsProjects/testNG_listeners/test-output/myreport.html");
		htmlreporter.config().setDocumentTitle("Automation Report");
		htmlreporter.config().setReportName("Functional Report");
		htmlreporter.config().setTheme(Theme.DARK);
		extent=new ExtentReports();
		extent.attachReporter(htmlreporter);
		extent.setSystemInfo("hostname", "localhost");
		extent.setSystemInfo("OS","Windows");
		extent.setSystemInfo("Browser", "Chrome");
		extent.setSystemInfo("tester", "Viraj");
	}
	@AfterTest()
	public void endReport()
	{
		extent.flush();
	}
	@BeforeMethod()
	public void setup()
	{
		System.setProperty("webdriver.chrome.driver","C:\\chromedriver.exe");
		gcdriver=new ChromeDriver();
		gcdriver.get("https://www.nopcommerce.com/en/demo");
		gcdriver.manage().window().maximize();
		
	}
	@Test()
	public void nopCommerceTitleTest() {
		test=extent.createTest("nopCommerceTitleTest");//It will create new test in the report;
		String title=gcdriver.getTitle();
		System.out.println(title);
		Assert.assertEquals(title, "Store Demo - noCommerce");
		
	}
	@Test()
	public void nopCommerceLogoTest()
	{
		test=extent.createTest("nopCommerceLogoTest");
		boolean status=gcdriver.findElement(By.xpath("//img[@title='nopCommerce']")).isDisplayed();
		Assert.assertTrue(status);
	}
	@Test()
	public void nopCommerceLoginTest()
	{
		test=extent.createTest("nopCommerceLoginTest");
		Assert.assertTrue(true);
		
	}
	@AfterMethod()
	public void tearDown(ITestResult result) throws IOException
	{
		if(result.getStatus()==ITestResult.FAILURE) {
			test.log(Status.FAIL,"TEST CASE FAIl Is"+result.getName()); //to add name in extent report
			test.log( Status.FAIL,"TEST CASE FAIL Is"+result.getThrowable());// to add exception in report
					String screenshotPath=ExtentReportDemo.getScreenshot(gcdriver, result.getName());
					test.addScreenCaptureFromPath(screenshotPath);//adding screenshot

		}
		else if(result.getStatus()==ITestResult.SKIP)
		{
			test.log(Status.SKIP, "TEST CASE SKIpIs"+result.getName());
		}
		else if(result.getStatus()==ITestResult.SUCCESS)
		{
			test.log(Status.PASS, "TEST CASE PASS IS"+result.getName());
		}
		gcdriver.quit();
	}
	public static String getScreenshot(WebDriver gcdriver, String screenshotName) throws IOException
	{
		String dateName=new SimpleDateFormat("yyyymmddhhmmss").format(new Date());
		TakesScreenshot ts=(TakesScreenshot)gcdriver;
		File source=ts.getScreenshotAs(OutputType.FILE);
		String destination=System.getProperty("user.dir")+"/Screenshots/" +screenshotName +dateName+".png";
		File finaldestination=new File(destination);
		FileUtils.copyFile(source, finaldestination);
		return destination;
	}
	
	
}
