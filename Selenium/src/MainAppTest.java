import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;


public class MainAppTest
{
    WebDriver driver;
    WebDriverWait wait;
    String user;
    String wrongPass;
    String rightPass;
    String homeUrl;
    String loginUrl;
    

    public MainAppTest(WebDriver wd, WebDriverWait wait, String user, String wrongPass, String rightPass, String homeUrl, String loginUrl)
    {
        this.driver = wd;
        this.user = user;
        this.wrongPass = wrongPass;
        this.rightPass = rightPass;
        this.homeUrl = homeUrl;
        this.wait = wait;
        this.loginUrl = loginUrl;
    }

    public void goToHome()
    {
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        this.driver.get(this.homeUrl);
    }

    
    public void makeNewAccount(WebDriver wd, String regPage, String firstName, String lastName, String biz, 
    String phoneNum, String zipCode, String newEmail, String newPass)
    {
        goToHome();
        WebElement regButton = wd.findElement(By.className("visitorOptionSignup"));
        regButton.click();
        assert wd.getCurrentUrl().equals(regPage) : "Not at reg page :(";

        WebElement accountOption = wd.findElement(By.xpath("/html/body/div[1]/div/main/div/div/div[2]/ul/li[1]/button"));
        accountOption.click();

        String currentWin = wd.getWindowHandle();
        WebElement firstNameInput = wd.findElement(By.cssSelector("input[placeholder='First Name']"));
        firstNameInput.sendKeys(firstName);
        WebElement lastNameInput = wd.findElement(By.cssSelector("input[placeholder='Last Name']"));
        lastNameInput.sendKeys(lastName);
        WebElement bizName = wd.findElement(By.cssSelector("input[placeholder='Restaurant or Business Name']"));
        bizName.sendKeys(biz);
        WebElement phone = wd.findElement(By.cssSelector("input[placeholder='Phone Number']"));
        phone.sendKeys(phoneNum);
        WebElement zip = wd.findElement(By.cssSelector("input[placeholder='Restaurant Zip Code']"));
        zip.sendKeys(zipCode);
        WebElement emailButton = wd.findElement(By.cssSelector("input[placeholder='Email']"));
        emailButton.sendKeys(newEmail);
        WebElement passButton = wd.findElement(By.cssSelector("input[placeholder='Password']"));
        passButton.sendKeys(newPass);

        //Wait bc of captcha
        //https://stackoverflow.com/questions/55264221/how-to-click-on-the-recaptcha-using-selenium-and-java/55265044#55265044
        WebDriverWait cap =  new WebDriverWait(wd, Duration.ofSeconds(10));
        cap.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.xpath("//iframe[starts-with(@name, 'a-') and starts-with(@src, 'https://www.google.com/recaptcha')]")));
        
        WebElement check = cap.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.recaptcha-checkbox-border")));
        check.click();
        
        //Not sure how to get past the picture part, so adding a delay to solve manually
        waitInSecs(10);

        wd.switchTo().window(currentWin);
        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[type='button']")));
        WebElement nextButton = wd.findElement(By.cssSelector("button[type='button']"));
        while(nextButton.isDisplayed())
        {
            nextButton.click();
        }

        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("name-badge"))); 
        String newUrl = wd.getCurrentUrl();
        assert newUrl.contains("staging.mhmfun.com") : "Wrong redirect :(";
        waitInSecs(10);
    }


    public void testExistingAccount() 
    {
        goToHome();

        login(this.driver, this.user, this.wrongPass, this.rightPass, this.homeUrl, this.wait);

        waitInSecs(3);
        
        //Sometimes, login redirects to a webpage version
        if (this.driver.getCurrentUrl().equals("https://staging.mhmfun.com/menu/login.do"))
        {
            System.out.println("Second attempt");
            secondLogin(this.driver, this.wait, this.user, this.rightPass);
        }
        //testDesigns(this.driver, this.wait);
        waitInSecs(3);
        logout(this.driver, this.wait, this.homeUrl);
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


    public void login(WebDriver wd, String user, String wrongPass, String rightPass, String loginUrl, WebDriverWait wait)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Log In")));
        WebElement login = wd.findElement(By.linkText("Log In"));
        login.click();
        waitInSecs(3);

        WebElement username = wd.findElement(By.cssSelector("input[type='email']"));
        WebElement pass = wd.findElement(By.cssSelector("input[type='password']"));
        String wrongURL = wd.getCurrentUrl();
        username.sendKeys(user);
        pass.sendKeys("hi");

        waitInSecs(1);
        WebElement loginButton = wd.findElement(By.cssSelector("button[type='button']"));
        loginButton.click();
        waitInSecs(3);
        assert wrongURL.equals(loginUrl) : " Wrong url";

        
        clearElement(pass);
        pass.sendKeys(rightPass);
        loginButton = wd.findElement(By.cssSelector("button[type='button']"));
        loginButton.click();
        String rightURL = wd.getCurrentUrl();
        assert rightURL.equals(homeUrl) : " Not home url";
    }


    public void secondLogin(WebDriver wd, WebDriverWait wait, String username, String password)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("value(login)")));
        WebElement user = wd.findElement(By.name("value(login)"));
        WebElement pass = wd.findElement(By.name("value(password)"));
        WebElement submit = wd.findElement(By.name("value(submit)"));
        user.sendKeys(username);
        pass.sendKeys(password);
        submit.click();
    }



    public void testDesigns(WebDriver wd, WebDriverWait wait)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("designs")));
        WebElement designList = wd.findElement(By.className("designs"));
        List<WebElement> designCount = designList.findElements(By.xpath("//li"));

        if (designCount.size() != 0)
        {
            WebElement firstDesign = wd.findElement(By.className("design-card"));
            Actions hover = new Actions(wd);
            hover.moveToElement(firstDesign).moveToElement(wd.findElement(By.cssSelector(".mobile-design-menu.more-btn-ops"))).click().build().perform();;
        }
        else
        {
            //do stuff
        }
    }


    public void logout(WebDriver wd, WebDriverWait wait, String homeUrl)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("name-badge"))); 
        WebElement userBadge = this.driver.findElement(By.className("name-badge"));
        userBadge.click();

        WebElement logout = this.driver.findElement(By.linkText("Sign Out"));
        logout.click();
        String lastUrl = wd.getCurrentUrl();
        assert lastUrl.equals(homeUrl) : " Not home url";

        //this.driver.quit();
    }



    public void clearElement(WebElement we)
    {
        while(!we.getAttribute("value").equals(""))
        {
            we.sendKeys(Keys.BACK_SPACE);
        }
    }
}
