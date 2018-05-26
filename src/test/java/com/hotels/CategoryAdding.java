package com.hotels;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;

public class CategoryAdding extends AbstractUITest {

    @Test
    public void addCategories () throws InterruptedException {
        String[] names = {"Villa", "House", "Lux"};
        
        driver.get(BASE_URL);
        Thread.sleep(4000);
      
        driver.findElement(By.cssSelector(".mytheme .v-menubar .v-menubar-menuitem:last-child")).click();
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
