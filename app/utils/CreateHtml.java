package utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class CreateHtml {

	
	
	public static void createHtml(String savePath,String body){
		StringBuilder stringHtml = new StringBuilder();  
		PrintStream printStream = null;
		try{  
			   //打开文件  
			   printStream = new PrintStream(new FileOutputStream(savePath));  
			}catch(FileNotFoundException e){  
			   e.printStackTrace();  
			}
		//输入HTML文件内容  
		stringHtml.append("<html><head>");  
		stringHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");  
		stringHtml.append("<title>测试报告文档</title>");   
		stringHtml.append("</head>");  
		stringHtml.append("<body>");  
		stringHtml.append("<div>"+body+"</div>");  
		stringHtml.append("</body></html>");  
		try{  
		       //将HTML文件内容写入文件中  
		       printStream.println(stringHtml.toString());  
		}catch (Exception e) {  
		      
		    e.printStackTrace();  
		} 
	}
}
