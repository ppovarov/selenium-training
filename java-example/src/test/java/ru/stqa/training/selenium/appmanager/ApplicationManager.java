package ru.stqa.training.selenium.appmanager;

import com.google.common.io.Files;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import ru.stqa.training.selenium.pages.CartPage;
import ru.stqa.training.selenium.pages.MainPage;
import ru.stqa.training.selenium.pages.ProductPage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {

    private final Properties properties;
    public WebDriver wd;
    public BrowserMobProxy proxy;
    private String browser;

    private LitecartHelper litecartHelper;

    private MainPage mainPage;
    private ProductPage productPage;
    private CartPage cartPage;


    public ApplicationManager(String browser) {
        this.browser = browser;
        properties = new Properties();
    }

    public void init() throws IOException {
        String target = System.getProperty("target", "local");
        properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));

        if ("".equals(properties.getProperty("selenium.server"))) {
            switch (browser) {
                case BrowserType.FIREFOX:
                    /*
                    FirefoxOptions options = new FirefoxOptions()
                            .setLegacy(true)
                            .setBinary("C:/Program Files/Mozilla Firefox ESR45/firefox.exe");
                    */
                    FirefoxOptions ffOoptions = new FirefoxOptions()
                            .setLegacy(false)
                            .setBinary("C:/Program Files/Mozilla Firefox/firefox.exe");
                    ;
                    wd = new FirefoxDriver(ffOoptions);
                    break;

                case BrowserType.CHROME:
                    ChromeOptions chrOptions = new ChromeOptions();

                    // Enable Performance Logging, see http://chromedriver.chromium.org/logging/performance-log
                    /*
                    LoggingPreferences logPrefs = new LoggingPreferences();
                    logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
                    chrOptions.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
                    */

                    // Using with Proxy, see https://github.com/lightbody/browsermob-proxy
                    proxy = new BrowserMobProxyServer();
                    proxy.start(0);
                    Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
                    chrOptions.setCapability(CapabilityType.PROXY, seleniumProxy);

                    //wd = new ChromeDriver(chrOptions);
                    wd = new EventFiringWebDriver(new ChromeDriver(chrOptions));
                    ((EventFiringWebDriver) wd).register(new MyListener());
                    break;

                case BrowserType.IE:
                    wd = new InternetExplorerDriver();
                    break;
            }
        } else {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setBrowserName(browser);
            capabilities.setPlatform(Platform.fromString(System.getProperty("platform", "windows")));
            wd = new RemoteWebDriver(new URL(properties.getProperty("selenium.server")), capabilities);
        }

        wd.manage().timeouts().implicitlyWait(Integer.parseInt(property("webdriver.wait")), TimeUnit.SECONDS);
        wd.get(property("web.baseURL"));

        litecartHelper = new LitecartHelper(wd);

        mainPage = new MainPage(wd);
        productPage = new ProductPage(wd);
        cartPage = new CartPage(wd);

    }

    public void stop() {
        wd.quit();
    }

    public LitecartHelper litecart() {
        return litecartHelper;
    }

    public MainPage mainPage() {return mainPage;}
    public ProductPage productPage() {return productPage;}
    public CartPage cartPage() {return cartPage;}

    public String property(String key) {
        return properties.getProperty(key);
    }

    public static class MyListener extends AbstractWebDriverEventListener {
        @Override
        public void beforeFindBy(By by, WebElement element, WebDriver driver) {
            System.out.println(by);
        }

        @Override
        public void afterFindBy(By by, WebElement element, WebDriver driver) {
            System.out.println(by + " found");
        }

        @Override
        public void onException(Throwable throwable, WebDriver driver) {
            System.out.println(throwable);
            File tempFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File screenshot = new File(String.format("screenshots/screen-%s.png", System.currentTimeMillis()));
            try {
                Files.copy(tempFile, screenshot);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(screenshot);
        }
    }


}

