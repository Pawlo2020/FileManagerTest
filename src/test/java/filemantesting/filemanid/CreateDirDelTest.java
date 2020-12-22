package filemantesting.filemanid;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.events.EventFiringWebDriverFactory;
import io.appium.java_client.events.api.general.ElementEventListener;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.pagefactory.ByChained;

//Listener class for driver
class ElementListener implements ElementEventListener {

	AndroidDriver<MobileElement> aDriver;

	// Get driver handler
	public AndroidDriver<MobileElement> getaDriver() {
		return aDriver;
	}

	// Set driver handler
	public void setaDriver(AndroidDriver<MobileElement> aDriver) {
		this.aDriver = aDriver;
	}

	// Fields - string id elements for catching premium popup
	static String letsBeginBtn = "com.mobisystems.fileman:id/go_premium_button";
	static String okButton = "com.mobisystems.fileman:id/go_premium_button";
	static String art = "com.mobisystems.fileman:id/artwork";
	static String header = "com.mobisystems.fileman:id/go_premium_upgrade_header";
	static String point1 = "com.mobisystems.fileman:id/go_premium_point1";
	static String point2 = "com.mobisystems.fileman:id/go_premium_point2";
	static String point3 = "com.mobisystems.fileman:id/go_premium_point3";
	static String point4 = "com.mobisystems.fileman:id/go_premium_point4";

	public void beforeClickOn(WebElement element, WebDriver driver) {

	}

	// After click event
	public void afterClickOn(WebElement element, WebDriver driver) {

		// Check premium button
		if (!aDriver.findElementsById(letsBeginBtn).isEmpty()) {

			// Navigate back to hide premium frame
			aDriver.navigate().back();
		}
	}

	public void beforeChangeValueOf(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub

	}

	public void afterChangeValueOf(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub

	}

	public void beforeGetText(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	public void afterGetText(WebElement element, WebDriver driver, String text) {
		// TODO Auto-generated method stub

	}

}

//Test class responsible for doing testCase 1 - Create and delete directory then check and valid
public class CreateDirDelTest {

	// Fields
	static AndroidDriver<MobileElement> driver;
	static WebDriverWait wait;
	static ElementListener listener;

	// Before clause - init Android driver and assign listener. Connect to appium
	// server
	@Before
	public void testInit() throws MalformedURLException, InterruptedException {
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
		cap.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");

		cap.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.mobisystems.fileman");
		cap.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.mobisystems.files.FileBrowser");
		cap.setCapability("noReset", "true");
		cap.setCapability("fullReset", "false");

		driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), cap);

		listener = new ElementListener();
		listener.setaDriver(driver);

		driver = EventFiringWebDriverFactory.getEventFiringWebDriver(driver, listener);

		// Implicit wait for driver
		driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 2);

		// Check visibility of intro
		if (!(driver.findElementsById(continueBtn).isEmpty())) {

			// Perform tutorial - for first boot only
			performTutorial();
		}

		// Check visibility of 'Got it' button
		if (!(driver.findElementsById(rogerThatBtn).isEmpty())) {

			// Click 'Got it' button
			driver.findElementById(rogerThatBtn).click();
		}

	}

	// Test case method
	@Test
	public void testCase1() throws InterruptedException {

		// Wait until local files button is visible and click
		wait.until(ExpectedConditions.visibilityOf(getMobileElement(localFiles)));
		driver.findElement((localFiles)).click();

		// Assertion - Create folder named 'TestDirectory'
		assertTrue(createFolder("TestDirectory", false));

		// Wait until visibility icon of the previously created directory
		wait.until(ExpectedConditions.visibilityOf(getFolders(globalDirectory_fileIcon)));
		assertFalse(deleteFolder("TestDirectory"));

		// Return to the dashboard
		navigateToDashboard();
	}

	// Return to the dashboard method
	public static void navigateToDashboard() {

		// Start activity returning to the dashboard
		driver.startActivity(new Activity("com.mobisystems.fileman", "com.mobisystems.files.FileBrowser"));
	}

	// Method responsible for creating folder
	public static boolean createFolder(String name, boolean alternate) {
		// In case of normal mode - click new folder button
		if (!alternate) {

			// Wait and click for new folder button
			wait.until(ExpectedConditions.visibilityOf(getMobileElement("com.mobisystems.fileman:id/menu_new_folder")));
			driver.findElementById("com.mobisystems.fileman:id/menu_new_folder").click();

			// Wait until visibility folder name text area
			wait.until(ExpectedConditions.visibilityOf(getMobileElement(newTextview)));
			driver.findElementById(newTextview).clear();

			// Type name and click ok
			driver.findElementById(newTextview).sendKeys(name);
			driver.findElementById(okButton).click();

			return checkFolderExist(name);
		} else {

			// Click more options
			wait.until(ExpectedConditions.visibilityOf(getMobileElement(altthreedots)));
			driver.findElement(altthreedots).click();

			wait.until(ExpectedConditions.visibilityOf(getMobileElement(altnewfolderButt)));
			driver.findElement(altnewfolderButt).click();

			// Clear textarea
			wait.until(ExpectedConditions.visibilityOf(getMobileElement(newTextview)));
			driver.findElementById(newTextview).clear();
			driver.findElementById(newTextview).sendKeys(name);

			driver.findElementById(okButton).click();

			// Check if folder exists
			return checkFolderExist(name);
		}
	}

	// Method responsible for deleting a folder
	public static boolean deleteFolder(String name) {
		// Click directory file icon
		driver.findElement(By.xpath(String.format(directory_fileIcon, name))).click();

		// Click more options for element and delete
		driver.findElement(By.id(threedots)).click();
		driver.findElement(By.id(delButt)).click();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		listener.getaDriver().manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.findElementById(okButton).click();

		// Check if folder exists
		return checkFolderExist(name);
	}

	// Method that checks if folder exists
	public static boolean checkFolderExist(String name) {
		// Check if list of elements is empty
		if (driver.findElements(By.xpath(String.format(directory_fileTitle, name))).isEmpty()) {
			driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
			listener.getaDriver().manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
			return false;
		}

		return true;
	}

	// Get mobile element by xpath
	public static MobileElement getMobileElement(By xPath) {
		return driver.findElement(xPath);
	}

	// Get mobile element by id
	public static MobileElement getMobileElement(String id) {
		return driver.findElementById(id);
	}

	// Get mobile element by chain
	public static MobileElement getMobileElement(ByChained chain) {
		return driver.findElement(chain);
	}

	// Search specific folder in list of folders
	public static MobileElement getFolders(String path) {
		if (driver.findElements(By.xpath(path)).isEmpty()) {
			return null;
		}
		return driver.findElement(By.xpath(path));
	}

	// Fields - string xpathes and id's
	static By localFiles = By.xpath(
			"/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/androidx.drawerlayout.widget.DrawerLayout/android.view.ViewGroup/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.view.ViewGroup[2]/android.widget.ScrollView/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.widget.RelativeLayout[2]/android.widget.FrameLayout[1]/android.widget.RelativeLayout");
	static String newTextview = "com.mobisystems.fileman:id/new_name";
	static String okButton = "android:id/button1";
	static String directory_fileTitle = "//android.widget.TextView[@text='%s']";
	static String globalDirectory_fileIcon = "//android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.ImageView";
	static String directory_fileIcon = "//android.widget.RelativeLayout[@content-desc='%s']/android.widget.FrameLayout/android.widget.ImageView";
	static String threedots = "com.mobisystems.fileman:id/menu_new_folder";
	static String delButt = "com.mobisystems.fileman:id/menu_delete";
	static String searchButt_copyButt = "com.mobisystems.fileman:id/menu_find";
	static String searchBar = "com.mobisystems.fileman:id/searchTextToolbar";
	static String pasteButt = "com.mobisystems.fileman:id/menu_paste";
	static By altnewfolderButt = By.xpath(
			"/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ListView/android.widget.LinearLayout[2]/android.widget.LinearLayout");
	static By altthreedots = By.xpath("//android.widget.ImageView[@content-desc='More options']");
	static String fileDate = "//android.widget.RelativeLayout[%s]/android.widget.RelativeLayout/android.widget.TextView[2]";
	static String sortButt = "com.mobisystems.fileman:id/menu_sort";
	static String sortDateButt = "//android.widget.LinearLayout[2]/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout[4]";
	static String sortList = "com.mobisystems.fileman:id/ribbons_list";

	// Tutorial variables
	static String continueBtn = "com.mobisystems.fileman:id/continue_btn";
	static String continueWithAds = "com.mobisystems.fileman:id/continue_with_ads_btn";
	static String androidDriveAllow = "com.android.packageinstaller:id/permission_allow_button";
	static String rogerThatBtn = "com.mobisystems.fileman:id/hint_action_button";
	static String letsBeginBtn = "com.mobisystems.fileman:id/go_premium_button";
	static String trailPanel = "com.mobisystems.fileman:id/fab_bottom_popup_container";
	static String androidContent = "android:id/content";

	// Perform tutorial methos - first boot only and if not performed earlier
	public void performTutorial() throws InterruptedException {

		// Find continue button and click
		driver.findElementById(continueBtn).click();

		// Find contunue with ads button and click
		wait.until(ExpectedConditions.visibilityOf(getMobileElement(continueWithAds))).click();

		// Find allow permission button and click
		wait.until(ExpectedConditions.visibilityOf(getMobileElement(androidDriveAllow))).click();

		// Navigare to dashboard
		navigateToDashboard();

		// Check 'Got it' button and click
		wait.until(ExpectedConditions.visibilityOf(getMobileElement(rogerThatBtn))).click();
	}

}
