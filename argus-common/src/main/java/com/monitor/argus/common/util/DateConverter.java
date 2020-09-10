/**
 * 
 */
package com.monitor.argus.common.util;

import org.apache.commons.beanutils.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 日期转换
 * 
 * @Author null
 * @Date 2014-4-28 下午06:24:27
 * 
 */
public class DateConverter implements Converter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(DateConverter.class);

	private static final SimpleDateFormat longFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	private static final SimpleDateFormat defaultFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static final SimpleDateFormat defaultCnFormat = new SimpleDateFormat(
			"yyyy年MM月dd日");

	private static final SimpleDateFormat shortFormat = new SimpleDateFormat(
			"yyyy-M-d");

	private static final SimpleDateFormat shortCnFormat = new SimpleDateFormat(
			"yyyy年M月d日");

	private static final SimpleDateFormat yearFormat = new SimpleDateFormat(
			"yyyy");

	private static final SimpleDateFormat usFormat = new SimpleDateFormat(
			"EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

	public Object convert(Class arg0, Object arg1) {
		if (arg1 != null && arg1 instanceof String) {
			String value = ((String) arg1).trim();
			if (value.equals(""))
				return null;
			try {
				try {
					return defaultFormat.parse(value);
				} catch (ParseException e) {
				}
				try {
					return longFormat.parse(value);
				} catch (ParseException e) {
				}
				try {
					return shortFormat.parse(value);
				} catch (ParseException e) {
				}
				try {
					return defaultCnFormat.parse(value);
				} catch (ParseException e) {
				}
				try {
					return shortCnFormat.parse(value);
				} catch (ParseException e) {
				}
				try {
					return usFormat.parse(value);
				} catch (ParseException e) {
				}
				try {
					return yearFormat.parse(value);
				} catch (ParseException e) {
				}
			} catch (Exception e) {
				logger.error(e.toString());
				return null;
			}
		}
		return arg1;
	}
}
