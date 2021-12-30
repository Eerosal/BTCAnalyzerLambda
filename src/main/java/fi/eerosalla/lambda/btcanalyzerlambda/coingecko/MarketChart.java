package fi.eerosalla.lambda.btcanalyzerlambda.coingecko;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MarketChart {

	@Data
	@AllArgsConstructor
	public static class DayEntry {

		private LocalDateTime timestamp;
		private double value;
	}

	private List<DayEntry> prices;
	private List<DayEntry> marketCaps;
	private List<DayEntry> totalVolumes;

	private static List<DayEntry> parse(List<List<String>> rawList) throws NumberFormatException {
		return rawList.stream().map(
				list -> new DayEntry(
						LocalDateTime.ofEpochSecond(
								// millis -> seconds
								Long.parseLong(list.get(0)) / 1000L, 0, ZoneOffset.UTC
						),
						Double.parseDouble(list.get(1))
				)
		).collect(Collectors.toList());
	}

	private String coin;
	private String currency;

	public MarketChart(RawMarketChart rawMarketChart, String coin, String currency) throws NumberFormatException {
		prices = parse(rawMarketChart.getPrices());
		marketCaps = parse(rawMarketChart.getMarketCaps());
		totalVolumes = parse(rawMarketChart.getTotalVolumes());

		this.coin = coin;
		this.currency = currency;
	}


}
