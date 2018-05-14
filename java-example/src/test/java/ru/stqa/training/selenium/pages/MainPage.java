package ru.stqa.training.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MainPage extends Page {

    public MainPage(WebDriver wd) {
        super(wd);
    }

    public MainPage open() {
        wd.get("http://localhost/litecart/");
        return this;
    }

    public void openFirstProduct() {
        wd.findElement(By.cssSelector("div#box-most-popular li.product")).click();
    }
}
