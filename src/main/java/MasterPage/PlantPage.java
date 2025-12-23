package MasterPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import Pages.BasePage;

public class PlantPage extends BasePage {

    @FindBy(id = "createPlant") private WebElement btnCreate;

    public PlantPage(WebDriver driver) {
        super(driver);
    }

    public boolean isCreateButtonDisplayed() {
        return btnCreate.isDisplayed();
    }
}