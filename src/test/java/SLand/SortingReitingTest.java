package SLand;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SortingReitingTest {
    private final static WebDriver driver = new ChromeDriver();
    public static String mainPage = "https://shopiland.ru/";
    private final String KETTLER_POP_MAIN = "//body/div[@id='root']/div[1]/div[3]/div[1]/div[2]/div[3]/div[1]/a[1]/div[1]/picture[1]/img[1]";
    private final String ALI_EXCLUDE_BUTTON = "input.PrivateSwitchBase-input.css-1m9pwf3[name=\"al\"][type=\"checkbox\"][aria-label=\"Checkbox demo\"][checked=\"\"]";
    private final String REITING_SORT_BUTTON = "//div[contains(text(),'рейтингу')]";
    private final String BRAND_SORT_BUTTON_FIRST = "a.css-95nm5l:nth-child(1)";
    private final String CARDS_TEXT_VALUE = "p.css-99ww93:nth-child(5)";
    private final String REITING_COUNT_VALUE ="span.css-1t0tstb";



        @Test
        @Description(value = "Тест проверяет правильность сортировки по рейтингу")
        @Severity(value = SeverityLevel.NORMAL)
        @DisplayName("Проверка сортировки товара по рейтингу")
        public void sortingReiting() {
            driver.get(mainPage);
            driver.findElement(By.xpath(KETTLER_POP_MAIN)).click();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.findElement(By.cssSelector(ALI_EXCLUDE_BUTTON)).click();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.findElement(By.xpath(REITING_SORT_BUTTON)).click();
            // находим все карточки товара на странице
            List<WebElement> productCards = driver.findElements(By.cssSelector
                    (REITING_COUNT_VALUE));
            Allure.addAttachment("Отсортированные товары по рейтингу", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

// получаем первую и последнюю карточки товара
            WebElement firstProductCard = productCards.get(0);
            String first = firstProductCard.getText();
            System.out.println(first);
            WebElement lastProductCard = productCards.get(productCards.size() - 1);
            String last = lastProductCard.getText();
            System.out.println(last);
        }

        @Test
        @Flaky
        @Description(value = "Тест проверяет правильность сортировки по брендам")
        @Severity(value = SeverityLevel.NORMAL)
        @DisplayName("Проверка сортировки по брендам")
        public void sortBrendItem() throws InterruptedException {
            driver.get(mainPage);
            driver.findElement(By.xpath(KETTLER_POP_MAIN)).click();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable
                    (By.cssSelector(BRAND_SORT_BUTTON_FIRST)));
            String brand1 = driver.findElement
                    (By.cssSelector(BRAND_SORT_BUTTON_FIRST)).getText();
            System.out.println(brand1);
            driver.findElement(By.cssSelector(BRAND_SORT_BUTTON_FIRST)).click();
            Thread.sleep(6000); //здесь не всегда работает
            List<WebElement> productCards = driver.findElements
                    (By.cssSelector(CARDS_TEXT_VALUE));
            Allure.addAttachment("Отсортированные товары по бренду", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

            WebElement firstProductCard = productCards.get(0);
            String first = firstProductCard.getText();
            System.out.println(first);
            Pattern pattern = Pattern.compile(brand1,
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(first);
            assertTrue(matcher.find(), brand1);

        }

        @AfterAll
        public static void tearDown() {
            driver.quit();
        }
    }


