package ru.stqa.training.selenium.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

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
        for (WebElement element: elements){
            String name = element.findElement(By.cssSelector("div.name")).getText();
            softAssert.assertEquals(element.findElements(By.cssSelector("div.sticker")).size(), 1, name);
        }
        softAssert.assertAll();
    }


}


































