package ru.stqa.training.selenium.appmanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {

    private final Properties properties;
    WebDriver wd;
    private String browser;
    private LitecartHelper litecartHelper;


    public ApplicationManager(String browser) {
        this.browser = browser;
        properties = new Properties();
    }

    public void init() throws IOException {
        properties.load(new FileReader(new File("src/test/resources/local.properties")));

        if (browser.equals(BrowserType.FIREFOX)) {
            /*
            FirefoxOptions options = new FirefoxOptions()
                    .setLegacy(true)
                    .setBinary("C:/Program Files/Mozilla Firefox ESR45/firefox.exe");
            */
            FirefoxOptions options = new FirefoxOptions()
                    .setLegacy(false)
                    .setBinary("C:/Program Files/Mozilla Firefox/firefox.exe");;
            wd = new FirefoxDriver(options);
        } else if (browser.equals(BrowserType.CHROME)) {
            wd = new ChromeDriver();
        } else if (browser.equals(BrowserType.IE)) {
            wd = new InternetExplorerDriver();
        }

        wd.manage().timeouts().implicitlyWait(Integer.parseInt(property("webdriver.wait")), TimeUnit.SECONDS);
        wd.get(property("web.baseURL"));
        litecartHelper = new LitecartHelper(wd);
    }

    public void stop() {
        wd.quit();
    }

    public LitecartHelper litecart() {
        return litecartHelper;
    }

    public String property(String key) {
        return properties.getProperty(key);
    }

}

