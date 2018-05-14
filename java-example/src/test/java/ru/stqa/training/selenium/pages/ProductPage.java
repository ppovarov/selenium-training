package ru.stqa.training.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.NoSuchElementException;

public class ProductPage extends Page {

    @FindBy(css = "span.quantity")
    private WebElement quantityElement;

    public ProductPage(WebDriver wd) {
        super(wd);
        PageFactory.initElements(wd, this);
    }

    public ProductPage addToCart() {
        wait = new FluentWait<WebDriver>(wd)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

        Integer quantity = Integer.valueOf(quantityElement.getText());

        selectSize("Small");

        click(By.cssSelector("button[name=add_cart_product]"));
        wait.until(ExpectedConditions.textToBePresentInElement(quantityElement, String.valueOf(quantity + 1)));
        return this;
    }

    public ProductPage selectSize(String size) {
        if (isElementPresent(By.xpath("//select[@name='options[Size]']"))) {
            new Select(wd.findElement(By.xpath("//select[@name='options[Size]']"))).selectByValue(size);
        }
        return this;
    }


}
