package utils;
public class Base64 {
   
    
    static private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
            .toCharArray();
   
    //codes里存放的是值表
    //可以这么理解根据码表中的值可以得到编码前的值
    //如codes['A']=0,即为其索引值
    static private byte[] codes = new byte[256];   
    static {
        for (int i = 0; i < 256; i++)
            codes[i] = -1;
        for (int i = 'A'; i <= 'Z'; i++)
            codes[i] = (byte) (i - 'A');
        for (int i = 'a'; i <= 'z'; i++)
            codes[i] = (byte) (26 + i - 'a');
        for (int i = '0'; i <= '9'; i++)
            codes[i] = (byte) (52 + i - '0');
        codes['+'] = 62;
        codes['/'] = 63;
    }

    /**
     * 将原始数据编码为base64编码
     */
    static public String encode(byte[] data) {
        //对于原文的字节数量是3的倍数的时候,转换后的字符串理论上将要比原来的长1/3,即原来的4/3.
        //当原文的字节数不是3的倍数的时候,不足的地方我们补全.
        //故原文补全后的最大长度为 原字符串长度+2
        //编码后的长度为(原字符串长度+2)*3/4
        char[] out = new char[((data.length + 2) / 3) * 4];
        //i指向原字符串,index指向编码后的字符串
        for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
            boolean second = false;//后第一字节是否存在
            boolean third = false;//后第二字节是否存在
            int val = (0xFF & (int) data[i]);
            //左移8位
            val <<= 8;
            if ((i + 1) < data.length) {
                val |= (0xFF & (int) data[i + 1]);
                third = true;
            }
            val <<= 8;
            if ((i + 2) < data.length) {
                val |= (0xFF & (int) data[i + 2]);
                second = true;
            }
            //0x3F为111111,val&0x3F就是取出最右边的六位,然后根据码表对其进行编码
            //如果不存在则直接补=
            out[index + 3] = alphabet[(second ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 2] = alphabet[(third ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 1] = alphabet[val & 0x3F];
            val >>= 6;
            out[index + 0] = alphabet[val & 0x3F];
        }
        return new String(out);
    }

    /**
     * 将base64编码的数据解码成原始数据
     */
    static public String decode(char[] data) {
        int len = ((data.length + 3) / 4) * 3;
        //去除补全位=后,原式数据的长度
        if (data.length > 0 && data[data.length - 1] == '=')
            --len;
        if (data.length > 1 && data[data.length - 2] == '=')
            --len;
        byte[] out = new byte[len];
        int shift = 0;
        int accum = 0;
        int index = 0;
        for (int ix = 0; ix < data.length; ix++) {
            //取得value值就为编码前的值
            int value = codes[data[ix] & 0xFF];
            if (value >= 0) {
                accum <<= 6;
                shift += 6;
                accum |= value;
                //有8位则输出
                if (shift >= 8) {
                    shift -= 8;
                    //accum >> shift即为取出前8位
                    out[index++] = (byte) ((accum >> shift) & 0xff);
                }
            }
        }
        if (index != out.length)
            throw new RuntimeException("miscalculated data length!");
        return new String(out);
    }
    
    
    public static void main(String[] args) throws Exception {
        // 加密成base64
        String strSrc = "123456-";
        String strEncode = new String(Base64.encode(strSrc.getBytes()));
        play.Logger.info(strEncode);
        String strDecode = new String(Base64.decode(strEncode.toCharArray()));
        play.Logger.info(strDecode);
    }
}