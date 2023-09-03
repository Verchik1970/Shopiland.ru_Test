package SLand;

import java.time.Duration;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.apache.commons.lang.time.StopWatch;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SEOTest {
    private final static WebDriver driver = new ChromeDriver();
    public String mainPage = "https://shopiland.ru/";
    private static String fotoProductCard = "picture:nth-child(1)";
    private static String fotoReviewProduct = "div.css-1utmhom:nth-child(1)";
    private static String review_button = "//*[@id=\"root\"]/div/div/div[3]/div[2]/div[3]/div[1]/div/div/div/button[1]";
    private static String WITH_FOTO_REVIEW_BTN = "#with-photo";
    private final String KETTLER_POP_MAIN = "//body/div[@id='root']/div[1]/div[3]/div[1]/div[2]/div[3]/div[1]/a[1]/div[1]/picture[1]/img[1]";
    public static final String KAZAN_COUNT = "//*[@id=\"root\"]/div/div/div[3]/div[1]/fieldset/div/label[6]/span[2]/span";

    @Test
    @Description("Тест проверяет наличие тега alt на фото")
    @Severity(value = SeverityLevel.NORMAL)
    @DisplayName("Проверка наличия тега alt на фото")
    public void seoTestImg() {
        driver.get(mainPage);
        WebElement firstImage = driver.findElement(By.cssSelector("img"));
        String altValue = firstImage.getAttribute("alt");
        System.out.println(altValue);
        //  Проверить, что значение атрибута alt не пустое
        if (!altValue.isEmpty()) {
            System.out.println("Attribute alt is not empty");
        } else {
            System.out.println("Attribute alt is empty");
        }

        //  Перейти на страницу категории товаров
        driver.findElement(By.xpath("//p[contains(text(),'женские сумки')]")).click();

        //  Найти первую картинку на странице категории товаров и проверить наличие атрибута alt
        WebElement firstCategoryImage = driver.findElement(By.cssSelector("img"));
        String categoryAltValue = firstCategoryImage.getAttribute("alt");

        //  Проверить, что значение атрибута alt не пустое
        if (!categoryAltValue.isEmpty()) {
            System.out.println("Attribute alt is not empty");
        } else {
            System.out.println("Attribute alt is empty");
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //  Перейти на страницу отзывов конкретного товара
        driver.findElement(By.xpath(review_button)).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.findElement(By.cssSelector(WITH_FOTO_REVIEW_BTN)).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement productImage = driver.findElement(By.cssSelector("img"));

        String productAltValue = productImage.getAttribute("alt");

        //  Проверить, что значение атрибута alt не пустое
        if (!productAltValue.isEmpty()) {
            System.out.println("Attribute alt is not empty");
        } else {
            System.out.println("Attribute alt is empty");
        }
    }


    @Test
    @Description("Тест проверяет наличие Cannonical link на всех страницах")
    @Severity(value = SeverityLevel.NORMAL)
    @DisplayName("Проверка canonical Link")
    public void canonicalLinkTest() {
        driver.get(mainPage);
        // Объявляем переменную links и получаем список всех ссылок на странице
        java.util.List<WebElement> links = driver.findElements(By.tagName("a"));
        System.out.println(links.size() + links.toString());

        for (int i = 0; i < links.size(); i++) {
            // Повторно находим элемент перед кликом
            links = driver.findElements(By.tagName("a"));
            WebElement link = links.get(i);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(By.tagName("a")));

            link.click();

            java.util.List<WebElement> canonicalLinks = driver.findElements
                    (By.cssSelector("link[rel='canonical']"));
            if (canonicalLinks.size() > 0) {
                System.out.println("Canonical link found on page " + driver.getCurrentUrl() + ": " + canonicalLinks.get(0).getAttribute("href"));
            }

            driver.navigate().back();
        }


    }

    @Test
    @Description("Проверка времени загрузки страницы с результатами поиска в маркетплейсах")
    @Severity(value = SeverityLevel.NORMAL)
    @DisplayName("Проверка скорости загрузки страницы")
    public void timeLoading() {

        driver.get(mainPage);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(KETTLER_POP_MAIN)));

        StopWatch pageLoadTime = new StopWatch();
        pageLoadTime.start();
        driver.findElement(By.xpath(KETTLER_POP_MAIN)).click();
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait1.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(KAZAN_COUNT)));
        // Остановка таймера и вывод времени загрузки страницы в миллисекундах
        pageLoadTime.stop();
        System.out.println("Время загрузки страницы с результатами поиска: " + pageLoadTime.getTime() + " ms");
        assertTrue(pageLoadTime.getTime() <= 20000, "Время загрузки превышает 20 сек");


    }
    @AfterAll
    public static void tearDown() {
        driver.quit();
    }

}


