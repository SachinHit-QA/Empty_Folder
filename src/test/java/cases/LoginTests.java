package cases;

import org.testng.Assert;
import org.testng.annotations.Test;

import Pages.HomePage;
import Pages.LoginPage;
import base.BaseTest;

public class LoginTests extends BaseTest {

    @Test
    public void validLoginTest() {
        LoginPage loginPage = new LoginPage(getDriver());
        HomePage homePage = loginPage.login("admin", "password123");
        
        Assert.assertEquals(homePage.getHeaderText(), "Dashboard");
    }
}