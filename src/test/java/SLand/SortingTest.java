package SLand;
/*import org.junit.Test;*/

import com.google.common.collect.ImmutableMap;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.*;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.ByteArrayInputStream;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SortingTest {

    private final static WebDriver driver = new ChromeDriver();
    public String mainPage = "https://shopiland.ru/";
    private final String KETTLER_POP_MAIN = "//body/div[@id='root']/div[1]/div[3]/div[1]/div[2]/div[3]/div[1]/a[1]/div[1]/picture[1]/img[1]";
    private final String ALI_EXCLUDE_BUTTON = "input.PrivateSwitchBase-input.css-1m9pwf3[name=\"al\"][type=\"checkbox\"][aria-label=\"Checkbox demo\"][checked=\"\"]";
    private final String PRICE_SORT_BUTTON = "//*[@id=\"root\"]/div/div/div[3]/div[2]/div[1]/div[1]/div[2]/button[1]/div";

    private final String CARD_ITEM_1 = "//body/div[@id='root']/div[1]/div[1]/div[3]/div[2]/div[3]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]";
    private final String CARD_ITEM_12 = "//body/div[@id='root']/div[1]/div[1]/div[3]/div[2]/div[3]/div[12]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]]";
    private final String LOW_PRICE_ITEM = "//*[@id=\"root\"]/div/div/div[3]/div[2]/div[3]/div[1]/div/div/a/div[2]/div/div[1]/span[1]";
    private final String HIGH_PRICE_ITEM = "//*[@id=\"root\"]/div/div/div[3]/div[2]/div[3]/div[44]/div/div/a/div[2]/div/div[1]/span[1]";

    private final String MIN_PRICE_INPUT = "#min_price";
    private final String MAX_PRICE_INPUT = "#max_price";
    private final String minPrice = "1000";
    private final String maxPrice = "3000";
    int iMin = 0;
    int iMax = 0;


    @Test
    @Description("Проверка сортировки по возрастанию цены")
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Проверка сортировки по возрастанию цены ")
    public void sortingPrice() {

        driver.get(mainPage);
        driver.findElement(By.xpath(KETTLER_POP_MAIN)).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.cssSelector(ALI_EXCLUDE_BUTTON)).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.findElement(By.xpath(PRICE_SORT_BUTTON)).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Allure.addAttachment("Отсортированные товары по возрастанию цены", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        String text = driver.findElement(By.xpath(LOW_PRICE_ITEM)).getText().replaceAll(" ", "");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        String priceMin = text.replaceAll("[^0-9]", "");
        System.out.println(priceMin + " " + "MIN");
        try {
            iMin = Integer.parseInt(priceMin);
        } catch (NumberFormatException e) {
            System.err.println("Неправильный формат строки!");
        }

        String textMax = driver.findElement(By.xpath(HIGH_PRICE_ITEM)).getText().replaceAll(" ", "");
        System.out.println(textMax);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        String priceMax = textMax.replaceAll("[^0-9]", "");
        System.out.println("MAX" + " " + priceMax);

        try {
            iMax = Integer.parseInt(priceMax);
        } catch (NumberFormatException e) {
            System.err.println("Неправильный формат строки!");
        }
        assertTrue(iMin <= iMax, "Сортировка неверно работает");


    }

    @Test
    @Description("Проверка сортировки по убыванию цены")
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Проверка сортировки по убыванию цены")
    public void sortingPriceLow() {

        driver.get(mainPage);
        driver.findElement(By.xpath(KETTLER_POP_MAIN)).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.cssSelector(ALI_EXCLUDE_BUTTON)).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.findElement(By.xpath(PRICE_SORT_BUTTON)).click();
        driver.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS);
        driver.findElement(By.xpath(PRICE_SORT_BUTTON)).click();
        driver.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS);
        Allure.addAttachment("Отсортированные товары уменьшению цены", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        String text = driver.findElement(By.xpath(LOW_PRICE_ITEM)).getText().replaceAll(" ", "");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        String priceMAX = text.replaceAll("[^0-9]", "");
        System.out.println(priceMAX + " " + "Maximum price");
        try {
            iMax = Integer.parseInt(priceMAX);

        } catch (NumberFormatException e) {
            System.err.println("Неправильный формат строки!");
        }

        String textMIN = driver.findElement(By.xpath(HIGH_PRICE_ITEM)).getText().replaceAll(" ", "");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        String priceMIN = textMIN.replaceAll("[^0-9]", "");
        System.out.println("MINIMUM price" + " " + priceMIN);

        try {
            iMin = Integer.parseInt(priceMIN);

        } catch (NumberFormatException e) {
            System.err.println("Неправильный формат строки!");
        }
        assertTrue(iMin <= iMax, "Сортировка неверно работает");


    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
    }

    @Test
    @Description("Проверка правильности отбора товара по диапазону цен")
    @Severity(value = SeverityLevel.CRITICAL)
    @DisplayName("Проверка сортировки по диапазону цен")
    public void searchInputPriceTest() {
        driver.get(mainPage);
        driver.findElement(By.xpath(KETTLER_POP_MAIN)).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.cssSelector(ALI_EXCLUDE_BUTTON)).click();
        driver.findElement(By.cssSelector(MIN_PRICE_INPUT)).sendKeys(minPrice + "\n");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.findElement(By.cssSelector(MAX_PRICE_INPUT)).sendKeys(maxPrice + "\n");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Allure.addAttachment("Отсортированные товары по ценовому диапазону", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        String text = driver.findElement(By.xpath(LOW_PRICE_ITEM)).getText().replaceAll(" ", "");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        String priceMin = text.replaceAll("[^0-9]", "");
        System.out.println(priceMin + " " + "MIN");
        try {
            iMin = Integer.parseInt(priceMin);
        } catch (NumberFormatException e) {
            System.err.println("Неправильный формат строки!");
        }

        String textMax = driver.findElement(By.xpath(HIGH_PRICE_ITEM)).getText().replaceAll(" ", "");
        System.out.println(textMax);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        String priceMax = textMax.replaceAll("[^0-9]", "");
        System.out.println("MAX" + " " + priceMax);

        try {
            iMax = Integer.parseInt(priceMax);
        } catch (NumberFormatException e) {
            System.err.println("Неправильный формат строки!");
        }
        assertTrue(iMin > 1000 && iMin < 3000, "Сортировка неверно работает");
        assertTrue(iMax > 1000 && iMax < 3000, "Сортировка неверно работает");


    }
}

