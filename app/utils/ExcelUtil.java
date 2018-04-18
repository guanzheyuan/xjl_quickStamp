package utils;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import play.Logger;

public class ExcelUtil<T>{

	
	/**
	 * 导出Excel
	 * @param title 标题
	 * @param headers 头部
	 * @param dataList 数据集
	 * @param out  输出流 
	 * @param pattern 日期 yyyy-MM-dd
	 */
	@SuppressWarnings("deprecation")
	public  void exportExcel(String title, String[] headers,List<Map<String, Object>> dataList, OutputStream out,String pattern){  
        HSSFWorkbook workbook = new HSSFWorkbook();  
        HSSFSheet sheet = workbook.createSheet(title);  
        sheet.setDefaultColumnWidth((short) 18);
        // sheet.setDefaultRowHeight((short)5);
        HSSFCellStyle style =createStyle(workbook);
	    HSSFCellStyle style2=createStyle2(workbook);  
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();  
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,0,0,0,(short)4,2,(short)6,5));  
        comment.setString(new HSSFRichTextString("可以在POI中添加注释"));  
        comment.setAuthor("leno");  
        HSSFRow row = sheet.createRow(0);  
        for (short i = 0; i < headers.length; i++){  
            HSSFCell cell = row.createCell(i);  
            cell.setCellStyle(style);  
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);  
            cell.setCellValue(text);  
            Logger.info("title:"+text);
        }  
        Logger.info("进入Excel工具-------------------------------------------------------------------------"+dataList.size());
        createFile(dataList,row,sheet,style2,pattern,patriarch,workbook,out);
	}
	/**
	 * 创建Excel表格
	 * @param dataList 数据集
	 * @param row  行对象
	 * @param sheet  数据对象
	 * @param style2  样式
	 * @param pattern  时间
	 * @param patriarch
	 * @param workbook
	 * @param out
	 */
	 @SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	 private  void createFile(List<Map<String, Object>> dataList,HSSFRow row, HSSFSheet sheet,HSSFCellStyle style2,String pattern,HSSFPatriarch patriarch ,HSSFWorkbook workbook,OutputStream out ){
		    Iterator<Map<String, Object>> it = dataList.iterator();  
	        int index = 0;  
	        while (it.hasNext()){  
	        		index++;  
	        		Logger.info("进入it："+index);
		            row = sheet.createRow(index);  
		            Map<String, Object> t = (Map<String, Object>) it.next();  
		            short index_1 =0;
		            for (String key : t.keySet()) {
		            	  Logger.info("得到t的value:"+t.get(key)+":列下标："+index_1);
		            	  HSSFCell cell = row.createCell(index_1);
		            	  cell.setCellStyle(style2);
		            	  Object value = t.get(key);
//		            Field[] fields = t.getClass().getDeclaredFields();  
//		            for (short i = 0; i < fields.length; i++){  
//		            	  Logger.info("进入循环"+fields.length);
//		                HSSFCell cell = row.createCell(i);  
//		                cell.setCellStyle(style2);  
//		                Field field = fields[i];  
//		                String fieldName = field.getName();  
//		                Logger.info("进入循环filename"+fieldName);
//		                String getMethodName = "get"+ fieldName.substring(0, 1).toUpperCase()+ fieldName.substring(1);  
		                try{  
//		                    Class tCls = t.getClass();  
//		                    Method getMethod = tCls.getMethod(getMethodName,new Class[]{}); 
//		                    Logger.info("得到getMethod"+getMethod);
//		                    Object value = getMethod.invoke(t, new Object[]  
//		                    {});  
		                    String textValue = null; 
		                    if (value instanceof Boolean){
		                        boolean bValue = (Boolean) value;  
		                        textValue = "��";  
		                        if (!bValue){  
		                            textValue = "Ů";  
		                        }  
		                    }  
		                    else if (value instanceof Date){  
		                        Date date = (Date) value;  
		                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);  
		                        textValue = sdf.format(date);  
		                    }  
		                    else if (value instanceof byte[]){  
		                        row.setHeightInPoints(60);  
		                        sheet.setColumnWidth(index_1, (short) (35.7 * 80));  
		                        byte[] bsValue = (byte[]) value;  
		                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,1000, 255, (short) 6, index, (short) 6, index);  
		                        //anchor.setAnchorType(2);  
		                        patriarch.createPicture(anchor,workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));  
		                    }  
		                    else{  
		                        textValue = String.valueOf(value);  
		                    }  
		                    if (textValue != null&&textValue.indexOf(",")==-1){  
		                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");  
		                        Matcher matcher = p.matcher(textValue);
		                        if (matcher.matches()){  
		                            cell.setCellValue(Double.parseDouble(textValue));  
		                        }  
		                        else{  
		                            HSSFRichTextString richString = new HSSFRichTextString(  
		                                    textValue);  
		                            HSSFFont font3 = workbook.createFont();  
		                            font3.setColor(HSSFColor.BLUE.index);  
		                            richString.applyFont(font3);  
		                            cell.setCellValue(richString);  
		                        }  
		                    }else if(textValue != null&&textValue.indexOf(",")!=-1){
		                    	  Pattern p = Pattern.compile("^//d+(//.//d+)?$");  
		                    	  String[] textValueArr = textValue.split(",");
		                    	  for (int j = 0; j < textValueArr.length; j++) {
		                    		  Logger.info("处理列值"+textValueArr[j]);
		                    		  Matcher matcher = p.matcher(textValueArr[j]);
		                    		  if (matcher.matches()){  
				                            cell.setCellValue(Double.parseDouble(textValueArr[j]));  
				                        }  
				                        else{  
				                            HSSFRichTextString richString = new HSSFRichTextString(  
				                            		textValueArr[j]);  
				                            HSSFFont font3 = workbook.createFont();  
				                            font3.setColor(HSSFColor.BLUE.index);  
				                            richString.applyFont(font3);  
				                            cell.setCellValue(richString);  
				                        }
								}
		                    }
		                    
		                }  
		                catch (SecurityException e){  
		                    System.out.println(e.getMessage());
		                }  
		                catch (IllegalArgumentException e){  
		                    System.out.println(e.getMessage());
		                }  
		                finally{  
		                } 
		                index_1++;
		        	}
		        }  
		        try {  
		            workbook.write(out);  
		        }  
		        catch (IOException e){  
		            System.out.println(e.getMessage());
		        }   
		        
	 }
	/**
	  * 样式背景设置
	  * @param workbook
	  * @return
	  */
	 private static HSSFCellStyle createStyle(HSSFWorkbook workbook){
			HSSFCellStyle style = workbook.createCellStyle();  
			style.setFillForegroundColor(HSSFColor.WHITE.index);  
	        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
	        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
	        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
	        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
	        HSSFFont font = workbook.createFont();  
	        font.setColor(HSSFColor.BLACK.index);
	        font.setFontHeightInPoints((short) 10);  
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
	        style.setFont(font);  
	        return  style;
		}
		/**
		 * 样式背景设置
		 * @param workbook
		 * @return HSSFCellStyle
		 */
		private static HSSFCellStyle createStyle2(HSSFWorkbook workbook){
	        HSSFCellStyle style2 = workbook.createCellStyle();  
	        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);  
	        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
	        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
	        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);  
	        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
	        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
	        HSSFFont font2 = workbook.createFont();  
	        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
	        style2.setFont(font2);  
	        return  style2;
		}
}
