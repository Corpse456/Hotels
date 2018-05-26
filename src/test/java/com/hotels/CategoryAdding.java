package com.hotels;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CategoryAdding extends AbstractUITest {

    @Test
    public void addCategories () throws InterruptedException {
        String[] names = {"Villa", "House", "Lux"};
        
        driver.get(BASE_URL);
        WebDriverWait waitDriver = new WebDriverWait(driver, 30);
        
        String categoryButton = ".mytheme .v-menubar .v-menubar-menuitem:last-child";
        waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(categoryButton)));
        driver.findElement(By.cssSelector(categoryButton)).click();
        Thread.sleep(200);
        
        int rows = driver.findElements(By.xpath("//tbody[@class='v-grid-body']/tr[contains(@class, 'v-grid-row')]")).size();
        
        for (int i = 0; i < names.length; i++) {
            driver.findElement(By.id("Add")).click();
            Thread.sleep(200);

            driver.findElement(By.id("CategoryName")).sendKeys(names[i]);
            Thread.sleep(200);
            
            driver.findElement(By.id("Save")).click();
            Thread.sleep(200);
        }
        
        int rows2 = driver.findElements(By.xpath("//tbody[@class='v-grid-body']/tr[contains(@class, 'v-grid-row')]")).size();
        
        Assert.assertEquals(rows + names.length, rows2);
    }
}
