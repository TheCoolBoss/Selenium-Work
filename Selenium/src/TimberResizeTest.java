import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class TimberResizeTest extends TimberTest
{
    public TimberResizeTest(WebDriver wd, WebDriverWait wait, String user, String pass, String designUrl)
    {
        super(wd, wait, user, pass, designUrl);
    }  

    public void doTests()
    {
        //This is a Menu Legal landscapep
        setSize("//*[@id='app-header']/nav[1]/ul/li[2]/div/div[2]/div[2]/ul/li[1]/ul/li[2]", 
        true, 
        false);

        //Custom 5x5 in
        setCustomSize("5", "5", true);

        //Custom 1500 x 1000 px
        setCustomSize("1500", "1000", false);

        //Reset to default size
        //Not really necessary, but I prefer using that size; not too big or small
        setSize("//*[@id='app-header']/nav[1]/ul/li[2]/div/div[2]/div[2]/ul/li[1]/ul/li[1]",
        true,
        true);
    }

    public void setSize(String newXpath, Boolean hasOrientation, Boolean isPortrait)
    {
        openResize();
        JavascriptExecutor jse = (JavascriptExecutor) wd;

        WebElement newSize = wd.findElement(By.xpath(newXpath));
        waitInSecs(2);
        jse.executeScript("arguments[0].scrollIntoView(true)", newSize);
        jse.executeScript("arguments[0].click()", newSize);

        String newType = wd.findElement(By.xpath(newXpath + "/span[1]")).getText();
        String newDims = wd.findElement(By.xpath(newXpath + "/span[2]")).getText();
        System.out.println("Resizing to " + newType + " " + newDims);

        if (hasOrientation)
        {
            WebElement newOrientation;
            if (isPortrait)
            {
                newOrientation = wd.findElement(By.xpath(newXpath + "/ul/li[1]/span"));
            }
            else
            {
                newOrientation = wd.findElement(By.xpath(newXpath + "/ul/li[2]/span"));
            }
            
            jse.executeScript("arguments[0].scrollIntoView(true)", newOrientation);
            jse.executeScript("arguments[0].click()", newOrientation);
        }

        confirmResize();

        assertResize(newType, newDims);
        System.out.println("Resized to " + newType + " " + newDims);
    }

    public void setCustomSize(String width, String height, Boolean inInches)
    {
        System.out.print("Resizing to " + width + " x " + height);
        if (inInches)
        {
            System.out.println(" inches");
        }

        else
        {
            System.out.println(" pixels");
        }

        openResize();

        WebElement customWidth = wd.findElement(By.cssSelector("input[placeholder='Width']"));
        JavascriptExecutor jse = (JavascriptExecutor) wd;
        
        jse.executeScript("arguments[0].scrollIntoView(true)", customWidth);
        jse.executeScript("arguments[0].click()", customWidth);

        //wait.until(ExpectedConditions.intera)
        WebElement customHeight = wd.findElement(By.cssSelector("input[placeholder='Height']"));

        customWidth.sendKeys(width);
        customHeight.sendKeys(height);

        //Unit button should be visible at this point, so skip scrolling
        if (!inInches)
        {
            WebElement unitButton = wd.findElement(By.xpath("//*[@id='app-header']/nav[1]/ul/li[2]/div/div[2]/div[2]/ul/li[11]/ul/li[1]/select"));
            unitButton.click();
            WebElement pixelButton = wd.findElement(By.cssSelector("option[value='px']"));
            pixelButton.click();
        }

        confirmResize();
        assertResize("custom", width + " x " + height);

        System.out.print("Resized to " + width + " x " + height);
        if (inInches)
        {
            System.out.println(" inches");
        }

        else
        {
            System.out.println(" pixels");
        }
    }



    public void openResize()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='app-header']/nav[1]/ul/li[2]/div/div[1]/span")));
        WebElement resizeButton = wd.findElement(By.xpath("//*[@id='app-header']/nav[1]/ul/li[2]/div/div[1]/span"));
        resizeButton.click();
    }

    public void confirmResize()
    {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div[3]/header/nav[1]/ul/li[2]/div/div[2]/div[3]/a[1]")));
        WebElement confirm = wd.findElement(By.xpath("/html/body/div[1]/div[3]/header/nav[1]/ul/li[2]/div/div[2]/div[3]/a[1]"));
        confirm.click();
    }

    public void assertResize(String menuType, String dimensions)
    {
        openResize();
        WebElement currentSize = wd.findElement(By.cssSelector("input[class='search-sizes']"));
        assert currentSize.getText().contains(menuType) : "Wrong menu type :(";
        assert currentSize.getText().contains(dimensions) : "Wrong size in use :(";

        //Need to close the menu by clicking it again
        openResize();
    }
}
