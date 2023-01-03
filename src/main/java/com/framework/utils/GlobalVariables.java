package com.framework.utils;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Set;

import com.aventstack.extentreports.ExtentReports;

public class GlobalVariables {
public static PropertyFileUtils configProp;
public static Set<String> featureList;
public static InheritableThreadLocal<String> currentFeature = new InheritableThreadLocal<String>();
public static InheritableThreadLocal<String> currentBrowser = new InheritableThreadLocal<String>();
public static InheritableThreadLocal<String> currentMOS = new InheritableThreadLocal<String>();
public static String currentTag;

public static String browserName;
public static String driverName;
public static Duration waitTime=Duration.ofSeconds(30);
public static String applicationName;

public static String applicationUrl=null;

//public static ExtentReportManager extentReportManagerObj;
public static ExtentReports extentReport;
public static String appType="androidEmuiosandroidReal";


//added by sathiyaraj
public static InheritableThreadLocal<String> currentStep = new InheritableThreadLocal<String>();
public static InheritableThreadLocal<String> currentTagLabel = new InheritableThreadLocal<String>();
public static LinkedHashMap<String,String> defectList=new LinkedHashMap<String,String>();
public static InheritableThreadLocal<Integer> currentRetryCount = new InheritableThreadLocal<Integer>();
public static InheritableThreadLocal<Integer> maxRetryCount = new InheritableThreadLocal<Integer>();

}
