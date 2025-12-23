package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    @FindBy(id = "username") private WebElement txtUsername;
    @FindBy(id = "password") private WebElement txtPassword;
    @FindBy(id = "loginBtn") private WebElement btnLogin;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public HomePage login(String user, String pass) {
        actions.sendKeys(txtUsername, user);
        actions.sendKeys(txtPassword, pass);
        actions.click(btnLogin);
        return new HomePage(driver);
    }
}