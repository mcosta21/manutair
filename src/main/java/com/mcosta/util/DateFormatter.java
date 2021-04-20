package com.mcosta.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {

	private LocalDate localDate;
	private LocalDateTime localDateTime;
	
	public DateFormatter(){}
	
	public DateFormatter(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}
	
	public DateFormatter(LocalDate localDate){
		this.localDate = localDate;
	}
	
	public String LocalDateToString() {
		return this.localDate.toString();
	}
	
	public static String LocalDateToString(LocalDate localDate) {
		return localDate.toString();
	}
	
	public String LocalDateTimeToString() {
		return this.localDateTime.toString();
	}
	
	public static String LocalDateTimeToString(LocalDateTime localDateTime) {
		return localDateTime.toString();
	}
	
	public String LocalDateFormatted() {
		try {
			return this.localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String LocalDateFormatted(LocalDate localDate) {
		return localDate == null ? null : localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
	
	public String LocalDateTimeFormatted() {
		return localDateTime == null ? null : localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
	}
	
	public static String LocalDateTimeFormatted(LocalDateTime localDateTime) {
		return localDateTime == null ? null : localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
	}
	
	public static LocalDate StringToLocalDate(String stringDate) {
		try {
			return LocalDate.parse(stringDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static LocalDateTime StringToLocalDateTime(String stringDate) {
		try {
			return LocalDateTime.parse(stringDate, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		} catch (Exception e) {
			return null;
		}
	}
}
