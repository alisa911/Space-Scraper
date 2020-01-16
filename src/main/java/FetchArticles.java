import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class FetchArticles {

    public static void main(String[] args) throws IOException {

        System.setProperty(CHROME_WEBDRIVER, WEBDRIVER_PATH);

        WebDriver driver = new ChromeDriver();
        driver.navigate().to(URL);

        int articlesCounter = driver.findElements(By.xpath(XPATH_ARTICLE_BOX)).size();

        LocalDate date = LocalDate.now();

        int index = 1;
        while (index < articlesCounter + 1) {

            String foundDate =
                driver.findElement(By.xpath(String.format("(%s)[%s]", XPATH_DATE, index))).getText();

            if (foundDate.equals(dateFormatter(date))) {
                driver.findElement(By.xpath(String.format("(%s)[%s]", XPATH_ARTICLE_BOX_TITLE, index))).click();
                String title = driver.findElement(By.xpath(XPATH_TITLE)).getText();
                String text = driver.findElement(By.xpath(XPATH_TEXT)).getText();
                saveFile(title, text, date);
                driver.findElement(By.xpath(XPATH_NEWS)).click();
            }
            index++;
        }

        driver.close();
    }

    private static String dateFormatter(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.ENGLISH);
        return date.format(formatter);
    }

    private static void saveFile(String title, String text, LocalDate date) throws IOException {
        File dir = new File(DIR_PATH);
        String fileName = String.format("%s - %s", date, title);
        File file = new File(dir, fileName);
        FileWriter writer = new FileWriter(file);
        writer.write(text);
        writer.close();
    }

    private static final String URL = "https://spacenews.com/segment/news/";

    private static final String CHROME_WEBDRIVER = "webdriver.chrome.driver";
    private static final String WEBDRIVER_PATH = "/Users/plotva/projects/spaceScraper/src/main/resources/webdriver"
        + "/chromedriver";

    private static final String XPATH_ARTICLE_BOX = "//div[contains(@class,'launch-article')]";
    private static final String XPATH_DATE = ".//div/time";
    private static final String XPATH_ARTICLE_BOX_TITLE = "//h2[contains(@class,'launch-title')]";
    private static final String XPATH_TEXT = "//div[contains(@class,'tablet-wrapper')]";
    private static final String XPATH_TITLE = "//h1";
    private static final String XPATH_NEWS = "//li[contains(@class,'menu')]/a[contains(@title,'News')]";

    private static final String DIR_PATH = "/Users/plotva/articles";

    private static final String DATE_FORMAT = "MMMM dd, yyyy";

}
