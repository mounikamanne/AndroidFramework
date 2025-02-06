package org.testSuite;

import org.TestUtils.AndroidBaseTest;
import org.pageObjects.android.FormPage;
import org.testng.annotations.Test;

public class GeneralStore_1 extends AndroidBaseTest {

	@Test(groups="smoke")
	public void GeneralStoretest() throws InterruptedException {
// Fill out the form
		FormPage fm = new FormPage(driver);
		fm.setNameField("mounika");
		fm.setGender("male");
		fm.setCountrySelection("Argentina");
		fm.submitForm();


	}
}
