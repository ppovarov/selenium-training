package ru.stqa.training.selenium.tests;

import net.lightbody.bmp.core.har.Har;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import ru.stqa.training.selenium.model.Account;
import ru.stqa.training.selenium.model.Country;
import ru.stqa.training.selenium.model.Product;
import ru.stqa.training.selenium.model.Zone;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LitecartTests extends TestBase {

    @Test
    public void testLitecartAdminLogin() {
        app.litecart().loginAdmin();
    }

    @Test
    public void testLeftNavigationMenu() {
        app.litecart().loginAdmin();
        app.litecart().clickMenuItems();

    }

    @Test
    public void testStickers() {
        app.litecart().openMainPage();
        app.litecart().checkStickers();
    }

    @Test
    public void testCountriesAndZonesSorting() {
        SoftAssert softAssert = new SoftAssert();

        app.litecart().loginAdmin();
        List<Country> countries = app.litecart().getCountries();

        boolean isSortedByName = countries.stream().sorted(Comparator.comparing(Country::getName)).collect(Collectors.toList()).equals(countries);
        softAssert.assertEquals(isSortedByName, true);

        countries.stream()
                .filter((c) -> c.getZonesCount() > 0)
                .forEach((c) -> {
                    boolean areZonesSortedByName = c.getZones().stream().sorted(Comparator.comparing(Zone::getName)).collect(Collectors.toList()).equals(c.getZones());
                    softAssert.assertEquals(areZonesSortedByName, true, c.getName());
                });
        softAssert.assertAll();
    }

    @Test
    public void testCountriesAndZonesSorting2() {
        SoftAssert softAssert = new SoftAssert();

        app.litecart().loginAdmin();
        List<Country> countries = app.litecart().getGeoZonesCountries();

        countries.forEach((c) -> {
            boolean areZonesSortedByName = c.getZones().stream().sorted(Comparator.comparing(Zone::getName)).collect(Collectors.toList()).equals(c.getZones());
            softAssert.assertEquals(areZonesSortedByName, true, c.getName());
        });
        softAssert.assertAll();
    }

    @Test
    public void testCampaignsProduct() {
        SoftAssert softAssert = new SoftAssert();

        app.litecart().openMainPage();
        Product product = app.litecart().getFirstCampaignsProduct();
        Product productDetails = app.litecart().getProductDetails(product.getLink());

        softAssert.assertEquals(product.getName(), productDetails.getName(), "Product name");
        softAssert.assertEquals(product.getPrice(), productDetails.getPrice(), "Product price");
        softAssert.assertEquals(product.getDiscountPrice(), productDetails.getDiscountPrice(), "Product discount price");

        softAssert.assertEquals(product.getPriceStyle(), "s", "Main page: Product price is crossed");
        softAssert.assertTrue(isGrey(product.getPriceColor()), "Main page: Product price is grey");
        softAssert.assertEquals(product.getDiscountPriceStyle(), "strong", "Main page: Product discount price is bold");
        softAssert.assertTrue(isRed(product.getDiscountPriceColor()), "Main page: Product discount price is red");
        softAssert.assertTrue(pxToDouble(product.getDiscountPriceSize()) > pxToDouble(product.getPriceSize()), "Main page: Discount price font is bigger than regular price font");

        softAssert.assertEquals(productDetails.getPriceStyle(), "s", "Product page: Product price is crossed");
        softAssert.assertTrue(isGrey(productDetails.getPriceColor()), "Product page: Product price is grey");
        softAssert.assertEquals(productDetails.getDiscountPriceStyle(), "strong", "Product page: Product discount price is bold");
        softAssert.assertTrue(isRed(productDetails.getDiscountPriceColor()), "Product page: Product discount price is red");
        softAssert.assertTrue(pxToDouble(productDetails.getDiscountPriceSize()) > pxToDouble(productDetails.getPriceSize()), "Product page: Discount price font is bigger than regular price font");

        softAssert.assertAll();
    }


    boolean isRed(Color color) {
        return (color.getGreen() == 0) && (color.getBlue() == 0);
    }

    boolean isGrey(Color color) {
        return (color.getRed() == color.getGreen()) && (color.getRed() == color.getBlue());

    }

    double pxToDouble(String px) {
        return Double.parseDouble(px.substring(0, px.length() - 2));
    }

    @Test
    public void testRegistration() {
        long now = System.currentTimeMillis();
        Account account = new Account()
                .withEmail(String.format("email%s@notreal.com", now))
                .withPassword("Password1");

        app.litecart().createAccount(account);
        app.litecart().logout();
        boolean result = app.litecart().login(account);
        Assert.assertTrue(result);
        app.litecart().logout();
    }

    @Test
    public void testProductCreation() {
        app.litecart().loginAdmin();
        long now = System.currentTimeMillis();

        Product product = new Product()
                .withName(String.format("Evil Duck %s", now))
                .withPrice("14.99");

        app.litecart().addNewProduct(product, now);

        Assert.assertTrue(app.litecart().isProductExists(product));
    }

    @Test
    public void testAddProductToCart() {
        app.litecart().openMainPage();
        for (int i = 0; i < 3; i++) {
            app.litecart().addFirstProductToCart();
        }
        app.litecart().clearCart();
    }

    @Test
    public void testAddProductToCart2() {
        for (int i = 0; i < 30; i++) {
            app.mainPage().open().openFirstProduct();
            app.productPage().addToCart();
        }
        app.cartPage().open().clearCart();
    }

    @Test
    public void testLinksOpenInNewWindow() {
        app.litecart().loginAdmin();
        app.litecart().openEditCountryPage("");
        app.litecart().checkExternalLinks();

    }

    @Test
    public void testBrowserLogs() {
        app.litecart().loginAdmin();
        String log = app.litecart().checkBrowserLogsOpeningProducts();
        Assert.assertEquals(log, "");
    }

    @Test
    public void testHttpResponsesUsingProxy() {
        app.proxy.newHar();
        app.litecart().loginAdmin();
        Har har = app.proxy.endHar();
        har.getLog().getEntries().forEach(l -> System.out.println(l.getResponse().getStatus() + ":" + l.getRequest().getUrl()));
    }

    
}

























