package ru.stqa.training.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.NoSuchElementException;

public class CartPage extends Page {

    public CartPage(WebDriver wd) {
        super(wd);
    }

    public CartPage open() {
        click(By.xpath("//a[contains(text(),'Checkout')]"));
        return this;
    }

    public CartPage clearCart() {
        wait = new FluentWait<WebDriver>(wd)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

        stopCarousel();

        while (isElementPresent(By.cssSelector("button[name=remove_cart_item]"))) {
            WebElement summaryTable = wd.findElement(By.cssSelector("div#order_confirmation-wrapper"));
            click(By.cssSelector("button[name=remove_cart_item]"));
            wait.until(ExpectedConditions.stalenessOf(summaryTable));

        }
        return this;
    }

    public CartPage stopCarousel() {
        if (isElementPresent(By.cssSelector("li.shortcut"))) {
            click(By.cssSelector("li.shortcut"));
        }
        return this;
    }
}
