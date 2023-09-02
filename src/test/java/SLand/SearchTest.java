package SLand;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.By.xpath;

public class SearchTest {

    private final static WebDriver driver = new ChromeDriver();
    public String mainPage = "https://shopiland.ru/";

    /*   *****************LOCATORS***************
     **********************************************
     */
    private final String SEARCH_INPUT = "//body/div[@id='root']/div[1]/div[1]/div[1]/form[1]/div[1]/div[1]/div[1]/input[1]";
    private final String ERROR_FOUND_ITEMS = "//div[contains(text(),'Ничего не найдено')]";
    public static final String OZON_COUNT = "//*[@id=\"root\"]/div/div/div[3]/div[1]/fieldset/div/label[1]/span[2]/span";
    public static final String ALI_COUNT = "//*[@id=\"root\"]/div/div/div[3]/div[1]/fieldset/div/label[2]/span[2]/span";
    public static final String WILDBERIES_COUNT = "//*[@id=\"root\"]/div/div/div[3]/div[1]/fieldset/div/label[3]/span[2]/span";
    public static final String YM_COUNT = "//*[@id=\"root\"]/div/div/div[3]/div[1]/fieldset/div/label[4]/span[2]/span";
    public static final String SBER_COUNT = "//*[@id=\"root\"]/div/div/div[3]/div[1]/fieldset/div/label[5]/span[2]/span";
    public static final String KAZAN_COUNT = "//*[@id=\"root\"]/div/div/div[3]/div[1]/fieldset/div/label[6]/span[2]/span";


    /*
     ****************METHOD*****************
     ***********************************************/
    private int countResult() {
        List<WebElement> resultSearch = driver.findElements(By.cssSelector("p.css-99ww93:nth-child(5)"));
        return resultSearch.size();

    }

    @ParameterizedTest
    @Description("Тест проверяет, что поиск происходит при вводе валидных данных в строку поиска")
    @Severity(value = SeverityLevel.CRITICAL)
    @ValueSource(strings = {"кофе зерновой", "coffe", "КОФЕ РАСТВОРИМЫЙ", " кофе "})
    @DisplayName("Ввод валидный тестовых данных в поле поиска")
    public void testSearchValue(String value) throws IOException {

        driver.get(mainPage);
        driver.manage().window().maximize();
        driver.findElement(xpath(SEARCH_INPUT)).sendKeys(value + "\n");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Allure.addAttachment("Осуществляется поиск по валидному тестовому слову",
                new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        assertTrue(countResult() > 0, "В маркетплейсе отсутствуют товары с данным поисковым запросом.");
    }

    @ParameterizedTest
    @Description("Тест проверяет, что при воде невалидных данных в поле поиска появляется ошибка")
    @Severity(value = SeverityLevel.CRITICAL)
    @ValueSource(strings = {"12321787893740", "%^&(&)(*)&*^&", " ", "        "})
    @DisplayName("Ввод невалидный тестовых данных в поле поиска")
    public void testSearchInvalidValue(String value) throws IOException {

        driver.get(mainPage);
        driver.manage().window().maximize();
        driver.findElement(xpath(SEARCH_INPUT)).sendKeys(value + "\n");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ERROR_FOUND_ITEMS)));
        } catch (Exception e) {
            System.out.println("Поиск по значению " + " /" + value + " /" + " не производится");

        }

        String error = driver.findElement(By.xpath(ERROR_FOUND_ITEMS)).getText();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        Allure.addAttachment("Ошибка при вводе невалидного значения в поиск",
                new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        assertTrue(Objects.equals(error, "Ничего не найдено"), "Поиск товара по невалидным данным происходит.");
    }

    @ParameterizedTest
    @Description("Тест проверят, что найденные товары соответствуют искомой фразе")
    @Severity(value = SeverityLevel.CRITICAL)
    @ValueSource(strings = {"кофе зерновой", "coffe", "КОФЕ РАСТВОРИМЫЙ", " кофе "})
    @DisplayName("сравнение текста")
    public void setAssertTextTest(String value) throws IOException {
        driver.get(mainPage);

        driver.manage().window().maximize();
        WebElement searchFields = driver.findElement(xpath(SEARCH_INPUT));
        searchFields.sendKeys(value + "\n");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Allure.addAttachment("Проверка соответствия названия товара поисковой фразе",
                new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        WebElement productText = driver.findElement(xpath("//p[contains(text(),'Кофе')]"));
        System.out.println(productText.getSize());
    }

    @ParameterizedTest
    @Description("Тест проверяет, что товар ищется во всех маркетплейсах")
    @Severity(value = SeverityLevel.CRITICAL)
    @ValueSource(strings = {OZON_COUNT, ALI_COUNT, WILDBERIES_COUNT, YM_COUNT, SBER_COUNT, KAZAN_COUNT})
    @DisplayName("наличие в маркетплейсах")
    public void setAssertMarketplaceTest(String value) throws IOException {
        driver.get(mainPage);

        driver.manage().window().maximize();
        WebElement searchFields = driver.findElement(By.xpath(SEARCH_INPUT));
        searchFields.sendKeys("кофе зерновой" + "\n");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement productText = driver.findElement(By.xpath(value));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        Allure.addAttachment("Наличие товаров в конкретном маркетплейсе",
                new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

        String marketSearch = productText.getAttribute("textContent");
        System.out.println(marketSearch);
        assertNotEquals("0 шт", marketSearch, "нет в маркете" + value);
    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
    }

}