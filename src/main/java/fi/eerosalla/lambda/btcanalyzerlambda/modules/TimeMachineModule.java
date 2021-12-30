package fi.eerosalla.lambda.btcanalyzerlambda.modules;

import fi.eerosalla.lambda.btcanalyzerlambda.FormatUtil;
import fi.eerosalla.lambda.btcanalyzerlambda.coingecko.MarketChart;

import java.util.List;

public class TimeMachineModule extends AbstractModule {

	public TimeMachineModule() {
		super(
				"Time Machine",
				"Find the best days to buy and sell bitcoins"
		);
	}

	@Override
	public String getResult(MarketChart marketChart) {
		List<MarketChart.DayEntry> prices = marketChart.getPrices();

		double[] maxPriceArray = new double[prices.size()];
		for(int i = prices.size() - 1; i > 0; --i){
			maxPriceArray[i - 1] = Math.max(maxPriceArray[i], prices.get(i).getValue());
		}

		int buyDateIndex = -1;
		double maxDelta = 0.0D;
		double sellPrice = 0.0D;
		for(int i = 0; i < prices.size() - 1; ++i){
			double delta = maxPriceArray[i] - prices.get(i).getValue();

			if(delta > maxDelta){
				maxDelta = delta;

				buyDateIndex = i;
				sellPrice = maxPriceArray[i];
			}
		}

		String result;

		if(maxDelta <= 0.0D){
			result = "No good days to buy/sell found";
		} else {
			int sellDayIndex = buyDateIndex + 1;
			for(;sellDayIndex < prices.size() - 1; ++sellDayIndex){
				if(prices.get(sellDayIndex).getValue() == sellPrice){
					break;
				}
			}

			result = "Buy on "
					+ FormatUtil.formatDate(prices.get(buyDateIndex).getTimestamp())
			        + " and sell on "
			        + FormatUtil.formatDate(prices.get(sellDayIndex).getTimestamp())
			        + " <span class=\"no-break\">("
					+ "change: "
			        + FormatUtil.formatMoney(maxDelta, marketChart.getCurrency())
					+ ")</span>";
		}

		return "<p>" + result + "</p>";
	}
}
