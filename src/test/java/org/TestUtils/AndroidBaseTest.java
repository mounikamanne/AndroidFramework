package org.TestUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import org.pageObjects.android.FormPage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.utils.AppiumUtils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class AndroidBaseTest extends AppiumUtils {

	public AndroidDriver driver;
	public AppiumDriverLocalService localService;
	AppiumServiceBuilder serviceBuilder;
	public FormPage formPage;

	@BeforeClass(alwaysRun = true)
	public void configureAppium() {
		Properties prop = new Properties();

		try {
			// Load properties file
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir") + "/src/main/java/org/resources/data.properties");
			prop.load(fis);

			// Fetch IP address and port from properties or system properties
			String ipAddress = System.getProperty("ipAddress") != null ? System.getProperty("ipAddress")
					: prop.getProperty("ipAddress");
			String port = System.getProperty("port") != null ? System.getProperty("port") : prop.getProperty("port");

			// Start Appium server
			serviceBuilder = new AppiumServiceBuilder().withIPAddress(ipAddress).usingPort(Integer.parseInt(port));
			localService = AppiumDriverLocalService.buildService(serviceBuilder);
			localService.start();

			System.out.println("Appium server started successfully on IP: " + ipAddress + " and Port: " + port);

			// Set UiAutomator2 options
			UiAutomator2Options options = new UiAutomator2Options();
			options.setDeviceName(prop.getProperty("AndroidDeviceNames")); // Device name from properties
			options.setChromedriverExecutable(System.getProperty("user.dir") + "//Drivers//chromedriver.exe"); 
			options.setApp(System.getProperty("user.dir") + "//APK//General-Store.apk"); // APK path

			// Initialize AndroidDriver
			driver = new AndroidDriver(localService.getUrl(), options);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

			// Initialize page objects
//			formPage = new FormPage(driver);
			restartApp();

		} catch (IOException e) {
			System.err.println("Failed to load properties file: " + e.getMessage());
			throw new RuntimeException("Properties file loading failed");
		} catch (Exception e) {
			System.err.println("Failed to start Appium or initialize driver: " + e.getMessage());
			throw new RuntimeException("Appium configuration failed");
		}
	}
	 private void restartApp() {
	        try {
	            System.out.println("Restarting app...");
	            driver.terminateApp("com.androidsample.generalstore"); // Replace with your app's package name
	            driver.activateApp("com.androidsample.generalstore"); // Replace with your app's package name
	            Thread.sleep(5000); // Wait for the app to load
	        } catch (Exception e) {
	            System.err.println("Failed to restart app: " + e.getMessage());
	            throw new RuntimeException("App restart failed");
	        }
	    }

	@AfterClass(alwaysRun = true)
	public void tearDown() {
		if (driver != null) {
			driver.quit();
			System.out.println("Driver quit successfully");
		}
		if (localService != null && localService.isRunning()) {
			localService.stop();
			System.out.println("Appium server stopped successfully");
		}
	}
}
