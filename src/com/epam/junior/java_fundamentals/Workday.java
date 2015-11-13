package com.epam.junior.java_fundamentals;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Workday {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Workday.class.getName());
	private static final String WORKDAY_PROPERTIES = "/workday.properties";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	private static final String VACATION_PREFIX = "vacation.";
	private static final String EXTRA_VACATION_PREFIX = "extra-vacation.";
	private static final String EXTRA_WORKDAY_PREFIX = "extra-workday.";
	
	private static final String CURRENT_YEAR = "2015";

	private static Properties props;
	private static Date dateToCheck;
	private static SimpleDateFormat dateFormat;

	public static void main(String[] args) {
		if (args.length != 1) {
			throw new RuntimeException("We need 1 argument!");
		}

		try {
			LOGGER.debug("Trying to initialize properties.");
			init();
			LOGGER.debug("Initialized properties.");
			
			dateToCheck = dateFormat.parse(args[0]);
			LOGGER.debug("Date to check if its a working day is: {}", dateToCheck.toString());
			
			if (checkIfWorkingDay()) {
				System.out.print(args[0] + " is a workday.");
			} else {
				System.out.print(args[0] + " is not a workday.");
			}
		} catch (ParseException e) {
			throw new RuntimeException("The argument has to be in yyyy-MM-dd format!");
		} catch (IOException e) {
			throw new RuntimeException("Problem with reading the properties file!");
		}
	}

	private static boolean checkIfWorkingDay() {
		boolean isWeekDay = isMondayToFriday();
		boolean defaultReturnValueBasedOnDayOfWeek = isWeekDay ? true : false;
		LOGGER.debug("{} is monday-friday: {}", dateToCheck.toString(), isWeekDay);

		Set<Object> allKeys = props.keySet();
		for (Object keyObject : allKeys) {
			String key = (String) keyObject;
			
			if (key.startsWith(EXTRA_VACATION_PREFIX) || key.startsWith(VACATION_PREFIX) || key.startsWith(EXTRA_WORKDAY_PREFIX)) {
				if (isWeekDay && weekDayIsHoliday(key)) {
					return false;
				} else if (!isWeekDay && weekendIsWorkingDay(key)) {
					return true;
				}
			}
		}

		return defaultReturnValueBasedOnDayOfWeek;
	}

	private static boolean weekDayIsHoliday(String key) {
		if (!(key.startsWith(EXTRA_VACATION_PREFIX) || key.startsWith(VACATION_PREFIX))) return false;
		
		Date day = getDateFromProperty(key);
		
		if (day == null) {
			return false;
		}
		
		Calendar dayCalendar = Calendar.getInstance();
		dayCalendar.setTime(day);
		Calendar dateToCheckCalendar = Calendar.getInstance();
		dateToCheckCalendar.setTime(dateToCheck);
		
		if (key.startsWith(VACATION_PREFIX)) {
			LOGGER.debug("Checking day {}-{} against {} for equality.", dayCalendar.get(Calendar.MONTH) + 1, dayCalendar.get(Calendar.DAY_OF_MONTH),  dateToCheck);
			return dayCalendar.get(Calendar.MONTH) == dateToCheckCalendar.get(Calendar.MONTH) && dayCalendar.get(Calendar.DAY_OF_MONTH) == dateToCheckCalendar.get(Calendar.DAY_OF_MONTH);
		} else {
			LOGGER.debug("Checking day {} against {} for equality.", day, dateToCheck);
			return day.equals(dateToCheck);
		}
	}

	private static boolean weekendIsWorkingDay(String key) {
		if (!(key.startsWith(EXTRA_WORKDAY_PREFIX))) return false;
		
		Date day = getDateFromProperty(key);
		
		if (day == null) {
			return false;
		}
		
		LOGGER.debug("Checking day {} against {} for equality.", day, dateToCheck);
		return day.equals(dateToCheck);
	}
	
	private static Date getDateFromProperty(String key) {
		Date retDate = null;
		
		try {
			if (key.startsWith(VACATION_PREFIX)) {
				retDate = dateFormat.parse(CURRENT_YEAR + "-" + props.getProperty(key));
			} else if (key.startsWith(EXTRA_VACATION_PREFIX) || key.startsWith(EXTRA_WORKDAY_PREFIX)) {
				retDate = dateFormat.parse(props.getProperty(key));
			}
		} catch (ParseException e) {
			LOGGER.error("Date format for {} is wrong in the properties file!", key);
			throw new RuntimeException("The date format in the properties file is incorrect!");
		}
		
		return retDate;
	}

	private static boolean isMondayToFriday() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateToCheck);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
			return true;
		}

		return false;
	}

	private static void init() throws IOException {
		props = new Properties();
		props.load(Workday.class.getResourceAsStream(WORKDAY_PROPERTIES));

		dateFormat = new SimpleDateFormat(DATE_FORMAT);
	}
}
