package fi.eerosalla.lambda.btcanalyzerlambda.methods;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import fi.eerosalla.lambda.btcanalyzerlambda.BtcAnalyzerLambda;
import fi.eerosalla.lambda.btcanalyzerlambda.coingecko.CoinGeckoAPI;
import fi.eerosalla.lambda.btcanalyzerlambda.coingecko.MarketChart;
import fi.eerosalla.lambda.btcanalyzerlambda.responses.ErrorResponse;

import java.util.regex.Pattern;

public class RunAnalysisMethod implements Method {

	private static final Pattern STRING_PATTERN = Pattern.compile("^[A-Z]{1,32}$", Pattern.CASE_INSENSITIVE);

	private final BtcAnalyzerLambda analyzer;
	public RunAnalysisMethod(BtcAnalyzerLambda analyzer) {
		this.analyzer = analyzer;
	}

	private static class RunAnalysisRequest {

		String coin;
		String currency;
		Long from;
		Long to;

	}

	@Override
	public Object run(APIGatewayProxyRequestEvent event, Context context) {
		if (event.getBody() == null) {
			return new ErrorResponse("missing input");
		}

		RunAnalysisRequest request;
		try {
			request = BtcAnalyzerLambda.GSON.fromJson(event.getBody(), RunAnalysisRequest.class);
		} catch (Exception ex){
			ex.printStackTrace();

			return new ErrorResponse("invalid input");
		}

		if(request.coin == null || request.currency == null || request.from == null || request.to == null){
			return new ErrorResponse("missing input");
		}

		if(!STRING_PATTERN.matcher(request.coin).matches()){
			return new ErrorResponse("invalid coin input");
		}

		if(!STRING_PATTERN.matcher(request.currency).matches()){
			return new ErrorResponse("invalid currency input");
		}

		MarketChart marketChart;
		try {
			marketChart = CoinGeckoAPI.getDailyMarketChart(request.coin, request.currency, request.from,
					request.to);
		} catch (Exception ex){
			ex.printStackTrace();

			return new ErrorResponse("internal server error");
		}

		if(marketChart == null){
			return new ErrorResponse("internal server error");
		}

		if(marketChart.getPrices().isEmpty()
			|| marketChart.getMarketCaps().isEmpty()
			|| marketChart.getTotalVolumes().isEmpty()){
			return new ErrorResponse("no data available for the selected range of days");
		}

		return analyzer.moduleManager.getResults(marketChart);
	}
}
