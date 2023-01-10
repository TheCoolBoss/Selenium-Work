import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TimberTest
{
    WebDriver wd;
    WebDriverWait wait;
    String user;
    String pass;
    String designUrl;

    public TimberTest(WebDriver wd, WebDriverWait wait, String user, String pass, String designUrl)
    {
        this.wd = wd;
        this.wait = wait;
        this.user = user;
        this.pass = pass;
        this.designUrl = designUrl;
    }

    public void navToTimber()
    {        
        wd.get(this.designUrl);

        //Since no login has happened yet, do so here
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='email']")));
        WebElement user = wd.findElement(By.cssSelector("input[type='email']"));
        WebElement pass = wd.findElement(By.cssSelector("input[type='password']"));

        user.sendKeys(this.user);
        pass.sendKeys(this.pass);
        pass.sendKeys(Keys.RETURN);
        
        wait.until(ExpectedConditions.urlContains("home"));

        //With login done, design should be visible now
        //Not terribly concerned about the need to login to staging url yet
        wd.get(this.designUrl);
    }

    public void startTests()
    {
        wait.until(wd -> ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
        waitInSecs(3);

        TimberResizeTest resizes = new TimberResizeTest(this.wd, this.wait, this.user, this.pass, this.designUrl);
        //resizes.doTests();

        TimberOptionsTest options = new TimberOptionsTest(this.wd, this.wait, this.user, this.pass, this.designUrl);
        options.doTests();
    }


    public void testText(WebDriver wd)
    {
        WebElement textMenu = wd.findElement(By.linkText("Text"));
        textMenu.click();
        WebElement textButton = wd.findElement(By.linkText("+ Add Text"));
        textButton.click();
        WebElement header = wd.findElement(By.linkText("Header Text"));
        header.click();

        //Font
        String fontToTest = "ABeeZee";
        WebElement textElement = wd.findElement(By.xpath("//*[contains(text(),'Add header')]"));
        String fontType = textElement.getCssValue("font-family");
        WebElement fontPicker = wd.findElement(By.linkText(fontType));
        fontPicker.click();
        WebElement newFont = wd.findElement(By.linkText(fontToTest));
        newFont.click();
        assert textElement.getCssValue("font-family").equals(fontToTest) : "Font didn't change :(";

        WebElement fontSizePicker = wd.findElement(By.className("dropdown-toggle car sm"));


    }

    public void testMenu()
    {

    }




    public void navToGraphics(WebDriver wd, String type)
    {
        WebElement graphicButton = wd.findElement(By.linkText("Graphics"));
        graphicButton.click();
        WebElement typeButton = wd.findElement(By.linkText(type));
        typeButton.click();
    }

    public void testPhoto(WebDriver wd)
    {
        navToGraphics(wd, "Free Stock Photos");
    }

    public void testIllus(WebDriver wd)
    {
        navToGraphics(wd, "Illustrations");
    }

    public void testShape(WebDriver wd)
    {
        navToGraphics(wd, "Shapes");
    }

    public void testLogo(WebDriver wd)
    {
        navToGraphics(wd, "My Logos");
    }

    public void testUpload(WebDriver wd)
    {   
        navToGraphics(wd, "My Uploads");
    }

    public void testQr()
    {

    }



    public void waitInSecs(int seconds)
    {
        try
        {
            Thread.sleep(seconds * 1000);
        }
        catch (InterruptedException ie)
        {
            System.out.println("Interrupted e");
        }
    }
}
