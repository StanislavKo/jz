package com.hsd.jz.api.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class PrimitiveUtils {

	public static String getMd5(String value) {
//		return DigestUtils.md5DigestAsHex(value.getBytes());
		return DigestUtils.md5Hex(value);
	}
	
}
