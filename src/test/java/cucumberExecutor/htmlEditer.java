package cucumberExecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedHashMap;

import com.framework.utils.GlobalVariables;

public class htmlEditer {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		LinkedHashMap<String,String> temp=new LinkedHashMap<String,String>();
		temp.put("NAUT-1", "unable to login#description#login button disabled");
		temp.put("NAUT-2", "unable to logout#description#logout button disabled");
		htmlEditer f=new htmlEditer();
		String newcontent=f.defectTableFormat(temp);
		f.editHTMLReport(newcontent);
	}
	
	public void editHTMLReport(String newConent) throws Exception
	{
		
		
		
	//	String reportPath=System.getProperty("user.dir") + GlobalVariables.configProp.getProperty("reportPath");
		String reportPath=System.getProperty("user.dir") + "/target/Spark/ExtentSpark.html";
		File inFile=new File(reportPath);
		File outFile=new File(System.getProperty("user.dir")+"/target/Spark/temp.html" );
		 FileInputStream fis  = new FileInputStream(inFile);
	     BufferedReader in = new BufferedReader
	         (new InputStreamReader(fis));

	     // output         
	     FileOutputStream fos = new FileOutputStream(outFile);
	     PrintWriter out = new PrintWriter(fos);

	     String thisLine = "";
	     boolean flag1=false;
	     int divCount=0;
	     boolean skipskeck=false;
	     int i =1;
	     while ((thisLine = in.readLine()) != null) {
	       if(skipskeck==false)
	       {
	    	 if(thisLine.contains("System/Environment"))
	    	 {
	    		 flag1=true;
	    		 //out.println(newConent);
	    	 }
	    	 else if(flag1==true && thisLine.contains("/div") )
	    		 divCount=divCount+1;
	    	
	    	 if(flag1==true & divCount==4)
	    	 {
	    		 skipskeck=true;
	    		 out.println(newConent);
	    		 
	    	 }
	       }
	       
	    	   out.println(thisLine); 
	       
	    	   
	      
	       i++;
	       }
	    out.flush();
	    out.close();
	    in.close();
	    
	    inFile.delete();
	    outFile.renameTo(inFile);
		
	}
	
	public String defectTableFormat(LinkedHashMap<String,String>  defects)
	{
	
		String defectContent="";
		if(defects.size()>0)
		{
			String defectCount=defects.size()+"";
			defectContent="<div class=\"col-lg-6 col-md-12 sysenv-container\">\r\n"
					+ "<div class=\"card\">\r\n"
					+ "<div class=\"card-header\"><p>Defect Count -"+defectCount+"</p></div>\r\n"
					+ "<div class=\"card-body pb-0 pt-0\"><table class=\"table table-sm table-bordered\">\r\n"
					+ "<thead><tr class=\"bg-gray\"><th>Defect ID</th><th>Summary</th><th>Description</th></tr></thead>\r\n"
					+ "<tbody>\r\n";
			
			for(String key:defects.keySet())
			{
				String summary=defects.get(key).split("#")[0];
				String description=defects.get(key).split("#")[1];
				
				
				
				defectContent=defectContent+"<tr>\r\n"
						+ "<td>"+key+"</td>\r\n"
						+ "<td>"+summary+"</td>\r\n"
						+ "<td>"+description+"</td>\r\n"
						
						+ "</tr>\r\n";
			}
			
			defectContent=defectContent+"</tbody>\r\n"
					+ "</table></div>\r\n"
					+ "</div>\r\n"
					+ "</div>\r\n";
		}
		return defectContent;
		
	}
	public void editHTMLReportforSysteminfo(String newConent) throws Exception
	{
		
		
		
	//	String reportPath=System.getProperty("user.dir") + GlobalVariables.configProp.getProperty("reportPath");
		String reportPath=System.getProperty("user.dir") + "/target/Spark/ExtentSpark.html";
		File inFile=new File(reportPath);
		File outFile=new File(System.getProperty("user.dir")+"/target/Spark/temp.html" );
		 FileInputStream fis  = new FileInputStream(inFile);
	     BufferedReader in = new BufferedReader
	         (new InputStreamReader(fis));

	     // output         
	     FileOutputStream fos = new FileOutputStream(outFile);
	     PrintWriter out = new PrintWriter(fos);

	     String thisLine = "";
	     boolean flag1=false;
	     int divCount=0;
	     boolean skipskeck=false;
	     int i =1;
	     while ((thisLine = in.readLine()) != null) {
	       if(skipskeck==false)
	       {
	    	 if(thisLine.contains("System/Environment"))
	    	 {
	    		 flag1=true;
	    		 //out.println(newConent);
	    	 }
	    	 else if(flag1==true && thisLine.contains("/tbody") )
	    		 divCount=divCount+1;
	    	
	    	 if(flag1==true & divCount==1)
	    	 {
	    		 skipskeck=true;
	    		 out.println(newConent);
	    		 
	    	 }
	       }
	       
	    	   out.println(thisLine); 
	       
	    	   
	      
	       i++;
	       }
	    out.flush();
	    out.close();
	    in.close();
	    
	    inFile.delete();
	    outFile.renameTo(inFile);
		
	}
	
	public String formatbrowserName(String browserName)
	{
		String newcontent="<tr>\r\n"
				+ "<td>browsers</td>\r\n"
				+ "<td>"+browserName+"</td>\r\n"
				+ "</tr>";
		return newcontent;
	}
	
}
