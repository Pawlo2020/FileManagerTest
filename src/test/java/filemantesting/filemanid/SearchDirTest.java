package filemantesting.filemanid;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.pagefactory.ByChained;

//Test class responsible for doing testCase 2 - search specific directory
public class SearchDirTest {

	// Fields
	static AndroidDriver<MobileElement> driver;
	static WebDriverWait wait;

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
	public void testCase2() {

		// Wait until local files button is visible and click
		wait.until(ExpectedConditions.visibilityOf(getMobileElement(localFiles)));
		driver.findElement((localFiles)).click();

		// Call search method and find 'Android' directory
		search("Android");

		// Wait for Android directory visibility
		wait.until(ExpectedConditions.visibilityOf(getFolder("Android")));

		// Select directory 'Android'
		selectFolder("Android");

		// Assertion - check if directory exists
		assertTrue(checkFolderExist("Android"));

		// Navigate to the dashboard
		navigateToDashboard();
	}

	// Return to the dashboard method
	public static void navigateToDashboard() {
		driver.startActivity(new Activity("com.mobisystems.fileman", "com.mobisystems.files.FileBrowser"));
	}

	// Find directory and click
	public static void selectFolder(String name) {
		driver.findElement(By.xpath(String.format(directory_fileIcon, name))).click();
	}

	// Method responsible for clicking a directory
	public void search(String name) {

		// Find and click copy button
		wait.until(ExpectedConditions.visibilityOf(getMobileElement(searchButt_copyButt)));
		driver.findElementById(searchButt_copyButt).click();

		// Wait for searchBar and set value on it
		wait.until(ExpectedConditions.visibilityOf(getMobileElement(searchBar)));
		driver.findElementById(searchBar).setValue(name);
	}

	// Method that checks if folder exists
	public static boolean checkFolderExist(String name) {
		if (driver.findElements(By.xpath(String.format(directory_fileTitle, name))).isEmpty()) {
			driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
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

	public static MobileElement getFolder(String name) {
		if (driver.findElements(By.xpath(String.format(directory_fileTitle, name))).isEmpty()) {
			return null;
		}

		return driver.findElement(By.xpath(String.format(directory_fileTitle, name)));
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

	public void performTutorial() throws InterruptedException {
		driver.findElementById(continueBtn).click();

		wait.until(ExpectedConditions.visibilityOf(getMobileElement(continueWithAds))).click();

		wait.until(ExpectedConditions.visibilityOf(getMobileElement(androidDriveAllow))).click();

		navigateToDashboard();

		wait.until(ExpectedConditions.visibilityOf(getMobileElement(rogerThatBtn))).click();
	}
}
