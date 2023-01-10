import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;

public class TimberOptionsTest extends TimberTest
{
    public TimberOptionsTest(WebDriver wd, WebDriverWait wait, String user, String pass, String designUrl)
    {
        super(wd, wait, user, pass, designUrl);
    }

    public void doTests()
    {
        //testNewDesign();
        //fullyLoadPage();
        //testCopy();
        //fullyLoadPage();
        testExit();
    }


    public void testNewDesign()
    {
        openOptions();
        WebElement newDesign = wd.findElement(By.cssSelector("a[data-menu='#new-design-dropdown']"));
        newDesign.click();

        WebElement menuButton = wd.findElement(By.xpath("//*[@id='new-design-dropdown']/div[2]/ul/li[1]/a"));
        menuButton.click();
        
        skipTemplatePrompt();

        assertNewDesign();
    }

    public void testCopy()
    {
        openOptions();
        WebElement copyDesign = wd.findElement(By.xpath("//*[@id='app-header']/nav[1]/ul/li[1]/div/div[1]/ul/li[2]/a"));
        copyDesign.click();
        WebElement copyName = wd.findElement(By.cssSelector("input[placeholder='Design name...']"));
        copyName.sendKeys("For Stage Copy");

        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Save a copy")));
        WebElement confirmCopy = wd.findElement(By.linkText("Save a copy"));
        confirmCopy.click();

        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Ok")));
        WebElement okButton = wd.findElement(By.linkText("Ok"));
        okButton.click();

        //Not quite sure how to assert a copy yet, but not major concern
        assertNewDesign();
    }

    public void testExit()
    {
        openOptions();
        WebElement exitButton = wd.findElement(By.xpath("//*[@id='app-header']/nav[1]/ul/li[1]/div/div[1]/ul/li[4]/a"));
        exitButton.click();
        waitInSecs(1);
        handleAlerts();

        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='email']")));
        assert wd.getCurrentUrl().contains("https://staging.mhmfun.com/account/login") : "Still on design :(";
        wd.get(designUrl);
    }

    public void openOptions()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='app-header']/nav[1]/ul/li[1]/div/a")));
        WebElement fileButton = wd.findElement(By.xpath("//*[@id='app-header']/nav[1]/ul/li[1]/div/a"));
        fileButton.click();
    }

    public void skipTemplatePrompt()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated((By.linkText("Nope"))));
        WebElement noTemplate = wd.findElement(By.linkText("Nope"));
        noTemplate.click();
    }

    public void assertNewDesign()
    {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[class='dropdown-toggle']")));
        assert !designUrl.equals(wd.getCurrentUrl()) : "Still on old design :(";
        
        //Go back to original design to continue tests
        wd.get(designUrl);
    }

    public void fullyLoadPage()
    {
        wait.until(wd -> ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }

    public void handleAlerts()
    {
        waitInSecs(1);
        try
        {
            //wait.until(ExpectedConditions.alertIsPresent());
            Alert leavePage = wd.switchTo().alert();
            leavePage.accept();
        }
        catch (NoAlertPresentException nap)
        {
            System.out.println("No alert here");
            //Do nothing and continue with tests
        }
    }
}
