package com.monitor.argus.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huxiaolei on 2016/10/17.
 */
public class CharacterUtil {

	/**
	 * ASCII表中可见字符从!开始，偏移位值为33(Decimal)
	 */
	static final char DBC_CHAR_START = 33; // 半角!
	/**
	 * ASCII表中可见字符到~结束，偏移位值为126(Decimal)
	 */
	static final char DBC_CHAR_END = 126; // 半角~
	/**
	 * 全角对应于ASCII表的可见字符从！开始，偏移值为65281
	 */
	static final char SBC_CHAR_START = 65281; // 全角！
	/**
	 * 全角对应于ASCII表的可见字符到～结束，偏移值为65374
	 */
	static final char SBC_CHAR_END = 65374; // 全角～
	/**
	 * ASCII表中除空格外的可见字符与对应的全角字符的相对偏移
	 */
	static final int CONVERT_STEP = 65248; // 全角半角转换间隔
	/**
	 * 全角空格的值，它没有遵从与ASCII的相对偏移，必须单独处理
	 */
	static final char SBC_SPACE = 12288; // 全角空格 12288
	/**
	 * 半角空格的值，在ASCII中为32(Decimal)
	 */
	static final char DBC_SPACE = ' '; // 半角空格

	/**
	 * <PRE>
	 * 半角字符->全角字符转换 
	 * 只处理空格，!到˜之间的字符，忽略其他
	 * </PRE>
	 */
	public static String bj2qj(String src) {
		String pattern = "^[ a-zA-Z0-9\\_.\\-\\,\u4e00-\u9fa5]+$";// 包含任意大小写字母
																	// 数字0-9 空格
																	// 逗号 横杠 点
		Matcher m = Pattern.compile(pattern).matcher(src);
		if (StringUtils.isBlank(src)) {
			return "";
		} else if (m.find()) {
			return src;
		}

		StringBuilder buf = new StringBuilder(src.length());
		char[] ca = src.toCharArray();
		for (int i = 0; i < ca.length; i++) {
			if (ca[i] == DBC_SPACE) { // 如果是半角空格，直接用全角空格替代
				buf.append(SBC_SPACE);
			} else if ((ca[i] >= DBC_CHAR_START) && (ca[i] <= DBC_CHAR_END)) { // 字符是!到~之间的可见字符
				buf.append((char) (ca[i] + CONVERT_STEP));
			} else { // 不对空格以及ascii表中其他可见字符之外的字符做任何处理
				buf.append(ca[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * <PRE>
	 * 数字类型并且小数点后四位正则
	 * </PRE>
	 */
	public static Boolean isNumberForFloat4(String src) {
		String pattern = "^\\-?([0-9]\\d{0,19})$|^\\-?(0|[0-9]\\d{0,19})\\.(\\d{1,4})$";
		Matcher m = Pattern.compile(pattern).matcher(src);
		if (StringUtils.isEmpty(src)) {
			return Boolean.FALSE;
		}
		if (m.find()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * <PRE>
	 * 数字类型
	 * </PRE>
	 */
	public static Boolean isNumber(String src) {
		String pattern = "^\\-?[0-9]+$";
		Matcher m = Pattern.compile(pattern).matcher(src);
		if (StringUtils.isEmpty(src)) {
			return Boolean.FALSE;
		}
		if (m.find()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	/**
	 * 转义html
	 * @param content
	 * @return
	 */
	public static String html(String content) {
		 if(content==null) return "";        
		     String html = content;
		     
		     html = html.replaceAll( "&amp;","&" );
		     html = html.replace(  "&quot;","\"");  //"
		     html = html.replace("&nbsp;&nbsp;", "\t");// 替换跳格
		     html = html.replace("&nbsp;", " " );// 替换空格
		     html = html.replace("&lt;","<" );
		 
		     html = html.replaceAll( "&gt;",">");
		   
		     return html;
		 }
	
	/**
	 * <PRE>
	 * 字符类型
	 * </PRE>
	 */
	public static Boolean isLetterAndNum(String src) {
		String pattern = "^[A-Za-z0-9]+$";
		Matcher m = Pattern.compile(pattern).matcher(src);
		if (StringUtils.isEmpty(src)) {
			return Boolean.FALSE;
		}
		if (m.find()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * <PRE>
	 * 无符号数字类型
	 * </PRE>
	 */
	public static Boolean isNumberN(String src) {
		String pattern = "^[0-9]+$";
		Matcher m = Pattern.compile(pattern).matcher(src);
		if (StringUtils.isEmpty(src)) {
			return Boolean.FALSE;
		}
		if (m.find()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * <PRE>
	 * 是否中文
	 * </PRE>
	 */
	public static Boolean isChinese(String src) {
		String pattern = "^\u4e00-\u9fa5";
		Matcher m = Pattern.compile(pattern).matcher(src);
		if (StringUtils.isEmpty(src)) {
			return Boolean.FALSE;
		}
		if (m.find()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public static void main(String[] args) {
		String src = "{a}123{/a}";
		System.out.println(src.substring(src.lastIndexOf("{a}")+"{a}".length(), src.lastIndexOf("{/a}")));
		//System.out.println("result: " + isNumberForFloat4(src));

		/*
		 * String src ="你好_a, -a_ ,a,b,b_b.---12.3- 12,,,,3_";
		 * System.out.println("result: "+bj2qj(src));
		 * 
		 * src ="_a, -a_ ,a,b,b_b.---12.3- 12,,,,3_"; src="a!@#$%^&()　;'.,";
		 * System.out.println("result: "+bj2qj(src));
		 * src="4111315UD0065-三方比价单,音乐影视作品列表";
		 * System.out.println("result1: "+bj2qj(src));
		 * 
		 * src="4111315UD0065-广告排期"; System.out.println("result2: "+bj2qj(src));
		 * 
		 * src="4111315UD0065-1"; System.out.println("result3: "+bj2qj(src));
		 * 
		 * src="201300166-DM"; System.out.println("result4: "+bj2qj(src));
		 * 
		 * src="201300166-1,2"; System.out.println("result5: "+bj2qj(src));
		 * 
		 * src="201300166-1 2"; System.out.println("result6: "+bj2qj(src));
		 */
	}
	
	/**
	 * <PRE>
	 * 字符串该字符集字节数是否符合字数限制
	 * </PRE>
	 */
	public static Boolean isFitCharsetMaxLength(String src,String charset,int maxLength) {
		try {
			if (StringUtils.isBlank(src)) {
				return Boolean.FALSE;
			}
			if (StringUtils.isNotBlank(src) && StringUtils.isNotBlank(charset)) {
				if (src.getBytes(charset).length>maxLength) {
					return Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 校验日期格式 yyyyMMdd
	 * @param date
	 * @return
	 */
	public static boolean isValidDate(String date) {  
	    try {  
	        int year = Integer.parseInt(date.substring(0, 4));  
	        if (year <= 0)  
	            return false;  
	        int month = Integer.parseInt(date.substring(4, 6));  
	        if (month <= 0 || month > 12)  
	            return false;  
	        int day = Integer.parseInt(date.substring(6, 8));  
	        if (day <= 0 || day > DAYS[month])  
	            return false;  
	        if (month == 2 && day == 29 && !isGregorianLeapYear(year)) {  
	            return false;  
	        }  
	    } catch (Exception e) {  
	        return false;  
	    }  
	    return true;  
	}  
	public static final boolean isGregorianLeapYear(int year) {  
	    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);  
	}

	/**
	 * <PRE>
	 * 字符类型
	 * </PRE>
	 */
	public static Boolean isLetterNumBrac(String src) {
		String pattern = "^[A-Za-z0-9()]+$";
		Matcher m = Pattern.compile(pattern).matcher(src);
		if (StringUtils.isEmpty(src)) {
			return Boolean.FALSE;
		}
		if (m.find()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	
	/**
	 * 数字类型并且小数点后三位正则
	 * @param src
	 * @return
	 */
    public static Boolean isNumberForFloat3(String src) {
        String pattern = "^(0|[1-9]\\d{0,15})$|^(0|[1-9]\\d{0,15})\\.(\\d{1,3})$";
        Matcher m = Pattern.compile(pattern).matcher(src);
        if (StringUtils.isEmpty(src)) {
            return Boolean.FALSE;
        }
        if (m.find()) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
    
	static int[] DAYS = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
}
