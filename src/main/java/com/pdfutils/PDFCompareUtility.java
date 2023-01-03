package com.pdfutils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDComboBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDPushButton;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

import com.framework.utils.GlobalVariables;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import de.redsix.pdfcompare.CompareResultWithPageOverflow;
import de.redsix.pdfcompare.PdfComparator;
import de.redsix.pdfcompare.RenderingException;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class PDFCompareUtility {
	
	ExtentReports report;
	ExtentTest test;
	String filename1,filename2;
	String file1name,file2name;
	int file1PageNo;
	int file2PageNo;
	String outFilePathPixel2Pixel;
	LinkedHashMap<String,String> file1Objects=new LinkedHashMap<String,String>();
	LinkedHashMap<String,String> file2Objects=new LinkedHashMap<String,String>();
	String destinationImgDir = System.getProperty("user.dir")+"/"+"target/PDF-Comparison-Report/pdfimage/";
	String reportDir=System.getProperty("user.dir")+"/"+"target/PDF-Comparison-Report/";
	String OCRPath;
	Properties properties;
	
	public PDFCompareUtility()
	{
		File directory=new File(reportDir);
		if (!directory.exists()) {
	        directory.mkdir();
		}
		 directory=new File(destinationImgDir);
		if (!directory.exists()) {
	        directory.mkdir();
		}
		
		
		try {
			BufferedReader reader;
			
			reader = new BufferedReader(new FileReader("config.properties"));
			properties = new Properties();
			properties.load(reader);
			OCRPath=properties.getProperty("OCRPath");
			reader.close();
		}  catch (Exception e1) {

		}
	}
	
	public void comparePDF(String file1, String file2) throws Exception
	{
		
		
		filename1=file1.split("/")[file1.split("/").length-1];
		filename2=file2.split("/")[file1.split("/").length-1];
		file1name=filename1.substring(0, filename1.indexOf("."));
		file2name=filename2.substring(0, filename2.indexOf("."));
		report=new ExtentReports(reportDir+file1name+"-"+file2name+" Comparison Report.html");;
		 
		
		file1PageNo=getpageNumber(file1);
		file2PageNo=getpageNumber(file2);
		
		test=report.startTest("Page Number Comparison ");
		if(file1PageNo==file2PageNo)
			test.log(LogStatus.PASS,"Number of Pages are same in both PDF:"+ file1PageNo);
		else
			test.log(LogStatus.FAIL,"Number of Pages are not same in both PDF: file 1-"+ file1PageNo+" and "+ "file 2-"+ file2PageNo);
		report.endTest(test);
		//--------------------------------------------------------------------------------------------------------------
		
		test=report.startTest("Pixel by Pixel Comparison ");
		boolean pixel2PixelResult=pixel2PixelComparison ( file1,  file2);
		if (pixel2PixelResult)
			test.log(LogStatus.PASS,"PDF PIXEL  comparison is success with  no difference  <a href='file:\\"+outFilePathPixel2Pixel+".pdf"+"'>Response</a>" );
		else
			test.log(LogStatus.FAIL,"PDF PIXEL  comparison is found difference  <a href='file:\\"+outFilePathPixel2Pixel+".pdf"+"'>Response</a>" );
		report.endTest(test);
		//--------------------------------------------------------------------------------------------------------------
		
		test=report.startTest("Embedded Object Level Comparison ");
		file1Objects.clear();
		file1Objects=storePdfObject(file1);
		file2Objects.clear();
		file2Objects=storePdfObject(file2);		
		comparePDFObjects();
		report.endTest(test);
		//--------------------------------------------------------------------------------------------------------------
		test=report.startTest("Line by Line Text Comparison ");
		comparePDFLinebyLine(file1,  file2);
		report.endTest(test);
		
		//------------------------------------------------
		if(properties.getProperty("OCRComparison").contains("Yes"))
		{
			test=report.startTest("OCR Line by Line Comparison ");
			OCRcompare(file1,  file2);
			report.endTest(test);
		}
		//---------------------------------------------------
		report.flush();
	}
	
	public int getpageNumber(String file1) throws Exception
	{
		PDDocument doc = PDDocument.load(new File(file1));
		int count = doc.getNumberOfPages();
		doc.close();
		return count;
		
	}
	
	public boolean pixel2PixelComparison (String file1, String file2) 
	{
		 outFilePathPixel2Pixel = reportDir+"Diff_"+filename1.substring(0, filename1.indexOf("."))+"_"+filename2.substring(0, filename2.indexOf("."));
		
		
		boolean result = false;
		try {
			result = new PdfComparator<CompareResultWithPageOverflow>(file1, file2, new CompareResultWithPageOverflow()).compare().writeTo(outFilePathPixel2Pixel);
		
			if (!result)
			    System.out.println("Differences found!");
			else
				System.out.println("PDF Matched successfully");
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	
	}
    public LinkedHashMap<String,String> storePdfObject(String file) throws Exception
    {
    	LinkedHashMap<String,String> temp=new LinkedHashMap<String,String>();
	  
	    PDDocument doc = PDDocument.load(new File(file));
	    PDDocumentCatalog catalog = doc.getDocumentCatalog();
	    PDAcroForm form = catalog.getAcroForm();
	    
	    try
	    {
	    List<PDField> fields = form.getFields();
	
	    for(PDField field: fields)
	    {
	        String name = field.getFullyQualifiedName();
	        if (field instanceof PDTextField || field instanceof PDComboBox)
	        {
	             Object value = field.getValueAsString();
	             System.out.print(name);
	             System.out.print(" = ");
	             System.out.print(value);
	             System.out.println();
	             temp.put(name, value.toString());
	        }
	        else if (field instanceof PDPushButton)
	            ;
	        else
	        {
	            if (field instanceof PDRadioButton)
	            {
	                PDRadioButton radioButton = (PDRadioButton)form.getField(name);
	                String value=radioButton.getValue();
	                System.out.print(name);
	                System.out.print(" = ");
	                System.out.print(value);
	                System.out.println();
	                temp.put(name, value);
	                /*List<String> exportValues = ((PDRadioButton) field).getSelectedExportValues();
	                for (String string : exportValues)
	                {
	                    name = field.getFullyQualifiedName();
	                    System.out.print(name);
	                    System.out.print(" = ");
	                    System.out.print(string);
	                    System.out.println();
	                } */
	            }
	            else if (field instanceof PDCheckBox)
	            {
	            	PDCheckBox box=(PDCheckBox)form.getField(name);
	                
	               
	                String value = box.getValue();
	                System.out.print(name);
	                System.out.print(" = ");
	                System.out.print(value);
	                System.out.println();
	                temp.put(name, value);
	            }
	            else
	            {
	            	System.out.print(name);
	            	System.out.println(field.getValueAsString());
	            	 temp.put(name, field.getValueAsString());
	            	
	            }
	
	        }
	    }
	   
	    }
	    catch(Exception e)
	    {
	    	
	    }
	    doc.close();
		return temp;
    }
    
    public void comparePDFObjects()
    {
    	System.out.println("Comparison will start");
    	
    	if(file1Objects.size()==0 && file1Objects.size()==0 )
    		  test.log(LogStatus.INFO, "No Embedded Object identified");	
    	
    	for(String object: file1Objects.keySet())
    	{
    		if(file2Objects.containsKey(object))
    		{
    			if(file1Objects.get(object).equals(file2Objects.get(object)))
    			  test.log(LogStatus.PASS, "Both PDF has: "+ object+"  field with same value: "+file1Objects.get(object)  );
    			else
    				test.log(LogStatus.FAIL, "Both PDF has: "+ object+" field with different value : "+file1Objects.get(object) +" & "+ file2Objects.get(object)  );
    		}
    		else
    		{
    		  test.log(LogStatus.FAIL, "PDF 2 does not contain "+ object);	
    		}
    	}
    	for(String object: file2Objects.keySet())
    	{
    		if(file2Objects.containsKey(object))
    		{
    			
    		}
    		else
    		{
    		  test.log(LogStatus.FAIL, "PDF 1 does not contain "+ object);	
    		}
    	}
    	
    }
    
  
	    
  public  void comparePDFLinebyLine(String actualPDFFile, String expectedPDFFile) {
		
		
		File pdftest1= new File(actualPDFFile);
		File pdftest2= new File(expectedPDFFile);
		
		
		
				
		String message =null;
		try {
		//Creating the object for the files
		PDDocument pdf1 = PDDocument.load(pdftest1);
	    PDDocument pdf2 = PDDocument.load(pdftest2);
	    PDPageTree pdf1pages = pdf1.getDocumentCatalog().getPages();    	 
	    PDPageTree pdf2pages = pdf2.getDocumentCatalog().getPages();
	    
	    PDFTextStripper pdfStripper = new PDFTextStripper();	    
      int issueNum = 0;
      int PageNum=1;
		for (@SuppressWarnings("unused") PDPage pdPage : pdf1pages) {
			
			int lineNum = 1;
			pdfStripper.setStartPage(PageNum);
			pdfStripper.setEndPage(PageNum);
			String pdf1PageText = pdfStripper.getText(pdf1);
			String pdf2PageText = pdfStripper.getText(pdf2);
			createFileAndAddContent(pdf1PageText, file1name);
			createFileAndAddContent(pdf2PageText, file2name);
           // System.out.println("-------------------------------------------------------------------");
			//System.out.println(pdf1PageText);
			//System.out.println("-------------------------------------------------------------------");
			//System.out.println(pdf2PageText);
			//System.out.println("-------------------------------------------------------------------");
			BufferedReader br1 = new BufferedReader(new FileReader(file1name + ".txt"));
			BufferedReader br2 = new BufferedReader(new FileReader(file2name + ".txt"));

			String line1 = br1.readLine();

			String line2 = br2.readLine();
			
			while (line1 != null || line2 != null) {
				
					if (line1.equals(line2)) {
	//					 System.out.println("Pass "+lineNum+":"+ " Expected - "+line1+ " Actual - " +line2);
					} else {
						issueNum++;
					//	message="<b>PageNum</b> " + PageNum+"  <b>lineNum</b> " + lineNum + " <b>Issue</b> " + issueNum + ":" + " <b>Expected</b> - " + line1
						//		+ " <b>Actual</b> - " + line2;
						
						 // test.log(LogStatus.FAIL, message);
						String difference1=StringUtils.difference(line1, line2);
						String difference2=StringUtils.difference(line2, line1);
						message="<b>PageNum</b> " + PageNum+"  <b>lineNum</b> " + lineNum + " <b>Issue</b> " + issueNum + ":" + " <b>Expected</b> - " + difference2
								+ " <b>Actual</b> - " + difference1;
						
						  test.log(LogStatus.FAIL, message);
						
					}
					line1 = br1.readLine();
					line2 = br2.readLine();
					lineNum++;
				
				
			}
			br1.close();br2.close();
			PageNum++;
		}
	    if (pdf1pages.getCount() != pdf2pages.getCount())
        {
          message = "Number of pages in the files ("+filename1+" , "+filename2+") do not match. actualPDFFile has "+pdf1pages.getCount()+" no pages, while expectedPDFFile has "+pdf2pages.getCount()+" no of pages";
          
        }
	    else
	    {
	    	message ="Number of pages in the files ("+filename1+" , "+filename2+") is matched";
	    	System.out.println(message);
	    	//test.log(LogStatus.PASS, message);
	    }
	    
	    if(issueNum==0)
	    	test.log(LogStatus.PASS, "All lines are verified & No miss match identified");
	    pdf1.close();
	    pdf2.close();
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
		
	}
		
	/**
	 * Create new .txt file and delete the previous file if present then copy the content into the new file.
	 * @param textLine
	 * @param filename
	 */
	public static void createFileAndAddContent(String textLine, String filename) {

		try {
			File myObj = new File(filename + ".txt");
			if (myObj.exists()) {
				myObj.delete();
			} else {
				System.out.println("***No File To Delete***");
			}

			myObj.createNewFile();
			//System.out.println("File created: " + myObj.getName());
			FileWriter myWriter = new FileWriter(myObj.getName());
			myWriter.write(textLine);
			myWriter.close();
			//System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			//System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	public  void comparePDFbystring(String actualPDFFile, String expectedPDFFile) {
		
		
		File pdftest1= new File(actualPDFFile);
		File pdftest2= new File(expectedPDFFile);
		boolean flag=true;
		String message =null;
		try {
		//Creating the object for the files
		PDDocument pdf1 = PDDocument.load(pdftest1);
	    PDDocument pdf2 = PDDocument.load(pdftest2);
	    PDPageTree pdf1pages = pdf1.getDocumentCatalog().getPages();    	 
	    PDPageTree pdf2pages = pdf2.getDocumentCatalog().getPages();
	    if (pdf1pages.getCount() != pdf2pages.getCount())
        {
            message = "Number of pages in the files ("+filename1+" , "+filename2+") do not match. actualPDFFile has "+pdf1pages.getCount()+" no pages, while expectedPDFFile has "+pdf2pages.getCount()+" no of pages";
            flag=false;
        }
	    else
	    {
	    	message ="Number of pages in the files ("+filename1+" , "+filename2+") is matched";
	    	System.out.println(message);
	    }
	    
	    //This class will take a pdf document and strip out all of the text and ignore the formatting and such.
	    PDFTextStripper pdfStripper = new PDFTextStripper();
        if(flag==true) {
	    for (int i = 0; i < pdf1pages.getCount(); i++)
        {
        	System.out.println(i + 1);
            pdfStripper.setStartPage(i + 1);
            pdfStripper.setEndPage(i + 1);

            String pdf1PageText = pdfStripper.getText(pdf1);
            
            String pdf2PageText = pdfStripper.getText(pdf2);
          //  test.log(LogStatus.INFO, pdf1PageText);
		  //  test.log(LogStatus.INFO, pdf2PageText);
            
            if (!pdf1PageText.equals(pdf2PageText))
            {
                //message = "<b>Contents of the files</b> ("+actualPDFFile+" , "+expectedPDFFile+") <b>do not match on Page no:</b> " + (i + 1)+" <b>actualPDFFile content is</b> : "+pdf1PageText+" , <b>while expectedPDFFile content is</b> : "+pdf2PageText;
            	message = "<b>Contents of the files</b> ("+filename1+" , "+filename2+") <b>do not match on Page no:</b> " + (i + 1);
            	//System.out.println(message);
                //test.log(LogStatus.FAIL, message);
                String difference = StringUtils.difference(pdf1PageText, pdf2PageText);
               // System.out.println("Difference is Page no: '" + (i + 1)+"' in PDF Filename '"+expectedPDFFile+"' : "+difference);
                String line1 = difference.substring(0, difference.indexOf("\n"));
              
                
                 //message="<b>Difference is Page no:</b> '" + (i + 1)+"' <b>in PDF Filename</b> '"+filename2+"' <b>:</b> "+line1;
                message="<b>Difference is Page no:</b> '" + (i + 1)+"' <b>in PDF Filename</b> '"+filename2+"' <b>:</b> "+line1;
                 test.log(LogStatus.FAIL, message);
                
            }
				else 
					{
						message = "Contents of the files (" + filename1 + " , " + filename2 + ") is matched";
						System.out.println(message);
						test.log(LogStatus.PASS, message);
					}
				}
			}
        else
			{
				System.out.println(message);
				test.log(LogStatus.PASS, message);
			}
        
        pdf1.close();
	    pdf2.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void OCRcompare(String actualPDFFile, String expectedPDFFile) throws Exception
	{
		convertpdf2Image(actualPDFFile);
		convertpdf2Image(expectedPDFFile);
		comparePDFLinebyLinewithOCR(actualPDFFile,expectedPDFFile);
		
	}
	
	public void convertpdf2Image(String file) throws Exception	
	{
		
		String filename1=file.split("/")[file.split("/").length-1];
		String file1name=filename1.substring(0, filename1.indexOf("."));
		
		//String destinationDir = System.getProperty("user.dir")+"/"+"src/test/resources/pdfimage/";
		  PDDocument document = PDDocument.load(new File(file));
	        PDFRenderer pdfRenderer = new PDFRenderer(document);
	        for (int page = 0; page < document.getNumberOfPages(); ++page)
	        { 
	            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.GRAY);

	            // suffix in filename will be used as the file format
	            ImageIO.write(bim,"jpeg", new File(destinationImgDir+file1name + "-" + (page+1) + ".jpeg"));
	            
	            
	        }
	        document.close();
		
	}
	
public  void comparePDFLinebyLinewithOCR(String actualPDFFile, String expectedPDFFile)  {
		
	   
	
	
		File pdftest1= new File(actualPDFFile);
		File pdftest2= new File(expectedPDFFile);
		
		
		
			
		String message =null;
		
		try
		{
		Tesseract tesseract = new Tesseract();
		tesseract.setDatapath(OCRPath);
		//Creating the object for the files
		PDDocument pdf1 = PDDocument.load(pdftest1);
	    PDDocument pdf2 = PDDocument.load(pdftest2);
	    PDPageTree pdf1pages = pdf1.getDocumentCatalog().getPages();    	 
	    PDPageTree pdf2pages = pdf2.getDocumentCatalog().getPages();
	    
	        
      int issueNum = 0;
      int PageNum=1;
		for (@SuppressWarnings("unused") PDPage pdPage : pdf1pages) {
			int lineNum = 1;
			
			
			
			String pdf1PageText = tesseract.doOCR(new File(destinationImgDir+file1name+"-"+PageNum+".jpeg"));	
			String pdf2PageText = tesseract.doOCR(new File(destinationImgDir+file2name+"-"+PageNum+".jpeg"));	
			//test.log(LogStatus.INFO, pdf1PageText);
			
			//System.out.println("-----------------------------------------------------------------");
			//System.out.println(pdf1PageText);
			//System.out.println("-----------------------------------------------------------------");
			//System.out.println(pdf2PageText);
			//System.out.println("-----------------------------------------------------------------");
			//test.log(LogStatus.INFO, pdf2PageText);
			createFileAndAddContent(pdf1PageText, file1name);
			createFileAndAddContent(pdf2PageText, file2name);

			BufferedReader br1 = new BufferedReader(new FileReader(file1name + ".txt"));
			BufferedReader br2 = new BufferedReader(new FileReader(file2name + ".txt"));

			String line1 = br1.readLine();

			String line2 = br2.readLine();
			
			while (line1 != null || line2 != null) {
              
				if (line1.equals(line2)) {
//					 System.out.println("Pass "+lineNum+":"+ " Expected - "+line1+ " Actual - " +line2);
				} else {
					issueNum++;
					String difference1=StringUtils.difference(line1, line2);
					String difference2=StringUtils.difference(line2, line1);
					message="<b>PageNum</b> " + PageNum+"  <b>lineNum</b> " + lineNum + " <b>Issue</b> " + issueNum + ":" + " <b>Expected</b> - " + difference2
							+ " <b>Actual</b> - " + difference1;
					
					  test.log(LogStatus.FAIL, message);
					 //String difference=  StringUtils.difference(line1, line2);
					 //message="<b>Difference is Page no:</b> '" + PageNum+"' <b>in PDF Filename</b> '"+filename1+"' <b>:</b> "+difference;
					 //test.log(LogStatus.FAIL, message);
					 
					 collectionCompare(PageNum,getCheckedcheckboxes(line1),getCheckedcheckboxes(line2));
				}
				line1 = br1.readLine();
				line2 = br2.readLine();
				lineNum++;
             
			}
			br1.close();br2.close();
			PageNum++;
		}
	    if (pdf1pages.getCount() != pdf2pages.getCount())
      {
          message = "Number of pages in the files ("+filename1+" , "+filename2+") do not match. actualPDFFile has "+pdf1pages.getCount()+" no pages, while expectedPDFFile has "+pdf2pages.getCount()+" no of pages";
          
      }
	    else
	    {
	    	message ="Number of pages in the files ("+filename1+" , "+filename2+") is matched";
	    	System.out.println(message);
	    	//test.log(LogStatus.PASS, message);
	    }
	    
	    if(issueNum==0)
	    	test.log(LogStatus.PASS, "All lines are verified & No miss match identified");
	    pdf1.close();
	    pdf2.close();
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
		
		
	}
	  public LinkedList<String> getCheckedcheckboxes(String Line)
	  {
		  LinkedList<String> temp=new LinkedList<String>();
		  if(Line!=null)
		  {
		  
		  Pattern p=Pattern.compile("\\.{1}\\w{1,}");
		  Matcher m=p.matcher(Line);
		  while (m.find())
		  {
			  String text=m.group();
			  
			  temp.add(text.replace(".", ""));
		  }
		  }
		return temp;
		  
	  }
	  
	  public void collectionCompare(int PageNum,LinkedList<String> l1,LinkedList<String> l2)
	  {
		  String checkedinpdf1alone="";
		  for (String s: l1)
		  {
			  if(l2.contains(s))
			  {
				  
			  }
			  else
			  { if(checkedinpdf1alone=="")
				  checkedinpdf1alone=s;
			    else
			    	checkedinpdf1alone=checkedinpdf1alone+","+s;
			  }
			  
		  }
		  if(checkedinpdf1alone!="")
			  test.log(LogStatus.FAIL, "<b>PageNum</b>:" + PageNum+" "+checkedinpdf1alone +" -Checked in PDF1 & but unchecked in PDF2");
		
		  String checkedinpdf2alone="";
		  for (String s: l2)
		  {
			  if(l1.contains(s))
			  {
				  
			  }
			  else
			  {
				  if(checkedinpdf2alone=="")
					  checkedinpdf2alone=s;
				    else
				    	checkedinpdf2alone=checkedinpdf2alone+","+s;
				  
			  }
			  
		  }
		  if(checkedinpdf2alone!="")
			  test.log(LogStatus.FAIL, "<b>PageNum</b>:" + PageNum+" "+ checkedinpdf2alone +" Checked in PDF2 & but unchecked in PDF1");
	       }

}
