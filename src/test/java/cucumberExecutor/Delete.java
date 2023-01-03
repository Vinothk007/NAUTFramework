package cucumberExecutor;

import java.io.IOException;

public class Delete {
	static String b;
	static int a=1;
	static String check = "one,two";
	public static void main(String[] args) throws IOException {
		
		int b=a++;
		System.out.println(a++);
		System.out.println(b);
		System.out.println(a);
//		String a ="re";
//		
//		Process proc = Runtime.getRuntime().exec("adb devices");
//		
//		
//		BufferedReader stdInput = new BufferedReader(new 
//			     InputStreamReader(proc.getInputStream()));
//		System.out.println(stdInput);
//	
//
//			// Read the output from the command
//			System.out.println("Here is the standard output of the command:\n");
//			String s = null;
//			while ((s = stdInput.readLine()) != null) {
//			    System.out.println(s);
//			}
//		
//		System.out.println(Objects.isNull(a));
//		System.out.println(Objects.isNull(b));
//
		//new B().test();
		//new B().test();
		}
	}
class B{
	static int s;
	
	static void test() {
		System.out.println(s);
		s++;
	}
	
	void test2() {
		test();
		System.out.println(s);
	}
	
}

