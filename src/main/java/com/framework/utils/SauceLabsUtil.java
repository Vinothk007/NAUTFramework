package com.framework.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;



public class SauceLabsUtil {
	
	private static final String SAUCE_USERNAME = GlobalVariables.configProp.getProperty("SauceUN");
	  private static final String SAUCE_ACCESS_KEY = GlobalVariables.configProp.getProperty("SauceKey");
	  private static final String KEY = String.format("%s:%s", SAUCE_USERNAME, SAUCE_ACCESS_KEY);
//	  private static final String SAUCE_TESTS_URL = "https://app.eu-central-1.saucelabs.com/tests";
	  private static final String SAUCE_TESTS_URL = "https://app.us-west-1.saucelabs.com/tests";
	  
	  public static String getShareableLink(String sauceJobId) throws NoSuchAlgorithmException, InvalidKeyException {
	    SecretKeySpec sks = new SecretKeySpec(KEY.getBytes(), "HmacMD5");
	    Mac mac = Mac.getInstance("HmacMD5");
	    mac.init(sks);
	    byte[] result = mac.doFinal(sauceJobId.getBytes());
	    StringBuilder hash = new StringBuilder();
	    for (byte b : result) {
	      String hex = Integer.toHexString(0xFF & b);
	      if (hex.length() == 1) {
	        hash.append('0');
	      }
	      hash.append(hex);
	    }
	    String digest = hash.toString();
	    return String.format("%s/%s?auth=%s", SAUCE_TESTS_URL, sauceJobId, digest);
	  }

	  public static void main(String[] args) {
	    try {
	      String sauceJobId = "c5eb67f00e124ba0a46f2b7869bd418c";
	      String shareableLink = SauceLabsUtil.getShareableLink(sauceJobId);
	      System.out.println(shareableLink);
	    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
	      // Handle appropriately according to your use  
	      e.printStackTrace();
	    }
	  }

}
