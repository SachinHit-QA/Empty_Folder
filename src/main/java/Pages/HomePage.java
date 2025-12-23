package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import MasterPage.PlantPage;

public class HomePage extends BasePage {

    @FindBy(xpath = "//h1[contains(text(),'Dashboard')]") 
    private WebElement lblHeader;
    
    @FindBy(id = "menu_plant") 
    private WebElement menuPlant;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public String getHeaderText() {
        return actions.getText(lblHeader);
    }

    public PlantPage navigateToPlantPage() {
        actions.click(menuPlant);
        return new PlantPage(driver);
    }
}