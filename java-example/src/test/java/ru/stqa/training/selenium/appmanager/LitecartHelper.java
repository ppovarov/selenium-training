package ru.stqa.training.selenium.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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

    public void clickMenuItems() {
        List<String> items = wd.findElements(By.xpath("//div[@id='box-apps-menu-wrapper']//span[@class='name']"))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());

        for (String item : items) {
            click(By.xpath(String.format("//span[@class='name' and text()='%s']", item)));
            System.out.println(item);
            List<String> subItems = wd.findElements(By.xpath("//li[@class='selected']/ul//span[@class='name']"))
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());

            for (String subItem : subItems) {
                click(By.xpath(String.format("//span[@class='name' and text()='%s']", subItem)));
                System.out.println(item + " - " + subItem + " (" + getPageHeading() + ")");
                //check heading
            }

        }

    }

    public String getPageHeading() {
        return wd.findElement(By.tagName("h1")).getText();
    }
}