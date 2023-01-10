import java.time.Duration;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.WebDriverWait;



public class Main 
{
    public static void main(String[] args) 
    {
        System.setProperty("webdriver.chrome.driver","Driver/chromedriver.exe");
        WebDriver wd = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(wd, Duration.ofSeconds(15));
        wd.manage().window().maximize();

        String user = "stephen.nguyen@musthavemenus.com";
        String pass = "mhmfun123";
        String design = "https://www.musthavemenus.com/xstage/design?id=872f4f7b-13ee-4688-8932-292fb15cf96f";

        //MainAppTest mainApp = new MainAppTest(wd, wait, user, "wrongPassHere", pass, 
        //"https://staging.mhmfun.com", "https://staging.mhmfun.com/account/login");
        //mainApp.testExistingAccount(); 

        //Strings are currently arbitrary
        //mainApp.makeNewAccount(wd, "https://staging.mhmfun.com/account/register", "Mission", "Control", 
        //"Abyss Bar", "1234567890", "12345", "something@mhmfun.com", pass);
        TimberTest timber = new TimberTest(wd, wait, user, pass, design);
        timber.navToTimber();
        timber.startTests();
        wd.quit();
    } 

    // @After
    // public void tearDown()
    // {
    //     wd.quit();
    // }
}
