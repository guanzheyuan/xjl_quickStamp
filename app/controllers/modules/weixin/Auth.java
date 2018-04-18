package controllers.modules.weixin;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import models.modules.mobile.WxServer;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import play.Logger;
import play.mvc.Controller;
import utils.CheckSignature;
import utils.HttpClientUtil;
import utils.InputStreamUtils;
import utils.SecurityUtil;
import controllers.comm.Sign;
import controllers.modules.mobile.bo.WxServerBo;
public class Auth extends Controller {
	public static void get() {
	}
	public static void post() throws ServletException, IOException {
	}
}
