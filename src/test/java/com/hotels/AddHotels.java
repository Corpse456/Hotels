package com.hotels;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.hotels.constants.HotelFieldNames;
import com.hotels.entities.Category;
import com.hotels.entities.FreeServices;
import com.hotels.entities.Hotel;
import com.hotels.entities.PaymentMethod;
import com.hotels.services.CategoryService;

public class AddHotels extends AbstractUITest {
    
    private CategoryService categoryService = CategoryService.getInstance();
    private WebDriverWait waitDriver;
    
    @Test
    public void addHotels () throws InterruptedException {
        List<Hotel> hotels = ensureTestData();
        
        driver.get(BASE_URL);
        waitDriver = new WebDriverWait(driver, 30);
        
        String gridPath = "//*[@id='HotelGrid']/div[3]/table";
        waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(gridPath)));
        WebElement grid = driver.findElement(By.xpath(gridPath));
        int rows = Integer.parseInt(grid.getAttribute("aria-rowcount"));
 
        int amount = hotels.size();
        for (int i = 0; i < amount; i++) {
            Hotel current = hotels.get(i);
            
            driver.findElement(By.id("addHotel")).click();
            
            waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.id(HotelFieldNames.Name.toString())));
            driver.findElement(By.id(HotelFieldNames.Name.toString())).sendKeys(current.getName());
            Thread.sleep(5);
            
            driver.findElement(By.id(HotelFieldNames.Address.toString())).sendKeys(current.getAddress());
            Thread.sleep(5);
            
            driver.findElement(By.id(HotelFieldNames.Rating.toString())).sendKeys(current.getRating() + "");
            Thread.sleep(5);

            driver.findElement(By.xpath("//*[@id='" + HotelFieldNames.OperatesFrom.toString() + "']//*[@type='text']")).sendKeys(dateCombinator(current));
            Thread.sleep(5);
            
            Select category = new Select(driver.findElement(By.xpath("//*[@id='" + HotelFieldNames.Category.toString() + "']//*[@class='v-select-select']")));
            category.selectByVisibleText(current.getCategory().toString());
            Thread.sleep(5);
            
            freeServices(current);
            
            payment(current);
            
            driver.findElement(By.id(HotelFieldNames.Description.toString())).sendKeys(current.getDescription());
            Thread.sleep(5);
            
            driver.findElement(By.id(HotelFieldNames.URL.toString())).sendKeys(current.getUrl());
            Thread.sleep(5);
            
            driver.findElement(By.id("HotelSave")).click();
            Thread.sleep(5);
        }
        
        grid = driver.findElement(By.xpath(gridPath));
        int rows2 = Integer.parseInt(grid.getAttribute("aria-rowcount"));
        
        Assert.assertEquals(rows + amount, rows2);
    }

    private void payment (Hotel current) {
        PaymentMethod payment = current.getPaymentMethod();
      
        if (!payment.isCash()) {
            driver.findElement(By.xpath("//*[@id=\"HotelRadioButton\"]/span[1]")).click();
            driver.findElement(By.id("HotelPrepayment")).sendKeys(payment.getCard() + "");;
        } 
    }

    private void freeServices (Hotel current) throws InterruptedException {
        FreeServices freeServices = current.getFreeServices();
        
        if (freeServices.isBreakfast()) {
            driver.findElement(By.xpath("//*[@id='breakfast']/span")).click();
            Thread.sleep(5);
        }
        if (freeServices.isTowels()) {
            driver.findElement(By.xpath("//*[@id='towels']/span")).click();;
            Thread.sleep(5);
        }
        if (freeServices.isColdSpirits()) {
            driver.findElement(By.xpath("//*[@id='spirits']/span")).click();;
            Thread.sleep(5);
        }
    }

    private String dateCombinator (Hotel current) {
        Long longDate = current.getOperatesFrom();
        LocalDate date = LocalDate.ofEpochDay(longDate);
        return date.getDayOfMonth() + "." + date.getMonthValue() + "." + date.getYear();
    }
    
    public List<Hotel> ensureTestData () {
        List<Hotel> hotels = new ArrayList<>();
        
            final String[] hotelData = new String[] {
                    "3 Nagas Luang Prabang - MGallery by Sofitel;4;https://www.booking.com/hotel/la/3-nagas-luang-prabang-by-accor.en-gb.html;Vat Nong Village, Sakkaline Road, Democratic Republic Lao, 06000 Luang Prabang, Laos;",
                    "Abby Boutique Guesthouse;1;https://www.booking.com/hotel/la/abby-boutique-guesthouse.en-gb.html;Ban Sawang , 01000 Vang Vieng, Laos",
                    "Bountheung Guesthouse;1;https://www.booking.com/hotel/la/bountheung-guesthouse.en-gb.html;Ban Tha Heua, 01000 Vang Vieng, Laos",
                    "Chalouvanh Hotel;2;https://www.booking.com/hotel/la/chalouvanh.en-gb.html;13 road, Ban Phonesavanh, Pakse District, 01000 Pakse, Laos",
                    "Chaluenxay Villa;3;https://www.booking.com/hotel/la/chaluenxay-villa.en-gb.html;Sakkarin Road Ban Xienthong Luang Prabang Laos, 06000 Luang Prabang, Laos",
                    "Dream Home Hostel 1;1;https://www.booking.com/hotel/la/getaway-backpackers-hostel.en-gb.html;049 Sihome Road, Ban Sihome, 01000 Vientiane, Laos",
                    "Inpeng Hotel and Resort;2;https://www.booking.com/hotel/la/inpeng-and-resort.en-gb.html;406 T4 Road, Donekoy Village, Sisattanak District, 01000 Vientiane, Laos",
                    "Jammee Guesthouse II;2;https://www.booking.com/hotel/la/jammee-guesthouse-vang-vieng1.en-gb.html;Vang Vieng, 01000 Vang Vieng, Laos",
                    "Khemngum Guesthouse 3;2;https://www.booking.com/hotel/la/khemngum-guesthouse-3.en-gb.html;Ban Thalat No.10 Road Namngum Laos, 01000 Thalat, Laos",
                    "Khongview Guesthouse;1;https://www.booking.com/hotel/la/khongview-guesthouse.en-gb.html;Ban Klang Khong, Khong District, 01000 Muang Không, Laos",
                    "Kong Kham Pheng Guesthouse;1;https://www.booking.com/hotel/la/kong-kham-pheng-guesthouse.en-gb.html;Mixay Village, Paksan district, Bolikhamxay province, 01000 Muang Pakxan, Laos",
                    "Laos Haven Hotel & Spa;3;https://www.booking.com/hotel/la/laos-haven.en-gb.html;047 Ban Viengkeo, Vang Vieng , 01000 Vang Vieng, Laos",
                    "Lerdkeo Sunset Guesthouse;1;https://www.booking.com/hotel/la/lerdkeo-sunset-guesthouse.en-gb.html;Muang Ngoi Neua,Ban Ngoy-Nua, 01000 Muang Ngoy, Laos",
                    "Luangprabang River Lodge Boutique 1;3;https://www.booking.com/hotel/la/luangprabang-river-lodge.en-gb.html;Mekong River Road, 06000 Luang Prabang, Laos",
                    "Manichan Guesthouse;2;https://www.booking.com/hotel/la/manichan-guesthouse.en-gb.html;Ban Pakham Unit 4/143, 60000 Luang Prabang, Laos",
                    "Mixok Inn;2;https://www.booking.com/hotel/la/mixok-inn.en-gb.html;188 Sethathirate Road , Mixay Village , Chanthabuly District, 01000 Vientiane, Laos",
                    "Ssen Mekong;2;https://www.booking.com/hotel/la/muang-lao-mekong-river-side-villa.en-gb.html;Riverfront, Mekong River Road, 06000 Luang Prabang, Laos",
                    "Nammavong Guesthouse;2;https://www.booking.com/hotel/la/nammavong-guesthouse.en-gb.html;Ban phone houang Sisalearmsak Road , 06000 Luang Prabang, Laos",
                    "Niny Backpacker hotel;1;https://www.booking.com/hotel/la/niny-backpacker.en-gb.html;Next to Wat Mixay, Norkeokhunmane Road., 01000 Vientiane, Laos",
                    "Niraxay Apartment;2;https://www.booking.com/hotel/la/niraxay-apartment.en-gb.html;Samsenthai Road Ban Sihom , 01000 Vientiane, Laos",
                    "Pakse Mekong Hotel;2;https://www.booking.com/hotel/la/pakse-mekong.en-gb.html;No 062 Khemkong Road, Pakse District, Champasak, Laos, 01000 Pakse, Laos",
                    "Phakchai Hotel;2;https://www.booking.com/hotel/la/phakchai.en-gb.html;137 Ban Wattay Mueng Sikothabong Vientiane Laos, 01000 Vientiane, Laos",
                    "Phetmeuangsam Hotel;2;https://www.booking.com/hotel/la/phetmisay.en-gb.html;Ban Phanhxai, Xumnuea, Xam Nua, 01000 Xam Nua, Laos" };

            Random r = new Random(0);
            for (String hotel : hotelData) {
                String[] split = hotel.split(";");
                Hotel h = new Hotel();
                h.setName(split[0]);
                h.setDescription("Empty");
                h.setRating(Integer.parseInt(split[1]));
                h.setUrl(split[2]);
                h.setAddress(split[3]);
                h.setFreeServices(freeServicesPrepare());
                h.setPaymentMethod(paymentMethodPrepare());
                List<Category> allCategory = categoryService .findAll();
                h.setCategory(allCategory.get((int) (Math.random() * allCategory.size())));
                int daysOld = 0 - r.nextInt(365 * 30);
                h.setOperatesFrom((LocalDate.now().plusDays(daysOld)).getLong(ChronoField.EPOCH_DAY));
                hotels.add(h);
            }
            return hotels;
        }
    
    private PaymentMethod paymentMethodPrepare () {
        PaymentMethod pay = new PaymentMethod();
        if (Math.random() > 0.5) {
            pay.setCash(false);
            pay.setCard((int) (Math.random() * 100));
        }
        return pay;
    }

    private FreeServices freeServicesPrepare () {
        FreeServices free = new FreeServices();
        free.setBreakfast(Math.random() > 0.5 ? true : false);
        free.setColdSpirits(Math.random() > 0.5 ? true : false);
        free.setTowels(Math.random() > 0.5 ? true : false);
        return free;
    }
}
