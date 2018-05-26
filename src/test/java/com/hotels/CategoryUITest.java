package com.hotels;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


public class CategoryUITest extends AbstractUITest {
    
    @Test
    public void testCategoryPage() throws InterruptedException {
        driver.get(BASE_URL);
        Thread.sleep(2000);
        
        WebElement categories = driver.findElement(By.cssSelector(".mytheme .v-menubar .v-menubar-menuitem:last-child"));
        categories.click();
        Thread.sleep(2000);
        
        Assert.assertEquals("http://localhost:8080/#!Category", driver.getCurrentUrl());
    }
    
    @Test
    public void testFilter() throws InterruptedException {
        driver.get(BASE_URL);
        Thread.sleep(2000);
        
        driver.findElement(By.xpath("//input[@placeholder='filter by address']")).sendKeys("60");
        
        Thread.sleep(2000);
        int founded = driver.findElements(By.xpath("//tbody[@class='v-grid-body']/tr[contains(@class, 'v-grid-row')]")).size();
        
        Assert.assertEquals(6, founded);
    }
}
