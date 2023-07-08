package listeners;

import BaseClass.TestBotBase;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.*;
import reports.ExtentReport;
import utils.TestUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TestListener implements ITestListener, ISuiteListener, IInvokedMethodListener,IReporter {
	TestUtils utils = new TestUtils();
	
	public void onTestFailure(ITestResult result) {
		if(result.getThrowable() != null) {
			  StringWriter sw = new StringWriter();
			  PrintWriter pw = new PrintWriter(sw);
			  result.getThrowable().printStackTrace(pw);
			  utils.log().error(sw.toString());
		}
		
		TestBotBase base = new TestBotBase();
		File file = base.getDriver().getScreenshotAs(OutputType.FILE);
		
		byte[] encoded = null;
		try {
			encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Map <String, String> params = new HashMap<String, String>();
		params = result.getTestContext().getCurrentXmlTest().getAllParameters();
		
		String imagePath = "Screenshots" + File.separator + params.get("platformName") 
		+ "_" + params.get("deviceName") + File.separator + base.getDateTime() + File.separator 
		+ result.getTestClass().getRealClass().getSimpleName() + File.separator + result.getName() + ".png";
		
		String completeImagePath = System.getProperty("user.dir") + File.separator + imagePath;
		
		try {
			FileUtils.copyFile(file, new File(imagePath));
			Reporter.log("This is the sample screenshot");
			Reporter.log("<a href='"+ completeImagePath + "'> <img src='"+ completeImagePath + "' height='400' width='400'/> </a>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ExtentReport.getTest().fail("Test Failed",
				MediaEntityBuilder.createScreenCaptureFromPath(completeImagePath).build());
		ExtentReport.getTest().fail("Test Failed",
				MediaEntityBuilder.createScreenCaptureFromBase64String(new String(encoded, StandardCharsets.US_ASCII)).build());
		ExtentReport.getTest().fail(result.getThrowable());
	}

	@Override
	public void onTestStart(ITestResult result) {
		TestBotBase base = new TestBotBase();
		ExtentReport.startTest(result.getName(), result.getMethod().getDescription())
		.assignCategory(base.getPlatform() + "_" + base.getDeviceName())
		.assignAuthor("Pipul Pant");
		System.out.println("I am in onTestStart method " +  getTestMethodName(result) + " start");

	}

	@Override
	public void onTestSuccess(ITestResult result) {
		ExtentReport.getTest().log(Status.PASS, "Test Passed");
		System.out.println("I am in onTestSuccess method " +  getTestMethodName(result) + " succeed");
		printTestResults(result);



	}

	@Override
	public void onTestSkipped(ITestResult result) {
		ExtentReport.getTest().log(Status.SKIP, "I am in onTestSkipped method "+  getTestMethodName(result) + " skipped");
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		Reporter.log("About to begin executing Test " + context.getName(), true);


	}

	@Override
	public void onFinish(ITestContext context) {
		ExtentReport.getReporter().flush();

	}
	/* (non-Javadoc)
	 * @see org.testng.ISuiteListener#onFinish(org.testng.ISuite)
	 */
	@Override
	public void onFinish(ISuite arg0) {
		// TODO Auto-generated method stub
		Reporter.log("About to end executing Suite " + arg0.getName(), true);


	}
	/* (non-Javadoc)
	 * @see org.testng.ISuiteListener#onStart(org.testng.ISuite)
	 */
	@Override
	public void onStart(ISuite arg0) {
		// TODO Auto-generated method stub
		Reporter.log("About to begin executing Suite " + arg0.getName(), true);

	}
	/**
	 * Gets the test method name.
	 *
	 * @param iTestResult the i test result
	 * @return the test method name
	 */
	private static String getTestMethodName(ITestResult iTestResult) {
		return iTestResult.getMethod().getConstructorOrMethod().getName();
	}
	private String returnMethodName(ITestNGMethod method) {

		return method.getRealClass().getSimpleName() + "." + method.getMethodName();

	}
	// This belongs to IInvokedMethodListener and will execute after every method including @Before @After @Test

	/* (non-Javadoc)
	 * @see org.testng.IInvokedMethodListener#afterInvocation(org.testng.IInvokedMethod, org.testng.ITestResult)
	 */
	public void afterInvocation(IInvokedMethod arg0, ITestResult arg1) {

		String textMsg = "Completed executing following method : " + returnMethodName(arg0.getTestMethod());

		Reporter.log(textMsg, true);
		System.out.println("=======================|   Test Completed     |=========================");

	}
	// This belongs to IInvokedMethodListener and will execute before every method including @Before @After @Test

	/* (non-Javadoc)
	 * @see org.testng.IInvokedMethodListener#beforeInvocation(org.testng.IInvokedMethod, org.testng.ITestResult)
	 */
	public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {

		String textMsg = "About to begin executing following method : " + returnMethodName(arg0.getTestMethod());

		Reporter.log(textMsg, true);

	}
	/**
	 * Prints the test results.
	 *
	 * @param result the result
	 */
	private void printTestResults(ITestResult result) {

		Reporter.log("Test Method resides in " + result.getTestClass().getName(), true);

		if (result.getParameters().length != 0) {

			String params = null;

			for (Object parameter : result.getParameters()) {

				params += parameter.toString() + ",";

			}

			Reporter.log("Test Method had the following parameters : " + params, true);

		}

		String status = null;

		switch (result.getStatus()) {

			case ITestResult.SUCCESS:

				status = "Pass";

				break;

			case ITestResult.FAILURE:

				status = "Failed";

				break;

			case ITestResult.SKIP:

				status = "Skipped";

		}

		Reporter.log("Test Status: " + status, true);

	}

}
