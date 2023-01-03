
package PDFComparision;


import com.pdfutils.PDFCompareUtility;



public class TestUtility {
	
 
  public  static void main(String[] args) throws Exception {  
	  
	  
	  PDFCompareUtility pdfUtil=new PDFCompareUtility();
	  
	  String filename1="data1Printed.pdf";
	  String filename2="data2Printed.pdf";
	  String FolderPath=System.getProperty("user.dir")+"/"+"src/main/resources/PDFFile/";
	  String file1 = FolderPath+filename1;
      String file2 =FolderPath+filename2;
      pdfUtil.comparePDF(file1,file2);
      
      
       filename1="data1.pdf";
	   filename2="data2.pdf";
	   file1 = FolderPath+filename1;
       file2 = FolderPath+filename2;
       pdfUtil.comparePDF(file1,file2);
       
       filename1="accelq-test1.pdf";
	   filename2="accelq-test2.pdf";
	   file1 = FolderPath+filename1;
       file2 = FolderPath+filename2;
       pdfUtil.comparePDF(file1,file2);
       
       
       
  }
  
  
}
