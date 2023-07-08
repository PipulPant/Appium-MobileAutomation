package utils;

import org.testng.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Assert verification Class
 * @author Pipul Pant
 *
 */
public class Assertion {
	private static Log log=new Log(Assertion.class);
	//断言成功日志内容
	private static  void AssertPassLog(String driverName){
		log.info("设备： "+driverName+" "+"【Assert验证:pass!】");
	}
	//断言失败日志内容
	private static  void AssertFailedLog(String driverName){
		log.error("设备： "+driverName+" "+"【Assert验证:failed!】");
	}
	public static String formatDate(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HHmmssSSS");
		return formatter.format(date).toString();
	}
	
	public static void Verity(String checkWay,String actualText,String exceptText,String driverName,String sdkVersion){
		if(checkWay.equals("textVerification")){
			VerityText(actualText,exceptText,driverName,sdkVersion);
		}
	}
	
	/**
	 * 验证某元素文本值是否与预期值exceptText一样
	 * @param exceptText 预期文本值
	 */
	public static void VerityText(String actualText,String exceptText,String driverName,String sdkVersion){
		String verityStr= "设备： "+driverName+" "+"Assert验证：某文本值是否与预期值一致{"+"实际值："+actualText+","+"预期值："+exceptText+"}";
		log.info(verityStr);
		try {
			Assert.assertEquals(actualText, exceptText);
			AssertPassLog(driverName);
		} catch (Error e) {
			AssertFailedLog(driverName);
			throw e;
		}
	}
}
