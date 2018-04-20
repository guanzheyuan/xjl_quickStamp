package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import play.Logger;
import play.Play;
import controllers.comm.BaseController;

/**
 * 和微信平台接口
 * 
 * @author leeyb
 * 
 */
public class HttpClientUtil extends BaseController{

	/**
	 * 请求接口类
	 * @param url
	 * @param requestMethod
	 * @param json
	 * @return
	 */
	public static JSONObject invoke(String url, String requestMethod,
			JSONObject json) {
		HttpURLConnection http = null;
		JSONObject retJsonObject = new JSONObject();
		JSONObject head = new JSONObject();
		retJsonObject.put("HEAD", head);
		head.put("retFlag", "-1");
		try {
			System.setProperty("jsse.enableSNIExtension", "false");

			URL urlGet = new URL(url);
			http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod(requestMethod); // 必须是get方式请求
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒

			// post信息
			if (json != null) {
				OutputStream os = http.getOutputStream();

				os.write(json.toString().getBytes());
				os.close();
			}

			http.connect();
			InputStream is = http.getInputStream();
			String message = IOUtils.toString(is);
			return JSONObject.fromObject(message);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e, "", "");
			retJsonObject.put("error", e.getMessage() + e.getLocalizedMessage()
					+ e.getCause().toString());
		} finally {
			if (http != null) {
				http.disconnect();
			}
		}
		return retJsonObject;
	}
	
	/**
	 * 请求接口类
	 * @param url
	 * @param requestMethod
	 * @param json
	 * @return
	 */
	public static String invoke_cross(String url, String requestMethod,
			JSONObject json) {
		HttpURLConnection http = null;
		JSONObject retJsonObject = new JSONObject();
		JSONObject head = new JSONObject();
		retJsonObject.put("HEAD", head);
		head.put("retFlag", "-1");
		try {
			System.setProperty("jsse.enableSNIExtension", "false");

			URL urlGet = new URL(url);
			http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod(requestMethod); // 必须是get方式请求
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒

			// post信息
			if (json != null) {
				OutputStream os = http.getOutputStream();

				os.write(json.toString().getBytes());
				os.close();
			}

			http.connect();
			InputStream is = http.getInputStream();
			String message = IOUtils.toString(is);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e, "", "");
			retJsonObject.put("error", e.getMessage() + e.getLocalizedMessage()
					+ e.getCause().toString());
		} finally {
			if (http != null) {
				http.disconnect();
			}
		}
		return "";
	}
	/**
	 * 网络请求，返回数据流
	 * @param url 请求的URL地址
	 * @param json POST请求带的JSON对象参数
	 * @param destPath 返回数据流后生成的文件保存路径
	 * @return 生成的文件绝对路径,jpg格式
	 */
	public static String requestInputStreamBackUrl(String url,JSONObject json,String destPath) {
		HttpURLConnection http = null;
		JSONObject retJsonObject = new JSONObject();
		JSONObject head = new JSONObject();
		retJsonObject.put("HEAD", head);
		head.put("retFlag", "-1");
		InputStream istream = null;
		//项目在服务器上的物理路径
		String baseUrl = Play.applicationPath + "";
		//本地不运行项目调试时Play.applicationPath会是null，这时候指定一个默认值
		baseUrl = (StringUtil.isEmpty(baseUrl))?"C:\\tmp":baseUrl;
		//保存保存路径是/开头的
		destPath = destPath.startsWith("/")?destPath:"/".concat(destPath);
		try {
			System.setProperty("jsse.enableSNIExtension", "false");
			URL urlGet = new URL(url);
			http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
			// post信息
			if (json != null) {
				OutputStream os = http.getOutputStream();
				os.write(json.toString().getBytes());
				os.close();
			}
			http.connect();
			istream = http.getInputStream();
			String uploadSysUrl = baseUrl+destPath;
            File saveUrl = new File(uploadSysUrl);
            if(!saveUrl.exists()){
                saveUrl.mkdirs();
            }
            String fileName = saveToImgByInputStream(istream, uploadSysUrl);
            if(StringUtil.isNotEmpty(fileName)){
        		destPath = destPath.endsWith("/")?destPath:destPath.concat("/");
        		destPath += fileName;
            }else{
            	destPath = "";
            }
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e, "", "");
			retJsonObject.put("error", e.getMessage() + e.getLocalizedMessage()
					+ e.getCause().toString());
		} finally {
			if (http != null) {
				http.disconnect();
			}
		}
		return destPath;
	}
	/***
	 * @param instreams 二进制流 
	 * @param imgPath 图片的保存路径 
	 * @return 保存的图片相对，为空字符串时表示保存失败
	 * @throws FileNotFoundException
	 */
	public static String saveToImgByInputStream(InputStream instreams,String imgPath) throws FileNotFoundException{  
		String imgName = "";
		imgName = new Date().getTime() + ".jpg";
		File file = new File(imgPath, imgName);// 可以是任何图片格式.jpg,.png等
		FileOutputStream fos = new FileOutputStream(file);
		if (instreams != null) {
			try {
				byte[] b = new byte[1024];
				int nRead = 0;
				while ((nRead = instreams.read(b)) != -1) {
					fos.write(b, 0, nRead);
				}

			} catch (Exception e) {
				imgName = "";
				e.printStackTrace();
			} finally {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} 
       return imgName;  
   }

	/***
	 * 下载远程图片到本服务器
	 * @param url
	 * @param destPath
	 * @param fileName
	 */
	public static void downloadImg(String url, String destPath, String fileName) {
		HttpURLConnection http = null;
		BufferedInputStream input = null;
		BufferedOutputStream output = null;
		try {
			System.setProperty("jsse.enableSNIExtension", "false");

			URL urlGet = new URL(url);
			http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒

			http.connect();

			http.getResponseCode();

			input = new BufferedInputStream(http.getInputStream());

			File f = new File(destPath, fileName);

			output = new BufferedOutputStream(new FileOutputStream(f));

			byte[] bs = new byte[1024];
			int len = 0;

			while ((len = input.read(bs)) > 0) {
				output.write(bs, 0, len);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (http != null) {
				http.disconnect();
			}
		}
	}
	
	/**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
	/**
	 * 向指定 URL 发送POST方法的请求
	 * @param url 发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			Logger.info("-----------------请求的地址："+conn.getURL());
			Logger.info("-----------------请求的地址param："+param);
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/***
	 * 微信支付接口需要带证书的请求方式【退款申请】
	 * @param url 接口地址
	 * @param xml XML形式的参数
	 * @param mchId 微信支付中的商户号
	 * @return
	 * @throws Exception
	 */
	public static String sendPostCarryWxPayCert(String url,String xml,String mchId) throws Exception  {
		KeyStore keyStore  = KeyStore.getInstance("PKCS12");
		//项目在服务器上的物理路径
		String baseUrl = Play.applicationPath + "";
		//本地不运行项目调试时Play.applicationPath会是null，这时候指定一个默认值
		baseUrl = (StringUtil.isEmpty(baseUrl))?"C:\\tmp":baseUrl;
		baseUrl += "/conf/cert/"+mchId+".p12";
		FileInputStream instream = new FileInputStream(new File(baseUrl));
        try {
            keyStore.load(instream, mchId.toCharArray());
        } finally {
            instream.close();
        }
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, mchId.toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

        String returnText = "";
        try {
        	HttpPost httpPost = new HttpPost(url);
            // post信息
            if (xml != null) {
            	StringEntity se = new StringEntity(xml); 
            	httpPost.setEntity(se);
    		}
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                Logger.info("==接口调用返回状态"+response.getStatusLine().toString());
                if (entity != null) {
                    Logger.info("Response content length: " + entity.getContentLength());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String tmpText;
                    while ((tmpText = bufferedReader.readLine()) != null) {
                    	returnText += tmpText;
                    }
                }else{
                	returnText = "空";
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
		return returnText;
	}
}