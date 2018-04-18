package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamUtils {

    public static String getString(InputStream is) throws IOException {

        String reStr = "";
        if (is != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder result = new StringBuilder();
            String temp = null;
            while ((temp = br.readLine()) != null)
                result.append(temp).append("\n");
            reStr = result.toString().trim();
        }
        return reStr;
    }
}
