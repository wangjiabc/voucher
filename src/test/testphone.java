import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class testphone {
	 public static int isPhone(String str) { 
	        Pattern p1 = null,p2 = null;
	        Matcher m = null;
	        boolean b = false;  
	        boolean b2 = false;
	        p1 = Pattern.compile(".*米.*");

	         m = p1.matcher(str);
	         b = m.matches();  
	         
	         p2 = Pattern.compile(".*公里.*");

	         m = p2.matcher(str);
	         b2 = m.matches();
	         
	         if(b){
	        	 
	        	 p1 = Pattern.compile("[^0-9]");
		         m = p1.matcher(str);
		        
	        	 int i=Integer.parseInt(m.replaceAll(""));
		         
	        	 return i;
	         }else if(b2){
	        	 p1 = Pattern.compile("[^0-9]");
		         m = p1.matcher(str);
		        
	        	 int i=Integer.parseInt(m.replaceAll(""));
		         
	        	 return i*1000;
	         }else{
	        	 return 0;
	         }
	     }
	 
	 public static void main(String[] args) {
	    Scanner scanner=new Scanner(System.in);
	    String a=scanner.nextLine();
	    System.out.println("大师傅大师傅="+isPhone(a));
  	}
}
