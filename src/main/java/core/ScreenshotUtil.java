package core;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {

    /**
     * Captures a screenshot and returns the file path.
     * @param methodName The name of the test method (used for filename).
     * @return String absolute path of the screenshot.
     */
    public static String captureScreenshot(String methodName) {
        WebDriver driver = DriverFactory.getDriver();
        
        if (driver == null) {
            System.out.println("Driver is null, cannot take screenshot.");
            return null;
        }

        // Generate timestamp for unique filename
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        
        // Take screenshot
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        
        // Define destination
        String destinationPath = System.getProperty("user.dir") + "/screenshots/" + methodName + "_" + dateName + ".png";
        File finalDestination = new File(destinationPath);
        
        try {
            FileUtils.copyFile(source, finalDestination);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return destinationPath;
    }
    
    /**
     * Captures screenshot as Base64 (Preferred for Extent Reports to avoid broken paths).
     */
    public static String captureScreenshotBase64() {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) return null;
        
        TakesScreenshot ts = (TakesScreenshot) driver;
        return ts.getScreenshotAs(OutputType.BASE64);
    }
}