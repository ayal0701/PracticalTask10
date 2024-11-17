package com.softserve.edu;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestSamples3 {
    @FindBy(css = "div.header_navigation-menu-right img.ubs-header-sing-in-img")
    private WebElement signInButton;
    @FindBy(css = ".ng-star-inserted > h1")
    private WebElement welcomeText;
    @FindBy(css = "h2:nth-child(2)")
    private WebElement signInDetailsText;
    @FindBy(css = "label:nth-child(1)")
    private WebElement emailLabel;
    @FindBy(id = "email")
    private WebElement emailInput;
    @FindBy(id = "password")
    private WebElement passwordInput;
    @FindBy(css = ".ubsStyle")
    private WebElement signInSubmitButton;
    @FindBy(css = ".mat-simple-snackbar > span")
    private WebElement result;
    @FindBy(css = ".alert-general-error")
    private WebElement errorMessage;
    @FindBy(xpath = "//*[@id=\"password-err-msg\"]/app-error/div")
    private WebElement errorPassword;
    @FindBy(xpath = "//*[@id=\"email-err-msg\"]/app-error/div")
    private WebElement errorEmail;
    @FindBy(css = "a.close-modal-window")
    private WebElement closeModalButton;  // To close any modal that might appear

    private static WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://www.greencity.cx.ua/#/greenCity");
        driver.manage().window().setSize(new Dimension(1264, 798));
    }

    @BeforeEach
    public void initPageElements() {
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void verifyTitle() {
        Assertions.assertEquals("GreenCity — Build Eco-Friendly Habits Today", driver.getTitle());
    }

    @ParameterizedTest
    @CsvSource({
            "samplestest@greencity.com, weyt3$Guew^",
            "anotheruser@greencity.com, anotherpassword"
    })
    public void signIn(String email, String password) {
        wait.until(ExpectedConditions.elementToBeClickable(signInButton)).click();

        wait.until(ExpectedConditions.visibilityOf(welcomeText));
        assertThat(welcomeText.getText(), is("Welcome back!"));
        assertThat(signInDetailsText.getText(), is("Please enter your details to sign in."));
        assertThat(emailLabel.getText(), is("Email"));

        emailInput.clear();
        emailInput.sendKeys(email); // Use parameterized email
        assertThat(emailInput.getAttribute("value"), is(email));

        passwordInput.clear();
        passwordInput.sendKeys(password); // Use parameterized password
        assertThat(passwordInput.getAttribute("value"), is(password));

        signInSubmitButton.click();
    }

    @ParameterizedTest
    @CsvSource({
            "Please check that your e-mail address is indicated correctly"
    })
    public void signInNotValid(String message) {
        signInButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
        emailInput.clear();
        emailInput.sendKeys("samplestesgreencity.com");

        passwordInput.clear();
        passwordInput.sendKeys("uT346^^^erw");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='email-err-msg']/app-error/div")));

        assertThat(errorEmail.getText(), is(message));
    }

    @Test
    public void verifyWindowClosesAfterEnteringPasswordAndClickingCloseButton() {
        signInButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("email")));
        emailInput.clear();
        emailInput.sendKeys("kate.yalanskaya@gmail.com");

        passwordInput.clear();
        passwordInput.sendKeys("pass__12344");

        closeWindow();

        WebElement signInButtonAfterClose = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Sign in")));
        assertThat(signInButtonAfterClose.isDisplayed(), is(true));
    }

    public void closeWindow() {
        WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".cross-btn")));
        closeButton.click();
    }


    @AfterAll
    public static void tearDown() {
        driver.quit();
    }
}
