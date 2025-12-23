package cases;

import org.testng.Assert;
import org.testng.annotations.Test;

import MasterPage.PlantPage;
import Pages.HomePage;
import Pages.LoginPage;
import base.BaseTest;

public class PlantTests extends BaseTest {

    @Test
    public void verifyPlantPageNavigation() {
        // Chaining actions
        LoginPage loginPage = new LoginPage(getDriver());
        HomePage homePage = loginPage.login("admin", "password123");
        
        PlantPage plantPage = homePage.navigateToPlantPage();
        
        Assert.assertTrue(plantPage.isCreateButtonDisplayed(), "Create button should be visible");
    }
}