package utils;

import java.util.Arrays;
public class BitHelper {

	/** 
     * 将byte转换为一个长度为8的byte数组，数组每个值代表bit 
     */  
    public static byte[] getBooleanArray(byte b) {  
        byte[] array = new byte[8];  
        for (int i = 7; i >= 0; i--) {  
            array[i] = (byte)(b & 1);  
            b = (byte) (b >> 1);  
        }  
        return array;  
    }  
    /** 
     * 把byte转为字符串的bit 
     */  
    public static String byteToBit(byte b) {  
        return ""  
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)  
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)  
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)  
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);  
    }  
    /** 
     * 把byte转为字符串的bit 
     */  
    public static String byteToBit_16(byte b) {  
    	return ""  
    			+ (byte) ((b >> 31) & 0x1) + (byte) ((b >> 30) & 0x1)  
    			+ (byte) ((b >> 29) & 0x1) + (byte) ((b >> 28) & 0x1)  
    			+ (byte) ((b >> 27) & 0x1) + (byte) ((b >> 26) & 0x1)  
    			+ (byte) ((b >> 25) & 0x1) + (byte) ((b >> 24) & 0x1) 
    			
    			+ (byte) ((b >> 23) & 0x1) + (byte) ((b >> 22) & 0x1)  
    			+ (byte) ((b >> 21) & 0x1) + (byte) ((b >> 20) & 0x1)  
    			+ (byte) ((b >> 19) & 0x1) + (byte) ((b >> 18) & 0x1)  
    			+ (byte) ((b >> 17) & 0x1) + (byte) ((b >> 16) & 0x1) 
    			
    			+ (byte) ((b >> 15) & 0x1) + (byte) ((b >> 14) & 0x1)  
    			+ (byte) ((b >> 13) & 0x1) + (byte) ((b >> 12) & 0x1)  
    			+ (byte) ((b >> 11) & 0x1) + (byte) ((b >> 10) & 0x1)  
    			+ (byte) ((b >> 9) & 0x1) + (byte) ((b >> 8) & 0x1) 
    			
    			+ (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)  
    			+ (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)  
    			+ (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)  
    			+ (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);  
    }  
    
    public static String int2Binary(int i) {  
        // 整数长度  
        int intLen = 8;  
        String s = "";  
        boolean minus = (i < 0);  
        int length = 0;  
        //如果是负数，求其反码  
        if (minus)  
            i = -i;  
        while (i > 0) {  
            if (i % 2 != 0)  
                s = "1" + s;  
            else  
                s = "0" + s;  
            i /= 2;  
            length++;  
        }  
          
        if (minus)  
            for (i = 0; i < intLen - length; i++)  
                s = "1" + s;  
//        System.out.println(s); 
        
        return s;
    } 
    
    public static String appendString(int size, String str)
    {
    	int leng = str.length();
    	if(leng>=size)
    		return str;
    	for(int i=0;i<size-leng;i++)
    	{
    		str="0"+str;
    	}
    	return str ;
    }
    
    public static void main(String[] args) {  
        byte b = 0x35; // 0011 0101  
        // 输出 [0, 0, 1, 1, 0, 1, 0, 1]  
        System.out.println(Arrays.toString(getBooleanArray(b)));  
        // 输出 00110101  
        System.out.println(byteToBit(b));  
        System.out.println("%%% "+byteToBit((byte)213));  
        // JDK自带的方法，会忽略前面的 0  
        System.out.println(Integer.toBinaryString(0x35));  
        
        String ssid = "0";
        int issid = Integer.parseInt(ssid, 2);
        System.out.println("issid="+issid);
        
//        System.out.println("%%% "+byteToBit_16((byte)1024));
        System.out.println("----------------------------------------");
        
        System.out.println("----------------------------------------"+appendString(10,int2Binary(issid)));
        System.out.println("----------------------------------------"+appendString(10,int2Binary(1024)));
        System.out.println("----------------------------------------"+appendString(10,int2Binary(512)));
        

    }  
	

}
