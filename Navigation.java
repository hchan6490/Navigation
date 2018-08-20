package pageobjects;

import java.net.URI;
import java.net.URISyntaxException;
import org.openqa.selenium.WebDriver;
import com.provar.core.testapi.annotations.*;

@SalesforcePage(connection = "HCHANQA", title = "Navigation")
public class Navigation {
	WebDriver driver;
	
	public Navigation (WebDriver driver) {
		this.driver = driver;
	}

    // ===== Breakdown the current URL and use its pieces to build a new URL (Salesforce Classic only) =====
    public void navigateToVisualforcePage(String targetPageName, String newParams, Boolean maintainCurrentParams) throws URISyntaxException {
        String url = driver.getCurrentUrl();
        URI currentUri = new URI(url);

        // ===== Breakdown the current URL =====
        // Example URL:
        // "https://my-domain.salesforce.com/apex/namespace__CurrentPage?param1=value1&param2=value2"
        String uriScheme = currentUri.getScheme();       // "https"
        String uriAuthority = currentUri.getAuthority(); // "my-domain.salesforce.com"
        String currentPath = currentUri.getPath();       // "/apex/namespace__CurrentPage"
        String currentParams = currentUri.getQuery();    // "param1=value1&param2=value2"

        // ===== Construct Path =====
        // "/apex/namespace__TargetPage"
        String currentPageName = currentPath;
        String finalPath = handleNewPath(currentPageName, targetPageName);

        // ===== Construct URL Parameters =====
        // "param1=value1&param2=value2&param3=value3"
        String finalParams = handleParams(currentParams, newParams, maintainCurrentParams);
        //String finalParams = newParams;

        // ===== Reconstruct the final URL =====
        String finalUrl = uriScheme + "://" + uriAuthority + finalPath + "?" + finalParams;
        driver.get(finalUrl);
    }
    
    private String handleNewPath(String currentPageName, String targetPageName) {
        String newPath = "/apex/";
        String finalPath = currentPageName;
        if (targetPageName != null && targetPageName != "") {
            finalPath = newPath + targetPageName;
        }
        return finalPath;
    }

    private String handleParams(String currentParams, String newParams, Boolean maintainCurrentParams) {
        String finalParams = "";
        if (maintainCurrentParams) {
        	finalParams = currentParams + newParams;
        } else if (newParams != null && newParams != "") {
            finalParams = newParams;
        }
        return finalParams;
    }
}
