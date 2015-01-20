package com.kaisen.common.util;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class SignUtil {
	/**
	 * 验证签名
	 * 
	 * @param sign
	 * @param paramsMap
	 * @return
	 */
	public static boolean verifySign(String sign, String secret,
			Map<String, String> paramsMap) {
		if (StringUtils.isBlank(sign) || StringUtils.isBlank(secret)
				|| paramsMap.size() == 0) {
			return false;
		}
		String params = "";
		Object[] keyArray = paramsMap.keySet().toArray();
		Arrays.sort(keyArray);
		for (int i = 0; i < keyArray.length; i++) {
			Object values = paramsMap.get(keyArray[i]);
			if (values == null) {
				continue;
			}
			String[] value = new String[1];
			if (values instanceof String[]) {
				value = (String[]) values;
			} else {
				value[0] = values.toString();
			}
			params += keyArray[i] + "=" + value[0];
			params += "&";
		}
		params = params.substring(0, params.length() - 1);
		if (sign.equals(DigestUtils.md5Hex(params + secret))) {
			return true;
		}

		return false;
	}
}
