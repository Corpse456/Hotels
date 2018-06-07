package com.hotels.services;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FacilitiesService {
    protected WebDriver driver;
    private WebDriverWait waitDriver;
    protected static final String BASE_URL = "http://localhost:8080";
    //private HotelService service = ServiceProvider.getHotelService();

    public void click () throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        driver = new ChromeDriver();

        driver.get(BASE_URL);
        waitDriver = new WebDriverWait(driver, 5);

        String mainWindowHandle = driver.getWindowHandle();
        String rowPath = "//*[@id='HotelGrid']/div[3]/table/tbody/tr";
        //List<Hotel> allHotels = service.findAll();
        Actions action = new Actions(driver);
        
        Set<String> hotelsInGrid = new TreeSet<>(); 
        boolean allRows = false;
        while (!allRows) {
            List<WebElement> visibleRows = waitDriver.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(rowPath)));
            allRows = true;
            
            for (WebElement currentRow : visibleRows) {
                String name = currentRow.findElement(By.xpath("./td[1]")).getText();
                if (!hotelsInGrid.add(name)) continue;
                
                allRows = false;

                /*@SuppressWarnings ("unused")
                Hotel currentHotel;
                for (Hotel hotel : allHotels) {
                    if (hotel.getName().equals(name)) currentHotel = hotel;
                    // TO DO
                }*/

                // find link
                WebElement link = currentRow.findElement(By.tagName("a"));
                // click on it
                action.moveToElement(link).click();
                action.build().perform();
                // and wait while new window opened
                String newWindowHandle = waitDriver.until(driver -> {
                    Set<String> newWindowsSet = driver.getWindowHandles();
                    newWindowsSet.remove(mainWindowHandle);
                    return newWindowsSet.size() > 0 ? newWindowsSet.iterator().next() : null;
                });
                driver.switchTo().window(newWindowHandle);

                StringBuilder newHotelFacilities = new StringBuilder();
                List<WebElement> facilities = driver.findElements(By.xpath("//div[@id='hotel_main_content']/div/div/div/div[@class='important_facility ']"));
                Iterator<WebElement> iterator = facilities.iterator();
                while (iterator.hasNext()) {
                    newHotelFacilities.append(iterator.next().getText());
                    if (iterator.hasNext()) newHotelFacilities.append(",");
                }
                
                driver.close();
                driver.switchTo().window(mainWindowHandle);
            }
        }
        driver.quit();
    }
}
