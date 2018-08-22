package ch.htwchur.coltero.dashboard.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;

public class DateHelper {

	public static final DateFormat df = new SimpleDateFormat("yyyy-MM");

	/**
	 * Get all Months between today and start date
	 * @param startDate
	 * @return
	 * @throws ParseException 
	 */
	public static List<Date> getAllMonthAsDate(Date startDate)  {
		String start = df.format(startDate);
		LocalDate fromDate = LocalDate.parse(start + "-01");
		String end = df.format(new Date());
		LocalDate toDate = LocalDate.parse(end + "-01");
		List<Date> allDatesBetween = new ArrayList<>();
		while(fromDate.isBefore(toDate)) {
			allDatesBetween.add(fromDate.toDate());
			fromDate = fromDate.plusMonths(1);
		}
		allDatesBetween.add(fromDate.plusMonths(1).toDate());
		return allDatesBetween;
	}
}
