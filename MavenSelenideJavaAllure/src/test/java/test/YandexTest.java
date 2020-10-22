package test;

import com.codeborne.selenide.junit.TextReport;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class YandexTest {
    WebDriver driver;

    private static String yandexUsername = System.getProperty("yandex.username", "your username");
    private static String yandexPassword = System.getProperty("yandex.password", "your password");
    private static String yandexAccount = System.getProperty("yandex.accname", "your account name");

    @Rule
    public TestRule report = new TextReport().onFailedTest(true).onSucceededTest(true);

    @Before
    public void setUp() {
        String currentBrowser = System.getProperty("selenide.browser", "chrome");
        if ("firefox".equals(currentBrowser)) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if ("safari".equals(currentBrowser)) {
            driver = new SafariDriver();
        } else if ("edge".equals(currentBrowser)) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        } else if ("ie".equals(currentBrowser)) {
            WebDriverManager.iedriver().setup();
            driver = new InternetExplorerDriver();
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void yandex_auth() {
        open("https://passport.yandex.ru/auth");
        login();
        waitUntilPagesIsLoaded();
        $(By.className("user-account__name")).shouldHave(text(yandexAccount));
        element(By.className("personal-info-login")).shouldBe(visible);
    }

    protected static void waitUntilPagesIsLoaded() {
        $(byText("Паспорт")).waitUntil(disappears, 40000);
    }

    private static void login() {
        $(By.name("login")).val(yandexUsername).pressEnter();
        $(By.name("passwd")).val(yandexPassword).pressEnter();
    }

}
