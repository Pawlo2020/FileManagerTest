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

//Test class responsible for doing testCase 4 - get into localfiles, sort direcotries by date, compare and valid correctness of sorting
public class SortingTest {

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
	public void testCase4() {
		// Wait until local files button is visible and click
		wait.until(ExpectedConditions.visibilityOf(getMobileElement(localFiles)));
		driver.findElement((localFiles)).click();

		// Click sort button
		wait.until(ExpectedConditions.visibilityOf(getMobileElement(sortButt)));
		driver.findElementById(sortButt).click();

		// Wait and click date sort button
		wait.until(ExpectedConditions
				.visibilityOf(getMobileElement(new ByChained(By.id(sortList), By.xpath(sortDateButt)))));
		driver.findElement(new ByChained(By.id(sortList), By.xpath(sortDateButt))).click();

		// Get dates from first and second elements
		String[] date1 = getFileUpdateDate("1");
		String[] date2 = getFileUpdateDate("2");

		// Assertion - compare modification date
		assertTrue(checkModificationDate(date1, date2));

	}

	// Check modify date and compare method
	public static boolean checkModificationDate(String[] date1, String[] date2) {
		// Year
		if (Integer.parseInt(date1[2]) > Integer.parseInt(date2[2])) {
			return true;
		} else if (Integer.parseInt(date1[2]) == Integer.parseInt(date2[2])) {
			// Month
			if (Integer.parseInt(getMonth(date1[0])) > Integer.parseInt(getMonth(date2[0]))) {
				return true;
			} else if (Integer.parseInt(getMonth(date1[0])) == Integer.parseInt(getMonth(date2[0]))) {
				// Day
				if (Integer.parseInt(date1[1]) > Integer.parseInt(date2[1])) {
					return true;
				} else if (Integer.parseInt(date1[1]) == Integer.parseInt(date2[1])) {
					// Hour
					if (Integer.parseInt(date1[3].split(":")[0]) > Integer.parseInt(date2[3].split(":")[0])) {
						return true;
					} else if (Integer.parseInt(date1[3].split(":")[0]) == Integer.parseInt(date2[3].split(":")[0])) {
						// Minutes
						if (Integer.parseInt(date1[3].split(":")[1]) > Integer.parseInt(date2[3].split(":")[1])) {
							return true;
						} else if (Integer.parseInt(date1[3].split(":")[1]) == Integer
								.parseInt(date2[3].split(":")[1])) {
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// Get file modification date string from element
	public static String[] getFileUpdateDate(String index) {
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(String.format(fileDate, index)))));
		// Split
		String[] parts = driver.findElement(By.xpath(String.format(fileDate, index))).getText().split(" ");

		// cut comma
		parts[1] = parts[1].substring(0,parts[1].length()-1);
		parts[2] = parts[2].substring(0, 4);

		
		return parts;
	}

	// Get month name
	public static String getMonth(String month) {
		if (month.equals("Jan")) {
			return "01";
		} else if (month.equals("Feb")) {
			return "02";
		} else if (month.equals("Mar")) {
			return "03";
		} else if (month.equals("Apr")) {
			return "04";
		} else if (month.equals("May")) {
			return "05";
		} else if (month.equals("Jun")) {
			return "06";
		} else if (month.equals("Jul")) {
			return "07";
		} else if (month.equals("Aug")) {
			return "08";
		} else if (month.equals("Sep")) {
			return "09";
		} else if (month.equals("Nov")) {
			return "10";
		} else if (month.equals("Oct")) {
			return "11";
		} else if (month.equals("Dec")) {
			return "12";
		}
		return null;
	}

	// Return to the dashboard method
	public static void navigateToDashboard() {
		driver.startActivity(new Activity("com.mobisystems.fileman", "com.mobisystems.files.FileBrowser"));
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

	// Method finds folder with specific name and returs it as MobileElement
	public static MobileElement getFolder(String name) {
		if (driver.findElements(By.xpath(String.format(directory_fileTitle, name))).isEmpty()) {
			return null;
		}

		return driver.findElement(By.xpath(String.format(directory_fileTitle, name)));
	}

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
