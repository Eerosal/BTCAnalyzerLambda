package fi.eerosalla.lambda.btcanalyzerlambda.modules;

import fi.eerosalla.lambda.btcanalyzerlambda.FormatUtil;
import fi.eerosalla.lambda.btcanalyzerlambda.coingecko.MarketChart;

import java.util.List;

public class HighestTradingVolumeFinderModule extends AbstractModule {

	public HighestTradingVolumeFinderModule() {
		super(
				"Highest trading volume finder",
				"Find the date with the highest trading volume"
		);
	}

	@Override
	public String getResult(MarketChart marketChart) {
		List<MarketChart.DayEntry> totalVolumes = marketChart.getTotalVolumes();

		int maxVolumeDateIndex = 0;
		double maxVolume = totalVolumes.get(0).getValue();
		for(int i = 1; i < totalVolumes.size(); ++i){
			MarketChart.DayEntry dayEntry = totalVolumes.get(i);

			if(dayEntry.getValue() > maxVolume){
				maxVolume = dayEntry.getValue();
				maxVolumeDateIndex = i;
			}
		}

		String result = FormatUtil.formatDate(totalVolumes.get(maxVolumeDateIndex).getTimestamp())
				+ " "
				+ "<span class=\"no-break\">("
				+ FormatUtil.formatMoney(maxVolume, marketChart.getCurrency())
				+ ")</span>";


		return "<p>" + result + "</p>";
	}
}
