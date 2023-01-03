package com.jira.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.TimeoutException;

import io.restassured.RestAssured;
import io.restassured.response.Response;


public class JiraServiceUtility {


     public String project;
     long StartTime;
     long EndTime ;
     long Duration_sec;
     public String baseURI;
     public String userName;
     public String password;     
     public String reporter;
     public String assignee;
     public String issueKey;
     public String priority;
     public String label;
     public String logDefect;
     public String defectLoggingLevel;
     public static boolean envDefectLogged;
     public boolean canILogdefect;     
     public static ArrayList<String> lowExceptionList;
     public static ArrayList<String> MediumExceptionList;
     public String severity;
     public boolean isEnvDefect;
     public String environment;
     Properties properties;
     
     
     public JiraServiceUtility() throws Exception
     {
    	 
    	    
    	    
	    	 try {
	 			BufferedReader reader;
	 			
	 			reader = new BufferedReader(new FileReader("config.properties"));
	 			properties = new Properties();
	 			properties.load(reader);
	 			
	 			reader.close();
	 		}  catch (Exception e1) {
	
	 		}
	    	    
		    baseURI=properties.getProperty("baseURI");  
		    userName=properties.getProperty("userName");  
		    password=properties.getProperty("password");  
		    project=properties.getProperty("project");
		    assignee=properties.getProperty("assignee");  
		    reporter=properties.getProperty("reporter");  
		    priority=properties.getProperty("Priority");  
		    logDefect=properties.getProperty("LogDefect");
		    defectLoggingLevel=  properties.getProperty("DefectLoggingLevel");
		    severity=  properties.getProperty("severity");
		    environment = properties.getProperty("environment");
		    label=properties.getProperty("label");
		    if(defectLoggingLevel.equalsIgnoreCase("Low"))
		    {
		    	if(lowExceptionList==null)
		    		lowExceptionList=new ArrayList<String>(readData("Low"));
		    }
		    else if(defectLoggingLevel.equalsIgnoreCase("Medium"))
		    {
		    	if(MediumExceptionList==null)
		    	 MediumExceptionList=new ArrayList<String>(readData("Medium"));
		    }
		   
     }
     
     /**
      * Below method is used to Create the Defect though calling JIRA API
      * It will mark the Summary, Description Reporter and some additional checks
      * @param summary
      * @param description
      * @param reporterName
     * @throws Exception 
      */
	public String  createJiraWusingRest(String summary, String description, String reporter,String assignee) throws Exception {
		StartTime = System.currentTimeMillis();
	    String responseString = null;
		
	    String data1 = "{\"fields\": {\"summary\": \"" + summary
				+ "\",\"issuetype\": {\"name\": \"Bug\"},\"project\": {\"key\": \""+project+"\"},\"description\": {\"type\": \"doc\",\"version\": 1,\"content\": [{\"type\": \"paragraph\",\"content\": [{\"text\": \""
				+ description + "\",\"type\": \"text\"}]}]},\"reporter\": {\"id\": \"" + reporter
				+ "\"},\"priority\": {\"name\": \""+priority+"\"},\"customfield_10036\": {\"value\": \""+severity+"\"},\"labels\": [\""+label+"\"],\"environment\": {\"type\": \"doc\",\"version\": 1,\"content\": [{\"type\": \"paragraph\",\"content\": [{\"text\": \""+environment+"\",\"type\": \"text\"}]}]},\"assignee\": {\"id\": \""+assignee+"\"}}}";
		Response res = RestAssured.given().relaxedHTTPSValidation().auth().preemptive()
				.basic(userName, password).header("Content-Type", "application/json")
				.body(data1).when().post(baseURI+"/rest/api/3/issue");
		EndTime = System.currentTimeMillis();
		
		if (res.statusCode() == 200 || res.statusCode() == 201) {
			System.out.println("Bug created");
		} else {
			
			//throw new Exception("Failed to create bug with response: "+ responseString);
		}
		
		Duration_sec = TimeUnit.MILLISECONDS.toSeconds(EndTime - StartTime);
		//System.out.println("Total createJiraWusingRest response time is: " + Duration_sec + " Sec");
		 responseString = res.asString();
		createFileAndAddContent(responseString,"log");
		//System.out.println("*******Response String*********" + responseString);
		JSONObject jsonObjectForKey = new JSONObject(res.asString());
		issueKey=jsonObjectForKey.get("key").toString();
		String issueURL = jsonObjectForKey.get("self").toString();
		System.out.println("========" + issueURL);
		
			
		 return issueURL;
		
		
		
	}
     
 /**
  * Below Method will add the Comment in Jira for Specific IssueID/Bug.
  * @param issueID
 * @throws Exception 
  */
	public void addCommentForIssue(String issueURL,String comment) throws Exception {
		StartTime = System.currentTimeMillis();
		String responseString = null;
		
		String data1 = "{\"body\": {\"type\": \"doc\",\"version\": 1,\"content\": [{\"type\": \"paragraph\",\"content\": [{\"text\": \""+comment+"\",\"type\": \"text\"}]}]}}";
		Response res=RestAssured.given().relaxedHTTPSValidation().auth().preemptive()
				.basic(userName, password)
				.header("Content-Type", "application/json")
				.body(data1).when().post(issueURL + "/comment");
		EndTime = System.currentTimeMillis();
		Duration_sec = TimeUnit.MILLISECONDS.toSeconds(EndTime - StartTime);
		//System.out.println("Total addCommentForIssue response time is: " + Duration_sec + " Sec"); 
		responseString=res.asString();
			if (res.statusCode() == 200 || res.statusCode() == 201) {
				
			} else {
				
				throw new Exception("Failed to create bug with response: "+ responseString);
			}
		
		
     }
	
	
	public static void createFileAndAddContent(String textLine, String filename) {

		try {
			File myObj = new File("c:\\JIRA\\"+filename + ".txt");
			if (myObj.exists()) {
				myObj.delete();
			} else {
				System.out.println("***No File To Delete***");
			}

			myObj.createNewFile();
			System.out.println("File created: " + myObj.getAbsolutePath());
			FileWriter myWriter = new FileWriter(myObj.getAbsolutePath());
			myWriter.write(textLine);
			myWriter.close();
			//System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			//System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	public  void addAttachment(String issueURL,String attachment) throws Exception {
		String responseString;
		StartTime = System.currentTimeMillis();
		
		Response res=RestAssured.given().relaxedHTTPSValidation().auth().preemptive()
				.basic(userName, password)
				.header("X-Atlassian-Token", "nocheck")
				.multiPart(new File(attachment))
				.when().post(issueURL+"/attachments");
		EndTime = System.currentTimeMillis();
		Duration_sec = TimeUnit.MILLISECONDS.toSeconds(EndTime - StartTime);
	//	System.out.println("Total attachmentupload response time is: " + Duration_sec + " Sec");  
		responseString=res.asString();
		if (res.statusCode() == 200 || res.statusCode() == 201) {
			//System.out.println("Bug created successfully");
		} else {
			
			throw new Exception("Failed to create bug with response: "+ responseString);
		}
     }
	
	
	/**
     * Below method is used to Create the Defect though calling JIRA API
     * It will mark the Summary, Description and some additional checks
     * @param summary
     * @param description
     * @param expected
     * @param actual
     * @param testCaseName
     * @param attachment
     * @param comment
    * @throws Exception 
     */
	public String logDefect(String summary,String description,String expected, String actual,String testCaseName,String label,String attachment,String comment) throws Exception
	{
		
		issueKey=null;
		    
		   if(logDefect.equalsIgnoreCase("Yes"))
		   {
			    
			    this.label=label;  
			    
		
			    String content="Description :  "+description+"\\n"+"Expected     :  "+expected+"\\n"+"Actual         :  "+actual+"\\n"+"TestCaseName:  "+testCaseName;
				    
				String issueURL=createJiraWusingRest(summary,content,reporter,assignee);
				addCommentForIssue(issueURL,comment);
				addAttachment(issueURL,attachment);
		   }
		
		return issueKey;
		
	}
	
	public String logDefect(String summary,String description,String expected, String actual,String testCaseName,String label,String attachment,String comment,String env, Throwable e) throws Exception
	{
		
		issueKey=null;
	
		   if(logDefect.equalsIgnoreCase("Yes"))
		   {
			  
			    
			    if(label!=null) this.label=label;
			    if(env!=null) this.environment=env;
			    String content="Description :  "+description+"\\n"+"Expected     :  "+expected+"\\n"+"Actual         :  "+actual+"\\n"+"TestCaseName:  "+testCaseName;
			    
			    if(defectLoggingLevel.equalsIgnoreCase("High"))
			    {
			    
			    
			    	canILogdefect=true;
					    
					
			    }
			    else if(defectLoggingLevel.equalsIgnoreCase("Medium"))
			    {
			    	decideifIcanLogdefect(e);
			    	
			    }
			    else if(defectLoggingLevel.equalsIgnoreCase("Low"))
			    {
			    	decideifIcanLogdefect(e);
			    	
			    }
			    
			    if(canILogdefect==true)
			    {
			    	String issueURL=createJiraWusingRest(summary,content,reporter,assignee);
					addCommentForIssue(issueURL,comment);
					addAttachment(issueURL,attachment);
			    }
			    
			    
		   }
		
		return issueKey;
		
	}
	
	public void decideifIcanLogdefect(Throwable e) throws Exception
	{
		
		if(defectLoggingLevel.equalsIgnoreCase("Medium"))
		{
			
			if(e instanceof TimeoutException||e instanceof ElementClickInterceptedException)
			{
				if(envDefectLogged==true)
					canILogdefect=false;
				else
				{	envDefectLogged=true;
					canILogdefect=true;
					severity="Low";
					isEnvDefect=true;
				}
				
			}
			else
			{
				for(int i=0;i<MediumExceptionList.size();i++)
				{
					try
					{
						Class<?> c=Class.forName(MediumExceptionList.get(i));
						

						if(c.isInstance(e) )
						{
							canILogdefect=true;
							break;
						}
					}
					catch(Exception ie)
					{
						
					}
					
					
				}
			}
				
			
				
		}
		else if(defectLoggingLevel.equalsIgnoreCase("Low"))
		{
			for(int i=0;i<lowExceptionList.size();i++)
			{
				try
				{
					Class<?> c=Class.forName(lowExceptionList.get(i));
					
	
					if(c.isInstance(e) )
					{
						canILogdefect=true;
						break;
					}
				}
				catch(Exception ie)
				{
					
				}
				
			}
		}
		
		
		
	}
	
	public ArrayList<String> readData(String sheetname) throws Exception	
	{
		 ArrayList<String> temp=new ArrayList<>();
		 temp.clear();
		 String folderPath=System.getProperty("user.dir")+"/"+"src/test/resources/jira/";
		 String excelFilepath = folderPath+"DefectLoggingLogic.xlsx";
		File file =    new File(excelFilepath);
        
        //Create an object of FileInputStream class to read excel file
        FileInputStream inputStream = new FileInputStream(file);
        
        //Creating workbook instance that refers to .xls file
        @SuppressWarnings("resource")
		XSSFWorkbook wb=new XSSFWorkbook(inputStream);
        
        //Creating a Sheet object using the sheet Name
        XSSFSheet sheet=wb.getSheet(sheetname);
        
        int rowcount=sheet.getLastRowNum();
        for(int i=1;i<=rowcount;i++)
        {
        	temp.add(sheet.getRow(i).getCell(0).getStringCellValue());
        }
		return temp;
	}
}
