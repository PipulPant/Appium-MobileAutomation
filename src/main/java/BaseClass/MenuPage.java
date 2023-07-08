package BaseClass;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import pages.SettingsPage;
import utils.Log;
import utils.TestUtils;

public class MenuPage extends TestBotBase {
	public Log log=new Log(this.getClass());

	TestUtils utils = new TestUtils();
	
	@AndroidFindBy (xpath="//android.view.ViewGroup[@content-desc=\"test-Menu\"]/android.view.ViewGroup/android.widget.ImageView\n" + 
			"") 
	@iOSXCUITFindBy (xpath="//XCUIElementTypeOther[@name=\"test-Menu\"]/XCUIElementTypeOther")
	private MobileElement settingsBtn;
	
	public SettingsPage pressSettingsBtn() {
		click(settingsBtn, "Press Settings Button");
		return new SettingsPage();
	}

}
