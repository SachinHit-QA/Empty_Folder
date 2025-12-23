package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import core.ScreenshotUtil;

public class ExtentTestNGListener implements ITestListener {
    private static ExtentReports extent;
    // ThreadLocal ensures reports don't get mixed up in parallel execution
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        ExtentSparkReporter spark = new ExtentSparkReporter("target/ExtentReport.html");
        spark.config().setReportName("Automation Test Results");
        spark.config().setDocumentTitle("Test Report");
        
        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester", "QA Engineer");
        extent.setSystemInfo("Environment", "QA");
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Log the failure exception
        test.get().fail(result.getThrowable());

        // Capture Screenshot in Base64
        String base64Screenshot = ScreenshotUtil.captureScreenshotBase64();
        
        // Attach to report
        if (base64Screenshot != null) {
            test.get().fail("Screenshot of Failure",
                MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        }
        
        // Optional: Save file to local disk as well
        ScreenshotUtil.captureScreenshot(result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }
}