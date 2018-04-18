package utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.w3c.dom.Document;

import play.Logger;
public class Preview {

	
	/**
	 * 03word 转html
	 * @param inputFile
	 * @param pdfFile
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public static void wordToHtml(String inputFile,String htmlFile) throws IOException, ParserConfigurationException, TransformerException{
		  InputStream input = new FileInputStream(inputFile);
		  HWPFDocument wordDocument = new HWPFDocument(input);
		  WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
		    DocumentBuilderFactory.newInstance().newDocumentBuilder()
		      .newDocument());
		  wordToHtmlConverter.setPicturesManager(new PicturesManager() {
		   public String savePicture(byte[] content, PictureType pictureType,
		     String suggestedName, float widthInches, float heightInches) {
		    return suggestedName;
		   }
		  });
		  wordToHtmlConverter.processDocument(wordDocument);
		  List pics = wordDocument.getPicturesTable().getAllPictures();
		  if (pics != null) {
		   for (int i = 0; i < pics.size(); i++) {
		    Picture pic = (Picture) pics.get(i);
		    try {
		     pic.writeImageContent(new FileOutputStream(htmlFile));
		    } catch (FileNotFoundException e) {
		     e.printStackTrace();
		    }
		   }
		  }
		  Document htmlDocument = wordToHtmlConverter.getDocument();
		  ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		  DOMSource domSource = new DOMSource(htmlDocument);
		  StreamResult streamResult = new StreamResult(outStream);
		  TransformerFactory tf = TransformerFactory.newInstance();
		  Transformer serializer = tf.newTransformer();
		  serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		  serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		  serializer.setOutputProperty(OutputKeys.METHOD, "html");
		  serializer.transform(domSource, streamResult);
		  outStream.close();
		  String content = new String(outStream.toByteArray());
		  FileUtils.writeStringToFile(new File(htmlFile), content, "utf-8");
	}
	
	/**
	 * 07dox 转html
	 * @param inputFile
	 * @param pdfFile
	 * @throws IOException
	 */
	public static void word07ToHtml(String inputFile,String htmlFile) throws IOException{
		 //读取文档内容    
        InputStream in = new FileInputStream(inputFile);    
        XWPFDocument document = new XWPFDocument(in);    
        File imageFolderFile = new File("");    
        //加载html页面时图片路径  
        XHTMLOptions options = XHTMLOptions.create().URIResolver( new BasicURIResolver("./"));   
        //图片保存文件夹路径  
        options.setExtractor(new FileImageExtractor(imageFolderFile));    
        OutputStream out = new FileOutputStream(new File(htmlFile));    
        XHTMLConverter.getInstance().convert(document, out, options);    
        out.close();    
	}
	
	   
	
	/**
	 * excel 转 html
	 * @param inputFile
	 * @param htmlFile
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public static void excel03ToHtml(String inputFile,String htmlFile) throws IOException, ParserConfigurationException, TransformerException{
		 InputStream input=new FileInputStream(inputFile);
	     HSSFWorkbook excelBook=new HSSFWorkbook(input);
	     ExcelToHtmlConverter excelToHtmlConverter = new ExcelToHtmlConverter (DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument() );
	     excelToHtmlConverter.processWorkbook(excelBook);
	     List pics = excelBook.getAllPictures();
	     if (pics != null) {
	         for (int i = 0; i < pics.size(); i++) {
	             Picture pic = (Picture) pics.get (i);
	             try {
	                 pic.writeImageContent (new FileOutputStream (inputFile) );
	             } catch (FileNotFoundException e) {
	                 e.printStackTrace();
	             }
	         }
	     }
	     Document htmlDocument =excelToHtmlConverter.getDocument();
	     ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	     DOMSource domSource = new DOMSource (htmlDocument);
	     StreamResult streamResult = new StreamResult (outStream);
	     TransformerFactory tf = TransformerFactory.newInstance();
	     Transformer serializer = tf.newTransformer();
	     serializer.setOutputProperty (OutputKeys.ENCODING, "utf-8");
	     serializer.setOutputProperty (OutputKeys.INDENT, "yes");
	     serializer.setOutputProperty (OutputKeys.METHOD, "html");
	     serializer.transform (domSource, streamResult);
	     outStream.close();
	     String content = new String (outStream.toByteArray() );
	     FileUtils.writeStringToFile(new File (htmlFile), content, "utf-8");
	}
	
	/**
     * 程序入口方法
     * @param filePath 文件的路径
     * @param isWithStyle 是否需要表格样式 包含 字体 颜色 边框 对齐方式
     * @return <table>...</table> 字符串
     */
    public static String readExcelToHtml(String filePath , boolean isWithStyle){
        
        InputStream is = null;
        String htmlExcel = null;
        try {
            File sourcefile = new File(filePath);
            is = new FileInputStream(sourcefile);
            Workbook wb = WorkbookFactory.create(is);
            if (wb instanceof XSSFWorkbook) {
                XSSFWorkbook xWb = (XSSFWorkbook) wb;
                htmlExcel = getExcelInfo(xWb,isWithStyle);
            }else if(wb instanceof HSSFWorkbook){
                HSSFWorkbook hWb = (HSSFWorkbook) wb;
                htmlExcel = getExcelInfo(hWb,isWithStyle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return htmlExcel;
    }
    
    
    
    public static String getExcelInfo(Workbook wb,boolean isWithStyle){
        
        StringBuffer sb = new StringBuffer();
        Sheet sheet = wb.getSheetAt(0);//获取第一个Sheet的内容
        int lastRowNum = sheet.getLastRowNum();
        Map<String, String> map[] = getRowSpanColSpanMap(sheet);
        sb.append("<table style='border-collapse:collapse;' border='1' width='100%'>");
        Row row = null;        //兼容
        Cell cell = null;    //兼容
        
        for (int rowNum = sheet.getFirstRowNum(); rowNum <= lastRowNum; rowNum++) {
            row = sheet.getRow(rowNum);
            if (row == null) {
                sb.append("<tr><td > &nbsp;</td></tr>");
                continue;
            }
            sb.append("<tr>");
            int lastColNum = row.getLastCellNum();
            for (int colNum = 0; colNum < lastColNum; colNum++) {
                cell = row.getCell(colNum);
                if (cell == null) {    //特殊情况 空白的单元格会返回null
                    sb.append("<td>&nbsp;</td>");
                    continue;
                }

                String stringValue = getCellValue(cell);
                if (map[0].containsKey(rowNum + "," + colNum)) {
                    String pointString = map[0].get(rowNum + "," + colNum);
                    map[0].remove(rowNum + "," + colNum);
                    int bottomeRow = Integer.valueOf(pointString.split(",")[0]);
                    int bottomeCol = Integer.valueOf(pointString.split(",")[1]);
                    int rowSpan = bottomeRow - rowNum + 1;
                    int colSpan = bottomeCol - colNum + 1;
                    sb.append("<td rowspan= '" + rowSpan + "' colspan= '"+ colSpan + "' ");
                } else if (map[1].containsKey(rowNum + "," + colNum)) {
                    map[1].remove(rowNum + "," + colNum);
                    continue;
                } else {
                    sb.append("<td ");
                }
                
                //判断是否需要样式
                if(isWithStyle){
                    //dealExcelStyle(wb, sheet, cell, sb);//处理单元格样式
                }
                
                sb.append(">");
                if (stringValue == null || "".equals(stringValue.trim())) {
                    sb.append(" &nbsp; ");
                } else {
                    // 将ascii码为160的空格转换为html下的空格（&nbsp;）
                    sb.append(stringValue.replace(String.valueOf((char) 160),"&nbsp;"));
                }
                sb.append("</td>");
            }
            sb.append("</tr>");
        }

        sb.append("</table>");
        return sb.toString();
    }
	private static Map<String, String>[] getRowSpanColSpanMap(Sheet sheet) {

        Map<String, String> map0 = new HashMap<String, String>();
        Map<String, String> map1 = new HashMap<String, String>();
        int mergedNum = sheet.getNumMergedRegions();
        CellRangeAddress range = null;
        for (int i = 0; i < mergedNum; i++) {
            range = sheet.getMergedRegion(i);
            int topRow = range.getFirstRow();
            int topCol = range.getFirstColumn();
            int bottomRow = range.getLastRow();
            int bottomCol = range.getLastColumn();
            map0.put(topRow + "," + topCol, bottomRow + "," + bottomCol);
            // System.out.println(topRow + "," + topCol + "," + bottomRow + "," + bottomCol);
            int tempRow = topRow;
            while (tempRow <= bottomRow) {
                int tempCol = topCol;
                while (tempCol <= bottomCol) {
                    map1.put(tempRow + "," + tempCol, "");
                    tempCol++;
                }
                tempRow++;
            }
            map1.remove(topRow + "," + topCol);
        }
        Map[] map = { map0, map1 };
        return map;
    }
    
    
    /**
     * 获取表格单元格Cell内容
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {

        String result = new String();  
        switch (cell.getCellType()) {  
        case Cell.CELL_TYPE_NUMERIC:// 数字类型  
            if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式  
                SimpleDateFormat sdf = null;  
                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {  
                    sdf = new SimpleDateFormat("HH:mm");  
                } else {// 日期  
                    sdf = new SimpleDateFormat("yyyy-MM-dd");  
                }  
                Date date = cell.getDateCellValue();  
                result = sdf.format(date);  
            } else if (cell.getCellStyle().getDataFormat() == 58) {  
                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)  
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
                double value = cell.getNumericCellValue();  
                Date date = org.apache.poi.ss.usermodel.DateUtil  
                        .getJavaDate(value);  
                result = sdf.format(date);  
            } else {  
                double value = cell.getNumericCellValue();  
                CellStyle style = cell.getCellStyle();  
                DecimalFormat format = new DecimalFormat();  
                String temp = style.getDataFormatString();  
                // 单元格设置成常规  
                if (temp.equals("General")) {  
                    format.applyPattern("#");  
                }  
                result = format.format(value);  
            }  
            break;  
        case Cell.CELL_TYPE_STRING:// String类型  
            result = cell.getRichStringCellValue().toString();  
            break;  
        case Cell.CELL_TYPE_BLANK:  
            result = "";  
            break; 
        default:  
            result = "";  
            break;  
        }  
        return result;  
    }
    
     
    /**
     * 单元格内容的水平对齐方式
     * @param alignment
     * @return
     */
    private static String convertAlignToHtml(short alignment) {

        String align = "left";
        switch (alignment) {
        case CellStyle.ALIGN_LEFT:
            align = "left";
            break;
        case CellStyle.ALIGN_CENTER:
            align = "center";
            break;
        case CellStyle.ALIGN_RIGHT:
            align = "right";
            break;
        default:
            break;
        }
        return align;
    }

    /**
     * 单元格中内容的垂直排列方式
     * @param verticalAlignment
     * @return
     */
    private static String convertVerticalAlignToHtml(short verticalAlignment) {

        String valign = "middle";
        switch (verticalAlignment) {
        case CellStyle.VERTICAL_BOTTOM:
            valign = "bottom";
            break;
        case CellStyle.VERTICAL_CENTER:
            valign = "center";
            break;
        case CellStyle.VERTICAL_TOP:
            valign = "top";
            break;
        default:
            break;
        }
        return valign;
    }
    
    private static String convertToStardColor(HSSFColor hc) {

        StringBuffer sb = new StringBuffer("");
        if (hc != null) {
            if (HSSFColor.AUTOMATIC.index == hc.getIndex()) {
                return null;
            }
            sb.append("#");
            for (int i = 0; i < hc.getTriplet().length; i++) {
                sb.append(fillWithZero(Integer.toHexString(hc.getTriplet()[i])));
            }
        }

        return sb.toString();
    }
    
    private static String fillWithZero(String str) {
        if (str != null && str.length() < 2) {
            return "0" + str;
        }
        return str;
    }
    
    static String[] bordesr={"border-top:","border-right:","border-bottom:","border-left:"};
    static String[] borderStyles={"solid ","solid ","solid ","solid ","solid ","solid ","solid ","solid ","solid ","solid","solid","solid","solid","solid"};

    private static  String getBorderStyle(  HSSFPalette palette ,int b,short s, short t){
         
        if(s==0)return  bordesr[b]+borderStyles[s]+"#d0d7e5 1px;";;
        String borderColorStr = convertToStardColor( palette.getColor(t));
        borderColorStr=borderColorStr==null|| borderColorStr.length()<1?"#000000":borderColorStr;
        return bordesr[b]+borderStyles[s]+borderColorStr+" 1px;";
        
    }
    
    private static  String getBorderStyle(int b,short s, XSSFColor xc){
         
         if(s==0)return  bordesr[b]+borderStyles[s]+"#d0d7e5 1px;";;
         if (xc != null && !"".equals(xc)) {
             String borderColorStr = xc.getARGBHex();//t.getARGBHex();
             borderColorStr=borderColorStr==null|| borderColorStr.length()<1?"#000000":borderColorStr.substring(2);
             return bordesr[b]+borderStyles[s]+borderColorStr+" 1px;";
         }
         
         return "";
    }
	
	
    
    /**
     * Excel 转为　HTML
     * @param fileName
     * @param outputFile
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerConfigurationException
     * @throws TransformerException 
     */
    public static void excelToHtml(String fileName, String outputFile)
            throws FileNotFoundException, IOException, ParserConfigurationException, 
                TransformerConfigurationException, TransformerException {
        InputStream is = new FileInputStream(fileName);

        HSSFWorkbook excelBook = new HSSFWorkbook(is);

        ExcelToHtmlConverter ethc = new ExcelToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        ethc.setOutputColumnHeaders(false);
        ethc.setOutputRowNumbers(false);

        ethc.processWorkbook(excelBook);

        Document htmlDocument = ethc.getDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(out);
        
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        out.close();
        
        String htmlStr = new String(out.toByteArray());
        
        htmlStr = htmlStr.replace("<h2>Sheet1</h2>", "")
                         .replace("<h2>Sheet2</h2>", "")
                         .replace("<h2>Sheet3</h2>", "")
                         .replace("<h2>Sheet4</h2>", "")
                         .replace("<h2>Sheet5</h2>", "");
        
        writeFile(htmlStr, outputFile);
    }
    private static void writeFile(String content, String path) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;

        File file = new File(path);

        try {
            fos = new FileOutputStream(file);

            bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            bw.write(content);
        } catch (FileNotFoundException ex) {
            Logger.info(ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
        	 Logger.info(ex.getMessage());
        } catch (IOException ex) {
        	 Logger.info(ex.getMessage());
        } finally {
            try {
                if (null != bw) {
                    bw.close();
                }
                if (null != fos) {
                    fos.close();
                }
            } catch (IOException ex) {
            	 Logger.info(ex.getMessage());
            }

        }
    }
}
