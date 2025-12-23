package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import core.ElementActions;

public class BasePage {
    protected WebDriver driver;
    protected ElementActions actions;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.actions = new ElementActions(driver);
        PageFactory.initElements(driver, this);
    }
}