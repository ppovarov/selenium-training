package ru.stqa.training.selenium.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;
import ru.stqa.training.selenium.model.Country;
import ru.stqa.training.selenium.model.Zone;

import java.util.ArrayList;
import java.util.List;
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
        countries.stream()
                .forEach((c) -> c.setZones(getGeoZonesCountryZones(c)));

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
}


































