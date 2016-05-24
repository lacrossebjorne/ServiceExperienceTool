package com.set.dao.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * A class that converts date/time from/to the ISO8601-format, with this pattern "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'"
 * @author Emil
 *
 */
public class IsoDateConverter {
	SimpleDateFormat dateFormatUTC;
	TimeZone timeZoneUTC;
	
	public IsoDateConverter() {
		this.dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.SSS'Z'");
		this.timeZoneUTC = TimeZone.getTimeZone("UTC");
		dateFormatUTC.setTimeZone(this.timeZoneUTC);
	}
	
	/**
	 * Returns a parsed UTC-string in format "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'"
	 * @param millis time since epoch
	 * @return a parsed UTC-string
	 */
	public String parseToUTCString(long millis) {
		Date dateNow = new Date(millis);
		String formattedIsoText = dateFormatUTC.format(dateNow);
		return formattedIsoText;
	}
	
	/**
	 * Returns a parsed UTC-string in format "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'"
	 * @param date a Date-object
	 * @return a parsed UTC-string
	 */
	public String parseToUTCString(Date date) {
		return parseToUTCString(date.getTime());
	}
	
	/**
	 * Returns a date-object parsed from a string in format "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'"
	 * @param parseable the string to parse
	 * @return a Date-object parsed from the input string, or null if the @param parseable is null
	 */
	public Date parseToUTCDate(String parseable) {
		Date date = null;
		
		if (parseable != null) {
			try {
				date = this.dateFormatUTC.parse(parseable);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		} 
		return date;
	}
}
