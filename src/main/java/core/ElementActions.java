package core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ElementActions {
    private WaitManager waitManager;

    public ElementActions(WebDriver driver) {
        this.waitManager = new WaitManager(driver);
    }

    public void click(WebElement element) {
        waitManager.waitForClickability(element);
        element.click();
        System.out.println("Clicked on element: " + element.toString());
    }

    public void sendKeys(WebElement element, String text) {
        waitManager.waitForVisibility(element);
        element.clear();
        element.sendKeys(text);
        System.out.println("Typed '" + text + "' into element.");
    }
    
    public String getText(WebElement element) {
        waitManager.waitForVisibility(element);
        return element.getText();
    }
}