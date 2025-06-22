package main;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StockChecker {
    public static void main(String[] args) throws InterruptedException {
        if (!ConfigReader.get("bot.enabled").equalsIgnoreCase("true")) {
            System.out.println("Bot is currently disabled. Exiting...");
            return;
        }
        WebDriver driver = new ChromeDriver();
        ChromeOptions options= new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--windows-size=1920,1080");
        try{
            String[] itemsCheck={"Amul Chocolate Whey Protein, 34 g","Amul Whey Protein, 32 g"};
            int count=0;
            String url="https://shop.amul.com/en/browse/protein";
            driver.get(url);
            WebElement pin=driver.findElement(By.xpath("//input[@placeholder='Enter Your Pincode']"));
            WebDriverWait wait= new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.elementToBeClickable(pin));
            pin.sendKeys(ConfigReader.get("pincode"));
            Thread.sleep(5000);
            //WebElement searchResult= driver.findElement(By.xpath("//a[@class='searchitem-name p-3 d-flex']"));
            WebElement searchResult= driver.findElement(By.xpath("//p[@class='item-name text-dark mb-0 fw-semibold fs-6']"));
            wait.until(ExpectedConditions.elementToBeClickable(searchResult));
            searchResult.click();
            System.out.println("Page title: " + driver.getTitle());

//            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@title='Add to Cart']")));
//            List<WebElement>  items= driver.findElements(By.xpath("//a[@title='Add to Cart']"));

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@title='Notify Me']")));
            List<WebElement> productCards = driver.findElements(By.xpath("//div[contains(@class, 'col product-grid-col')]"));

            for (WebElement productCard : productCards) {
                if(count==2) break;
                try {
                    // Check if it has the Notify Me button
                    WebElement notifyBtn = productCard.findElement(By.xpath(".//a[@title='Notify Me']"));

                    // Get the product name inside the same card
                    WebElement nameElement = productCard.findElement(By.xpath(".//a[@class='lh-sm m-0 d-block fw-semibold text-dark']"));
                    String productName = nameElement.getText();
                    for (String matchItem : itemsCheck) {
                        if (productName.toLowerCase().contains(matchItem.toLowerCase())) {
                            TelegramAlert.sendMessage("✅ ALERT: " + productName);
                            count++;
                            break;
                        }
                    }

                } catch (NoSuchElementException e) {
                    // Skip if Notify Me button not found in this card
                }
            }
        }finally {
            driver.quit();
        }
    }
}
