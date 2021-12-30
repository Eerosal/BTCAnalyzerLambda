package fi.eerosalla.lambda.btcanalyzerlambda.modules;

import fi.eerosalla.lambda.btcanalyzerlambda.FormatUtil;
import fi.eerosalla.lambda.btcanalyzerlambda.coingecko.MarketChart;

import java.util.List;

public class DownwardTrendFinderModule extends AbstractModule {

	public DownwardTrendFinderModule() {
		super(
				"Downward trend finder",
				"Find the longest period of consecutive bearish days"
		);
	}

	@Override
	public String getResult(MarketChart marketChart) {
		List<MarketChart.DayEntry> prices = marketChart.getPrices();

		int trendStart = -1;
		int trendLength = 0;

		int longestTrendStart = -1;
		int longestTrendLength = -1;

		for(int i = 1; i < prices.size(); ++i){
			boolean bearish = prices.get(i).getValue() < prices.get(i - 1).getValue();

			if(bearish){
				if(trendLength == 0){
					trendStart = i;
				}

				++trendLength;
				if(trendLength > longestTrendLength){
					longestTrendLength = trendLength;
					longestTrendStart = trendStart;
				}
			} else {
				trendLength = 0;
			}
		}

		String result;
		if(longestTrendLength <= 0){
			result = "No bearish days on selected range";
		} else {
			String startDate = FormatUtil.formatDate(prices.get(longestTrendStart).getTimestamp());

			int endDateIndex = longestTrendStart + longestTrendLength - 1;
			String endDate = FormatUtil.formatDate(prices.get(endDateIndex).getTimestamp());

			result = longestTrendLength
					+ " "
					+ (longestTrendLength == 1? "day" : "days")
					+ " "
					+ "<span class=\"no-break\">"
					+ "(" + startDate + " - " + endDate + ")"
					+ "</span>";
		}

		return "<p>" + result + "</p>";
	}
}
