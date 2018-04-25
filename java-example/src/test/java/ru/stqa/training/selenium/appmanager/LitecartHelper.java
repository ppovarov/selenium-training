package ru.stqa.training.selenium.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.testng.asserts.SoftAssert;
import ru.stqa.training.selenium.model.Account;
import ru.stqa.training.selenium.model.Country;
import ru.stqa.training.selenium.model.Product;
import ru.stqa.training.selenium.model.Zone;

import java.awt.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


public class LitecartHelper extends HelperBase {

    public LitecartHelper(WebDriver wd) {
        super(wd);
    }

    public void loginAdmin() {
        wd.get("http://localhost/litecart/admin/login.php");

        type(By.name("username"), "admin");
        type(By.name("password"), "admin");
        click(By.name("login"));
    }

    public void openMainPage() {
        wd.get("http://localhost/litecart/");
    }

    public void openCountriesPage() {
        wd.get("http://localhost/litecart/admin/?app=countries&doc=countries");
    }

    public void openEditCountryPage(String code) {
        wd.get(String.format("http://localhost/litecart/admin/?app=countries&doc=edit_country&country_code=%s", code));
    }

    public void openGeoZonesPage() {
        wd.get("http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones");
    }

    public void openEditGeoZonePage(int id) {
        wd.get(String.format("http://localhost/litecart/admin/?app=geo_zones&doc=edit_geo_zone&geo_zone_id=%s", id));
    }

    public void openCreateAccountPage() {
        wd.get("http://localhost/litecart/en/create_account");
    }


    public void clickMenuItems() {
        SoftAssert softAssert = new SoftAssert();
        List<String> items = wd.findElements(By.xpath("//div[@id='box-apps-menu-wrapper']//span[@class='name']"))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());

        for (String item : items) {
            click(By.xpath(String.format("//span[@class='name' and text()='%s']", item)));
            softAssert.assertEquals(isElementPresent(By.tagName("h1")), true, item);

            List<String> subItems = wd.findElements(By.xpath("//li[@class='selected']/ul//span[@class='name']"))
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());

            for (String subItem : subItems) {
                click(By.xpath(String.format("//span[@class='name' and text()='%s']", subItem)));
                softAssert.assertEquals(isElementPresent(By.tagName("h1")), true, subItem);
            }
        }
        softAssert.assertAll();
    }

    public String getPageHeading() {
        return wd.findElement(By.tagName("h1")).getText();
    }

    public void checkStickers() {
        SoftAssert softAssert = new SoftAssert();
        List<WebElement> elements = wd.findElements(By.cssSelector("li.product"));
        for (WebElement element : elements) {
            String name = element.findElement(By.cssSelector("div.name")).getText();
            softAssert.assertEquals(element.findElements(By.cssSelector("div.sticker")).size(), 1, name);
        }
        softAssert.assertAll();
    }

    public List<Country> getCountries() {
        openCountriesPage();
        List<Country> countries = new ArrayList<Country>();

        List<WebElement> rows = wd.findElements(By.cssSelector("form[name=countries_form] tr.row"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            countries.add(new Country()
                    .withID(Integer.parseInt(cells.get(2).getText()))
                    .withCode(cells.get(3).getText())
                    .withName(cells.get(4).getText())
                    .withZonesCount(Integer.parseInt(cells.get(5).getText())));
        }
        countries.stream()
                .filter((c) -> c.getZonesCount() > 0)
                .forEach((c) -> c.setZones(getCountryZones(c)));

        return countries;
    }

    public List<Zone> getCountryZones(Country country) {
        openEditCountryPage(country.getCode());
        List<Zone> zones = new ArrayList<Zone>();

        List<WebElement> rows = wd.findElements(By.xpath("//table[@id='table-zones']//tr[.//input[@type='hidden']]"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            zones.add(new Zone()
                    .withID(Integer.parseInt(cells.get(0).getText()))
                    .withCode(cells.get(1).getText())
                    .withName(cells.get(2).getText()));
        }
        return zones;
    }

    public List<Country> getGeoZonesCountries() {
        openGeoZonesPage();
        List<Country> countries = new ArrayList<Country>();

        List<WebElement> rows = wd.findElements(By.cssSelector("form[name=geo_zones_form] tr.row"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            countries.add(new Country()
                    .withID(Integer.parseInt(cells.get(1).getText()))
                    .withName(cells.get(2).getText())
                    .withZonesCount(Integer.parseInt(cells.get(3).getText())));
        }
        countries.forEach((c) -> c.setZones(getGeoZonesCountryZones(c)));

        return countries;
    }

    public List<Zone> getGeoZonesCountryZones(Country country) {
        openEditGeoZonePage(country.getID());
        List<Zone> zones = new ArrayList<Zone>();

        List<WebElement> rows = wd.findElements(By.xpath("//table[@id='table-zones']//tr[.//input[@type='hidden']]"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));

            int id = Integer.parseInt(cells.get(0).getText());
            WebElement el = cells.get(2).findElement(By.xpath(".//option[@selected]"));
            String name = el.getText();

            zones.add(new Zone()
                    .withID(Integer.parseInt(cells.get(0).getText()))
                    .withName(cells.get(2).findElement(By.xpath(".//option[@selected]")).getText()));
        }
        return zones;
    }

    public void openFirstCampaignsProduct() {
        wd.findElement(By.cssSelector("div#box-campaigns li.product")).click();
    }

    public Product getFirstCampaignsProduct() {
        WebElement prod = wd.findElement(By.cssSelector("div#box-campaigns li.product"));

        String name = prod.findElement(By.cssSelector("div.name")).getText();

        WebElement regularPrice = prod.findElement(By.cssSelector(".regular-price"));
        String price = regularPrice.getText();
        Color priceColor = org.openqa.selenium.support.Color.fromString(regularPrice.getCssValue("color")).getColor();
        String priceStyle = regularPrice.getTagName();
        String priceSize = regularPrice.getCssValue("font-size");

        WebElement campaignPrice = prod.findElement(By.cssSelector(".campaign-price"));
        String discountPrice = campaignPrice.getText();
        Color discountPriceColor = org.openqa.selenium.support.Color.fromString(campaignPrice.getCssValue("color")).getColor();
        String discountPriceStyle = campaignPrice.getTagName();
        String discountPriceSize = campaignPrice.getCssValue("font-size");

        String link = prod.findElement(By.cssSelector(".link")).getAttribute("href");

        return new Product()
                .withName(name)
                .withPrice(price).withPriceColor(priceColor).withPriceStyle(priceStyle).withPriceSize(priceSize)
                .withDiscountPrice(discountPrice).withDiscountPriceColor(discountPriceColor).withDiscountPriceStyle(discountPriceStyle).withDiscountPriceSize(discountPriceSize)
                .withLink(link);
    }

    public Product getProductDetails(String link) {
        wd.get(link);

        String name = wd.findElement(By.cssSelector("[itemprop=name]")).getText();

        WebElement regularPrice = wd.findElement(By.cssSelector(".regular-price"));
        String price = regularPrice.getText();
        Color priceColor = org.openqa.selenium.support.Color.fromString(regularPrice.getCssValue("color")).getColor();
        String priceStyle = regularPrice.getTagName();
        String priceSize = regularPrice.getCssValue("font-size");

        WebElement campaignPrice = wd.findElement(By.cssSelector(".campaign-price"));
        String discountPrice = campaignPrice.getText();
        Color discountPriceColor = org.openqa.selenium.support.Color.fromString(campaignPrice.getCssValue("color")).getColor();
        String discountPriceStyle = campaignPrice.getTagName();
        String discountPriceSize = campaignPrice.getCssValue("font-size");

        return new Product()
                .withName(name)
                .withPrice(price).withPriceColor(priceColor).withPriceStyle(priceStyle).withPriceSize(priceSize)
                .withDiscountPrice(discountPrice).withDiscountPriceColor(discountPriceColor).withDiscountPriceStyle(discountPriceStyle).withDiscountPriceSize(discountPriceSize);
    }

    public void createAccount(Account account) {
        openCreateAccountPage();

        wd.findElement(By.xpath("//input[@name='firstname']")).sendKeys("fname");
        wd.findElement(By.xpath("//input[@name='lastname']")).sendKeys("lname");
        wd.findElement(By.xpath("//input[@name='address1']")).sendKeys("123 Main st");
        wd.findElement(By.xpath("//input[@name='postcode']")).sendKeys("10007");
        wd.findElement(By.xpath("//input[@name='city']")).sendKeys("New York");

        new Select(wd.findElement(By.xpath("//select[@name='country_code']"))).selectByValue("US");

        wait = new FluentWait<WebDriver>(wd)
                .withTimeout(Duration.ofMillis(100))
                .pollingEvery(Duration.ofMillis(10))
                .ignoring(NoSuchElementException.class);

        WebElement select = wait.until(driver -> driver.findElement(By.xpath("//select[@name='zone_code']")));
        new Select(select).selectByValue("NY");

        wd.findElement(By.xpath("//input[@name='email']")).sendKeys(account.getEmail());
        wd.findElement(By.xpath("//input[@name='phone']")).sendKeys("+12223334455");
        wd.findElement(By.xpath("//input[@name='password']")).sendKeys(account.getPassword());
        wd.findElement(By.xpath("//input[@name='confirmed_password']")).sendKeys(account.getPassword());

        wd.findElement(By.xpath("//button[@name='create_account']")).click();
    }

    public boolean login(Account account) {
        openMainPage();
        wd.findElement(By.xpath("//input[@name='email']")).sendKeys(account.getEmail());
        wd.findElement(By.xpath("//input[@name='password']")).sendKeys(account.getPassword());
        wd.findElement(By.xpath("//button[@name='login']")).click();
        return isElementPresent(By.xpath("//div[@class='notice success']"));
    }

    public void logout() {
        wd.findElement(By.xpath("//a[text()='Logout']")).click();
    }
}


































