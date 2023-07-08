package BaseClass;

import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import reports.ExtentReport;
import utils.Log;
import utils.TestUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Properties;

/**
 * The Class TestBotBase.
 *
 * @author Pipul Pant
 */
public class TestBotBase {
	Log log= new Log(this.getClass());

	protected static ThreadLocal<AppiumDriver> driver = new ThreadLocal<AppiumDriver>();
	protected static ThreadLocal<Properties> props = new ThreadLocal<Properties>();
	protected static ThreadLocal<HashMap<String, String>> strings = new ThreadLocal<HashMap<String, String>>();
	protected static ThreadLocal<String> platform = new ThreadLocal<String>();
	protected static ThreadLocal<String> dateTime = new ThreadLocal<String>();
	protected static ThreadLocal<String> deviceName = new ThreadLocal<String>();
	private static AppiumDriverLocalService server;
	//Creating Utils object of TestUtils Class
	TestUtils utils = new TestUtils();

	public AppiumDriver getDriver() {
		return driver.get();
	}

	public void setDriver(AppiumDriver driver2) {
		driver.set(driver2);
	}

	public Properties getProps() {
		return props.get();
	}

	public void setProps(Properties props2) {
		props.set(props2);
	}

	public HashMap<String, String> getStrings() {
		return strings.get();
	}

	public void setStrings(HashMap<String, String> strings2) {
		strings.set(strings2);
	}

	public String getPlatform() {
		return platform.get();
	}

	public void setPlatform(String platform2) {
		platform.set(platform2);
	}

	public String getDateTime() {
		return dateTime.get();
	}

	public void setDateTime(String dateTime2) {
		dateTime.set(dateTime2);
	}

	public String getDeviceName() {
		return deviceName.get();
	}

	public void setDeviceName(String deviceName2) {
		deviceName.set(deviceName2);
	}

	public TestBotBase() {
		PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
	}

	/**
	 * Extracting the System/Laptop User Name.
	 */
	public static final String currentUserName = System.getProperty("user.name");

	//Implementing Screenrecord feature
	//Not working for honor play
//	@BeforeMethod
//	public void beforeMethod() {
//		((CanRecordScreen) getDriver()).startRecordingScreen();
//	}
//
//	//stop video capturing and create *.mp4 file
//	@AfterMethod
//	public synchronized void afterMethod(ITestResult result) throws Exception {
//		String media = ((CanRecordScreen) getDriver()).stopRecordingScreen();
//
//		Map <String, String> params = result.getTestContext().getCurrentXmlTest().getAllParameters();
//		String dirPath = "videos" + File.separator + params.get("platformName") + "_" + params.get("deviceName")
//		+ File.separator + getDateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName();
//
//		File videoDir = new File(dirPath);
//
//		synchronized(videoDir){
//			if(!videoDir.exists()) {
//				videoDir.mkdirs();
//			}
//		}
//		FileOutputStream stream = null;
//		try {
//			stream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4");
//			stream.write(Base64.decodeBase64(media));
//			stream.close();
//			utils.log().info("video path: " + videoDir + File.separator + result.getName() + ".mp4");
//		} catch (Exception e) {
//			utils.log().error("error during video capture" + e.toString());
//		} finally {
//			if(stream != null) {
//				stream.close();
//			}
//		}
//	}

	@BeforeSuite
	public void beforeSuite() throws Exception, Exception {
		ThreadContext.put("ROUTING_KEY", "ServerLogs");
		server = getAppiumService();
		if (!checkIfAppiumServerIsRunnning(4723)) {
			server.start();
			server.clearOutPutStreams();
			utils.log().info("Appium Server Started");
		} else {
			utils.log().info("Appium Server Already Running");
		}
	}

	/**
	 * This method checks whether Appium Server is Running or not
	 * @param port Port on which Appium Server is started
	 * @return true if server is running
	 * false if server is not running
	 * @throws Exception
	 */
	public boolean checkIfAppiumServerIsRunnning(int port) throws Exception {
		boolean isAppiumServerRunning = false;
		ServerSocket socket;
		try {
			socket = new ServerSocket(port);
			socket.close();
		} catch (IOException e) {
			System.out.println("1");
			isAppiumServerRunning = true;
		} finally {
			socket = null;
		}
		return isAppiumServerRunning;
	}

	/**
	 * This method is to Stop the Appium Server After the test execution
	 */
	@AfterSuite
	public void afterSuite() {
		server.stop();
		utils.log().info("Appium Server Stopped");
	}

	/**
	 * This method is used to get the Default Appium Server
	 * @return
	 */
	public AppiumDriverLocalService getAppiumServerDefault() {
		return AppiumDriverLocalService.buildDefaultService();
	}

	/**
	 * * Initializing the Appium Service
	 * in our System
	 * @return Appium Driver
	 */
	public AppiumDriverLocalService getAppiumService() {
		HashMap<String, String> environment = new HashMap<String, String>();
		environment.put("PATH", "/Users/" + currentUserName + "/Library/Android/sdk/build-tools/30.0.2/bin:/Users/" + currentUserName + "/Library/Android/sdk/build-tools/30.0.2:/Users/" + currentUserName + "/Library/Android/sdk/tools/bin:/Users/patron/Library/Android/sdk/tools:/Users/" + currentUserName + "/Library/Android/sdk/platform-tools/bin:/Users/" + currentUserName + "/Library/Android/sdk/platform-tools:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Library/Apple/usr/bin" + System.getenv("PATH"));
		environment.put("ANDROID_HOME", "/Users/" + currentUserName + "/Library/Android/sdk");
		return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
				.usingDriverExecutable(new File("/usr/local/bin/node"))
				.withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
				.usingPort(4723)
				.withArgument(GeneralServerFlag.SESSION_OVERRIDE)
				.withEnvironment(environment)
				.withLogFile(new File("ServerLogs/server.log")));
	}

	/**
	 * Setting Android or IOS Driver based on Test Suite
	 *
	 * @param emulator             : True or false based on the testing
	 * @param platformName         : Android or IOS platform
	 * @param udid                 : This is device ID on which testing is performed.
	 * @param deviceName           : Name of the device where testing is performed
	 * @param systemPort           : System Port of the device
	 * @param chromeDriverPort     : ChromeDriver port
	 * @param wdaLocalPort
	 * @param webkitDebugProxyPort
	 * @throws Exception
	 */
	@Parameters({"emulator", "platformName", "udid", "deviceName", "systemPort",
			"chromeDriverPort", "wdaLocalPort", "webkitDebugProxyPort"})
	@BeforeTest
	public void beforeTest(@Optional("androidOnly") String emulator, String platformName, String udid, String deviceName,
						   @Optional("androidOnly") String systemPort, @Optional("androidOnly") String chromeDriverPort,
						   @Optional("iOSOnly") String wdaLocalPort, @Optional("iOSOnly") String webkitDebugProxyPort) throws Exception {
		setDateTime(utils.dateTime());
		setPlatform(platformName);
		setDeviceName(deviceName);
		URL url;
		InputStream inputStream = null;
		InputStream stringsis = null;
		Properties props = new Properties();
		AppiumDriver driver;

		String strFile = "Logs" + File.separator + platformName + "_" + deviceName;
		File logFile = new File(strFile);
		if (!logFile.exists()) {
			logFile.mkdirs();
		}
		//route logs to separate file for each thread
		ThreadContext.put("ROUTING_KEY", strFile);
		utils.log().info("Log Path: " + strFile);

		try {
			props = new Properties();
			String propFileName = "config.properties";
			String xmlFileName = "strings/strings.xml";

			utils.log().info("Load:: " + propFileName);
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			props.load(inputStream);
			setProps(props);

			utils.log().info("Load:: " + xmlFileName);
			stringsis = getClass().getClassLoader().getResourceAsStream(xmlFileName);
			setStrings(utils.parseStringXML(stringsis));

			DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
			desiredCapabilities.setCapability("platformName", platformName);
			desiredCapabilities.setCapability("deviceName", deviceName);
			desiredCapabilities.setCapability("udid", udid);
			url = new URL(props.getProperty("appiumURL"));

			switch (platformName) {
				case "Android":
					desiredCapabilities.setCapability("automationName", props.getProperty("androidAutomationName"));
					desiredCapabilities.setCapability("appPackage", props.getProperty("androidAppPackage"));
					desiredCapabilities.setCapability("appActivity", props.getProperty("androidAppActivity"));
					if (emulator.equalsIgnoreCase("true")) {
						desiredCapabilities.setCapability("avd", deviceName);
						desiredCapabilities.setCapability("avdLaunchTimeout", 120000);
					}
					desiredCapabilities.setCapability("systemPort", systemPort);
					desiredCapabilities.setCapability("chromeDriverPort", chromeDriverPort);
					String androidAppUrl = getClass().getResource(props.getProperty("androidAppLocation")).getFile();
					utils.log().info("Application URL::" + androidAppUrl);
					desiredCapabilities.setCapability("app", androidAppUrl);

					driver = new AndroidDriver(url, desiredCapabilities);
					break;
				case "iOS":
					desiredCapabilities.setCapability("automationName", props.getProperty("iOSAutomationName"));
					String iOSAppUrl = getClass().getResource(props.getProperty("iOSAppLocation")).getFile();
					utils.log().info("Application URL::" + iOSAppUrl);
					desiredCapabilities.setCapability("bundleId", props.getProperty("iOSBundleId"));
					desiredCapabilities.setCapability("wdaLocalPort", wdaLocalPort);
					desiredCapabilities.setCapability("webkitDebugProxyPort", webkitDebugProxyPort);
					desiredCapabilities.setCapability("app", iOSAppUrl);

					driver = new IOSDriver(url, desiredCapabilities);
					break;
				default:
					throw new Exception("Invalid Platform!!! - " + platformName);
			}
			setDriver(driver);
			utils.log().info("Driver Initialized Successfully:: " + driver);
		} catch (Exception e) {
			utils.log().fatal("Driver Initialization Failed.Aborting Test!!!\n" + e.toString());
			throw e;
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (stringsis != null) {
				stringsis.close();
			}
		}
	}

	/**
	 * This method check for the specific web-element element visibility in Mobile
	 * @param e any element xpath for which visibility has to be checked
	 */
	public void waitForVisibility(MobileElement e) {
		WebDriverWait wait = new WebDriverWait(getDriver(), TestUtils.WAIT);
		wait.until(ExpectedConditions.visibilityOf(e));
	}

	/**
	 * Similar method as above but we are using fluentWait for the particular Mobile element
	 * Useful for those element which are not working using above commmand.
	 * @param e
	 */
	public void waitForVisibility(WebElement e) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
				.withTimeout(Duration.ofSeconds(30))
				.pollingEvery(Duration.ofSeconds(5))
				.ignoring(NoSuchElementException.class);

		wait.until(ExpectedConditions.visibilityOf(e));
	}

	/**
	 * Method Application:
	 * This Method clears the value of the particular mobile web element
	 * @param e xpath for the particular mobile element
	 */
	public void clear(MobileElement e) {
		waitForVisibility(e);
		e.clear();
	}

	/**
	 * This method click on the particular mobile element
	 * @param e xpath for the particular mobile element
	 */
	public void click(MobileElement e) {
		waitForVisibility(e);
		e.click();
	}

	/**
	 * This method click on the particular mobile element
	 * with logging for the click element
	 * @param e xpath for the particular mobile element
	 */
	public void click(MobileElement e, String msg) {
		waitForVisibility(e);
		utils.log().info(msg);
		ExtentReport.getTest().log(Status.INFO, msg);
		e.click();
	}

	/**
	 * This method send keys to the inputs box on the particular mobile element
	 * @param e xpath for the particular mobile element
	 */
	public void sendKeys(MobileElement e, String txt) {
		waitForVisibility(e);
		e.sendKeys(txt);
	}

	/**
	 * This method send keys to the inputs box on the particular mobile element
	 * with logging for the click element
	 * @param e xpath for the particular mobile element
	 */
	public void sendKeys(MobileElement e, String txt, String msg) {
		waitForVisibility(e);
		utils.log().info(msg);
		ExtentReport.getTest().log(Status.INFO, msg);
		e.sendKeys(txt);
	}

	/**
	 * This method get the Attribute of the particular web element
	 * @param e xpath for the particular mobile element, attribute value
	 */
	public String getAttribute(MobileElement e, String attribute) {
		waitForVisibility(e);
		return e.getAttribute(attribute);
	}

	/**
	 * This method get the text of the particular web element
	 * @param e xpath for the particular mobile element, attribute value
	 */
	public String getText(MobileElement e, String msg) {
		String txt = null;
		switch (getPlatform()) {
			case "Android":
				txt = getAttribute(e, "text");
				break;
			case "iOS":
				txt = getAttribute(e, "label");
				break;
		}
		utils.log().info(msg + txt);
		ExtentReport.getTest().log(Status.INFO, msg + txt);
		return txt;
	}

	/**
	 * This method is used to close the app
	 * @param e xpath for the particular mobile element, attribute value
	 */
	public void closeApp() {
		((InteractsWithApps) getDriver()).closeApp();
	}

	/**
	 * This method is used to launch the app
	 * @param e xpath for the particular mobile element, attribute value
	 */
	public void launchApp() {
		((InteractsWithApps) getDriver()).launchApp();
	}

	/**
	 * Method Application:
	 * This method is used to scroll to the particular mobile element in android
	 */
	public MobileElement scrollToElement() {
		return (MobileElement) ((FindsByAndroidUIAutomator) getDriver()).findElementByAndroidUIAutomator(
				"new UiScrollable(new UiSelector()" + ".scrollable(true)).scrollIntoView("
						+ "new UiSelector().description(\"test-Price\"));");
	}

	/**
	 * Method Application:
	 * This method is used to scroll to the particular mobile element in IOS
	 */
	public void iOSScrollToElement() {
		RemoteWebElement element = (RemoteWebElement) getDriver().findElement(By.name("test-ADD TO CART"));
		String elementID = element.getId();
		HashMap<String, String> scrollObject = new HashMap<String, String>();
		scrollObject.put("element", elementID);
//	  scrollObject.put("direction", "down");
//	  scrollObject.put("predicateString", "label == 'ADD TO CART'");
//	  scrollObject.put("name", "test-ADD TO CART");
		scrollObject.put("toVisible", "sdfnjksdnfkld");
		getDriver().executeScript("mobile:scroll", scrollObject);
	}

	/**
	 * This runs after the test execution is completed.
	 * This quits the initialized driver
	 */
	@AfterTest
	public void afterTest() {
		getDriver().quit();
	}
}