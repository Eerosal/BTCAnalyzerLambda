package fi.eerosalla.lambda.btcanalyzerlambda;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatUtil {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	public static String formatDate(LocalDateTime input){
		return FORMATTER.format(input);
	}

	public static String formatMoney(double input, String currency) {
		return String.format(Locale.ENGLISH, "%,.0f", input)
				+ " "
				+ currency;
	}

	private FormatUtil(){}
}
