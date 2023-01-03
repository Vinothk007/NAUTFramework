package com.framework.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import io.cucumber.java.Scenario;


public class Reporter {
	public static InheritableThreadLocal<Scenario> TScenario = new InheritableThreadLocal<Scenario>();

	private static int fileCount = 0;

	/**
	 * Added desired message to cucumber report
	 * 
	 */
	public synchronized static void addStepLog(String message) {
		//TScenario.get().write(message);
		TScenario.get().log(message);		
	}
	
	/**
	 * Add web application screenshot (Display in step)
	 * @throws IOException 
	 * 	
	 */
	public static void addScreenCapture() throws IOException {

		byte[] screenshot = CaptureScreenshot.screenCaptureByte();		
		TScenario.get().attach(screenshot, "image/png", "Error Screenshot");		
	}

	/**
	 * Add web application screenshot (current screen)
	 * 
	 */
	public synchronized static void addScreenCapture2() {

		String screenshotName = CaptureScreenshot.screenCapture(null);

		String html = "<div style=\"margin-bottom: 5px\">"
				+ "<label style=\"color: #1E90FF\">Click on image to see Screenshot !!</label>" + "<a href=\""
				+ screenshotName
				+ "\" target=\"_blank\" style=\"float:right;\"><img src=\"core-image-screenshot.png\" style=\"background-color:aliceblue;\"/></a>\r\n"
				+ "</div>";

		addStepLog(html);
	}

	/**
	 * Take Web screenshot
	 * "Click on image to see Failure Screenshot !!", "#ff1a1a"
	 */
	public synchronized static void addScreenCapture(String message, String color) {

		String screenshotName = CaptureScreenshot.screenCapture(null);

		String html = "<div style=\"margin-bottom: 5px\">"
				+ "<label style=\"color: "+color+"\">"+message+"</label>" + "<a href=\""
				+ screenshotName
				+ "\" target=\"_blank\" style=\"float:right;\"><img src=\"core-image-screenshot.png\" style=\"background-color:aliceblue;\"/></a>\r\n"
				+ "</div>";

		addStepLog(html);
	}

	/**
	 * Add web application screenshot (entire application)
	 * 
	 */
	public synchronized static void addEntireScreenCaptured() {

		String screenshotName = CaptureScreenshot.captureEntireScreen();

		String html = "<div style=\"margin-bottom: 5px\">"
				+ "<label style=\"color: #1E90FF\">Click on image to see Screenshot !!</label>" + "<a href=\""
				+ screenshotName
				+ "\" target=\"_blank\" style=\"float:right;\"><img src=\"core-image-screenshot.png\" style=\"background-color:aliceblue;\"/></a>\r\n"
				+ "</div>";

		addStepLog(html);
	}
	
	
	/**
	 * Adding Request or Response in Extent report
	 * 
	 */
	public synchronized static void addResponse(String content, String type, String message) {

		PropertyFileUtils property = new PropertyFileUtils("./src/test/resources/extent.properties");
		String reportDirPath = property.getProperty("extent.reporter.spark.scr");

		/* default file extension is .txt */
		String extension = ".txt";

		type = type.trim();
		if (type.equalsIgnoreCase("json") || type.equalsIgnoreCase(".json")) {
			extension = ".json";
		} else if (type.equalsIgnoreCase("xml") || type.equalsIgnoreCase(".xml")) {
			extension = ".xml";
		} else if (type.equalsIgnoreCase("yaml") || type.equalsIgnoreCase(".yaml") || type.equalsIgnoreCase("yml")
				|| type.equalsIgnoreCase(".yml")) {
			extension = ".yml";
		}

		String fileName = "file-" + (++fileCount) + extension;
		String filePath = reportDirPath + "/" + fileName;

		FileManager.createFile(filePath);

		FileManager.write(filePath, content);

		String html = "";

		if (message == null || message.length() == 0) {
			html = "<a href=\"" + fileName
					+ "\" target=\"_blank\" style=\"float: right\"><img src=\"core-image-hper-link.png\" style=\"background-color: aliceblue\"></a>";
		} else {
			html = "<lable><font color=#1E90FF>" + message + "</font></lable>" + "<a href=\"" + fileName
					+ "\" target=\"_blank\" style=\"float: right\"><img src=\"core-image-hper-link.png\" style=\"background-color: aliceblue\"></a>";
		}

		Reporter.addStepLog(html);
	}

	/**
	 * Cleaning Report folder
	 * 
	 */
	public static void cleanReportFolder() {

		PropertyFileUtils property = new PropertyFileUtils("./src/test/resources/extent.properties");
		String partialSparkDirPath = "./" + property.getProperty("extent.reporter.spark.out");
		
		File dirPath = new File(partialSparkDirPath);
		
		if(dirPath.exists()) {
			FileManager.deleteAllFilesInFolder(partialSparkDirPath);
		}
		

	}
	/**
	 * Add web application screenshot (Display in step)
	 * @throws IOException 
	 * 	
	 */
	public static String getScreenshotPath() throws IOException {

		byte[] screenshot = CaptureScreenshot.screenCaptureByte();	
		PropertyFileUtils extentPro = new PropertyFileUtils("./src/test/resources/extent.properties");
		String sparkDirPath = "./" + extentPro.getProperty("extent.reporter.spark.scr");
		Date date = new Date();
		String fileNamewithPath=sparkDirPath+"/Screenshot_"+date.toString()+".png";
		fileNamewithPath=fileNamewithPath.replaceAll(" ", "");
		fileNamewithPath=fileNamewithPath.replaceAll(":", "-");
        FileOutputStream os= new FileOutputStream(new File(fileNamewithPath));
		os.write(screenshot);
		return fileNamewithPath;	
		
	}

}
