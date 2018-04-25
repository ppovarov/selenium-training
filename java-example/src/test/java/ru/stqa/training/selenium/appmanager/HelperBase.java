package ru.stqa.training.selenium.appmanager;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Wait;

import java.io.File;

public class HelperBase {
    protected WebDriver wd;
    protected Wait<WebDriver> wait;

    public HelperBase(WebDriver wd) {
        this.wd = wd;
    }

    protected void type(By locator, String text) {
        click(locator);
        if (text != null) {
            String existingText = wd.findElement(locator).getAttribute("value");
            if (!text.equals(existingText)) {
                wd.findElement(locator).clear();
                wd.findElement(locator).sendKeys(text);
            }
        }
    }

    protected void attach(By locator, File file) {
        if (file != null) {
            wd.findElement(locator).sendKeys(file.getAbsolutePath());
        }
    }

    protected void click(By locator) {
        wd.findElement(locator).click();
    }

    public boolean isAlertPresent() {
        try {
            wd.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    protected boolean isElementPresent(By locator) {
        try {
            wd.findElement(locator);
            return true;
        } catch (InvalidSelectorException ex) {
            throw ex;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    protected boolean areElementsPresent(By locator) {
        return wd.findElements(locator).size() > 0;
    }

    protected boolean areElementsPresent(WebElement element, By locator) {
        return element.findElements(locator).size() > 0;
    }

}
