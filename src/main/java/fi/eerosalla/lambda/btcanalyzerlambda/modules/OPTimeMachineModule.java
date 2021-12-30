package fi.eerosalla.lambda.btcanalyzerlambda.modules;

import fi.eerosalla.lambda.btcanalyzerlambda.FormatUtil;
import fi.eerosalla.lambda.btcanalyzerlambda.coingecko.MarketChart;

import java.util.List;

public class OPTimeMachineModule extends AbstractModule {

	public OPTimeMachineModule() {
		super(
				"OP Time Machine",
				"Find the best days to buy and sell bitcoins, if the order of the days doesn't matter"
		);
	}

	@Override
	public String getResult(MarketChart marketChart) {
		List<MarketChart.DayEntry> prices = marketChart.getPrices();

		int maxPriceIndex = 0;
		double maxPrice = prices.get(0).getValue();

		int minPriceIndex = 0;
		double minPrice = prices.get(0).getValue();

		for(int i = 1; i < prices.size(); ++i){

			double price = prices.get(i).getValue();

			if(price > maxPrice){
				maxPrice = price;
				maxPriceIndex = i;
			}

			if(price < minPrice){
				minPrice = price;
				minPriceIndex = i;
			}

		}

		double delta = maxPrice - minPrice;

		String result;
		if(delta <= 0){
			result = "No good days to buy/sell found";
		} else {
			result = "Buy on "
					+ FormatUtil.formatDate(prices.get(minPriceIndex).getTimestamp())
					+ " and sell on "
					+ FormatUtil.formatDate(prices.get(maxPriceIndex).getTimestamp())
					+ " <span class=\"no-break\">("
					+ "change: "
					+ FormatUtil.formatMoney(delta, marketChart.getCurrency())
					+ ")</span>";
		}

		return "<p>" + result + "</p>";
	}
}
