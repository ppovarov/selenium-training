package ru.stqa.training.selenium.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.testng.asserts.SoftAssert;
import ru.stqa.training.selenium.model.Account;
import ru.stqa.training.selenium.model.Country;
import ru.stqa.training.selenium.model.Product;
import ru.stqa.training.selenium.model.Zone;

import java.awt.*;
import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
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
        if (code.equals("")) {
            wd.get("http://localhost/litecart/admin/?app=countries&doc=edit_country");
        } else {
            wd.get(String.format("http://localhost/litecart/admin/?app=countries&doc=edit_country&country_code=%s", code));
        }
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

    public void openCatalogPage() {
        wd.get("http://localhost/litecart/admin/?app=catalog&doc=catalog&category_id=1");
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
                .withTimeout(Duration.ofMillis(500))
                .pollingEvery(Duration.ofMillis(50))
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

    public void openAddNewProductPage() {
        click(By.xpath(String.format("//span[@class='name' and text()='%s']", "Catalog")));
        click(By.xpath(String.format("//a[@class='button' and contains(text(),'%s')]", "Add New Product")));
    }

    public void addNewProduct(Product product, long now) {
        openAddNewProductPage();

        //GENERAL tab
        WebElement generalTab = wd.findElement(By.xpath("//div[@id='tab-general']"));
        //Status
        generalTab.findElement(By.xpath(".//input[@name='status' and @value='1']")).click();
        //Name
        generalTab.findElement(By.xpath(".//input[@name='name[en]']")).sendKeys(product.getName());
        //Code
        generalTab.findElement(By.xpath(".//input[@name='code']")).sendKeys(String.format("code%s", now));
        //Categories
        By categoriesLocator = By.xpath(String.format(".//input[@data-name='%s' and @value='1']", "Rubber Ducks"));
        generalTab.findElement(categoriesLocator).click();
        //Default Category
        new Select(generalTab.findElement(By.xpath(".//select[@name='default_category_id']"))).selectByVisibleText("Rubber Ducks");
        //Product Groups
        By productGroupsLocator = By.xpath(String.format(".//td[text()='%s']/preceding-sibling::td//input[@name='product_groups[]']", "Unisex"));
        generalTab.findElement(productGroupsLocator).click();
        //Quantity
        generalTab.findElement(By.xpath(".//input[@name='quantity']")).clear();
        generalTab.findElement(By.xpath(".//input[@name='quantity']")).sendKeys("100");
        //Upload Images
        By uploadImagesLocator = By.xpath("//div[@id='tab-general']//input[@name='new_images[]']");
        File image = new File("src/test/resources/evil_duck.jpg");
        attach(uploadImagesLocator, image);
        //Date Valid From
        generalTab.findElement(By.xpath("//input[@name='date_valid_from']")).sendKeys("12312017");
        generalTab.findElement(By.xpath("//input[@name='date_valid_to']")).sendKeys("06302018");

        //INFORMATION tab
        wd.findElement(By.xpath("//a[text()='Information']")).click();
        WebElement informationTab = wd.findElement(By.xpath("//div[@id='tab-information']"));
        //Manufacturer
        new Select(informationTab.findElement(By.xpath("//select[@name='manufacturer_id']"))).selectByVisibleText("ACME Corp.");
        //Keywords
        informationTab.findElement(By.xpath(".//input[@name='keywords']")).sendKeys("Keywords");
        //Short Description
        informationTab.findElement(By.xpath(".//input[@name='short_description[en]']")).sendKeys("Short Description");
        //Description
        informationTab.findElement(By.xpath(".//div[@class='trumbowyg-editor']")).click();
        informationTab.findElement(By.xpath(".//div[@class='trumbowyg-editor']")).sendKeys("Description");
        //Head Title
        informationTab.findElement(By.xpath(".//input[@name='head_title[en]']")).sendKeys("Head Title");
        //Meta Description
        informationTab.findElement(By.xpath(".//input[@name='meta_description[en]']")).sendKeys("Meta Description");

        //PRICES tab
        wd.findElement(By.xpath("//a[text()='Prices']")).click();
        WebElement pricesTab = wd.findElement(By.xpath("//div[@id='tab-prices']"));
        //Purchase Price
        pricesTab.findElement(By.xpath(".//input[@name='purchase_price']")).clear();
        pricesTab.findElement(By.xpath(".//input[@name='purchase_price']")).sendKeys(product.getPrice().toString());
        new Select(pricesTab.findElement(By.xpath("//select[@name='purchase_price_currency_code']"))).selectByVisibleText("US Dollars");
        //Price
        pricesTab.findElement(By.xpath(".//input[@name='prices[USD]']")).clear();
        pricesTab.findElement(By.xpath(".//input[@name='prices[USD]']")).sendKeys(product.getPrice().toString());

        wd.findElement(By.xpath("//button[@name='save']")).click();
    }

    public boolean isProductExists(Product product) {
        wd.get("http://localhost/litecart/admin/?app=catalog&doc=catalog");
        return isElementPresent(By.xpath(String.format("//a[text()='%s']", product.getName())));
    }

    public void addFirstProductToCart() {

        click(By.cssSelector("div#box-most-popular li.product"));

        wait = new FluentWait<WebDriver>(wd)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

        Integer quantity = Integer.valueOf(wd.findElement(By.cssSelector("span.quantity")).getText());

        if (isElementPresent(By.xpath("//select[@name='options[Size]']"))) {
            new Select(wd.findElement(By.xpath("//select[@name='options[Size]']"))).selectByValue("Small");
        }

        click(By.cssSelector("button[name=add_cart_product]"));
        wait.until(ExpectedConditions.textToBe(By.cssSelector("span.quantity"), String.valueOf(quantity + 1)));
        openMainPage();
    }

    public void clearCart() {
        click(By.xpath("//a[contains(text(),'Checkout')]"));

        wait = new FluentWait<WebDriver>(wd)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

        if (isElementPresent(By.cssSelector("li.shortcut"))) {
            click(By.cssSelector("li.shortcut"));
        }
        while (isElementPresent(By.cssSelector("button[name=remove_cart_item]"))) {
            WebElement summaryTable = wd.findElement(By.cssSelector("div#order_confirmation-wrapper"));
            click(By.cssSelector("button[name=remove_cart_item]"));
            wait.until(ExpectedConditions.stalenessOf(summaryTable));
        }
    }


    public void checkExternalLinks() {
        String originalWindow = wd.getWindowHandle();
        wait = new FluentWait<WebDriver>(wd)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

        List<WebElement> links = wd.findElements(By.cssSelector(".fa-external-link"));
        for (WebElement link : links) {
            Set<String> existingWindows = wd.getWindowHandles();
            link.click();
            String newWindow = wait.until(anyWindowOtherThan(existingWindows));

            wd.switchTo().window(newWindow);
            System.out.println(wd.getTitle());
            wd.close();

            wd.switchTo().window(originalWindow);
        }


        System.out.println();
    }

    public String checkBrowserLogsOpeningProducts() {
        StringBuilder str = new StringBuilder();
        openCatalogPage();

        List<String> products = new ArrayList<>();
        List<WebElement> links = wd.findElements(By.xpath("//form[@name='catalog_form']//a[contains(@href,'doc=edit_product') and not(@title='Edit')]"));
        products = links.stream().map(WebElement::getText).collect(Collectors.toList());

        for (String product : products) {
            click(By.linkText(product));
            wd.manage().logs().get("browser").getAll().forEach(logEntry -> str.append(logEntry.toString()));
            openCatalogPage();
        }
        return str.toString();

    }
}


































