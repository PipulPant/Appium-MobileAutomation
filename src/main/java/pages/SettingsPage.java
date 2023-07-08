package pages;

import BaseClass.TestBotBase;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import utils.TestUtils;

public class SettingsPage extends TestBotBase {
	TestUtils utils = new TestUtils();
	
	@AndroidFindBy (accessibility="test-LOGOUT") 
	@iOSXCUITFindBy (id = "test-LOGOUT")
	private MobileElement logoutBtn;
	
	public LoginPage pressLogoutBtn() {
		click(logoutBtn, "press Logout button");
		return new LoginPage();
	}

}
