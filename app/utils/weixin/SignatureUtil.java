package utils.weixin;

import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

public class SignatureUtil {

	/**
	 * 生成sign MD5 加密 toUpperCase
	 * 
	 * @param map
	 * @param paternerKey
	 * @return
	 */
	public static String generateSign(Map<String, String> map,
			String paternerKey) {
		Map<String, String> tmap = MapUtil.order(map);
		if (tmap.containsKey("sign")) {
			tmap.remove("sign");
		}
		String str = MapUtil.mapJoin(tmap, false, false);
		return DigestUtils.md5Hex(str + "&key=" + paternerKey).toUpperCase();
	}

}
