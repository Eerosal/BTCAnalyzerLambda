package fi.eerosalla.lambda.btcanalyzerlambda.coingecko;

import fi.eerosalla.lambda.btcanalyzerlambda.BtcAnalyzerLambda;
import lombok.SneakyThrows;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class CoinGeckoAPI {

	private static final CloseableHttpClient HTTP_CLIENT;
	static {
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000)
				.setSocketTimeout(5000)
				.build();

		HTTP_CLIENT = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
	}

	@SneakyThrows
	public static MarketChart getMarketChart(String coin, String currency, long from, long to) {
		URIBuilder builder = new URIBuilder("https://api.coingecko.com/api/v3/coins/" + coin + "/market_chart/range");
		builder.setParameter("vs_currency", currency);
		builder.setParameter("from", String.valueOf(from));
		builder.setParameter("to", String.valueOf(to));

		URI uri = builder.build();
		HttpGet request = new HttpGet(uri);

		CloseableHttpResponse response = HTTP_CLIENT.execute(request);
		if(response == null){
			return null;
		}

		if(response.getStatusLine().getStatusCode() != 200){
			response.close();
			return null;
		}

		RawMarketChart rawMarketChart = BtcAnalyzerLambda.GSON.fromJson(
				EntityUtils.toString(response.getEntity()),
				RawMarketChart.class
		);

		response.close();

		return new MarketChart(rawMarketChart, coin.toLowerCase(), currency.toUpperCase());
	}

	// convert to midnight timestamps and remove duplicates
	private static List<MarketChart.DayEntry> convertToDailyData(List<MarketChart.DayEntry> dayEntries){
		final HashSet<Long> seenTimestamps = new HashSet<>();

		return dayEntries.stream().map(dayEntry -> {
			LocalDateTime timestamp = dayEntry.getTimestamp();

			return new MarketChart.DayEntry(
					timestamp.toLocalDate().atStartOfDay(),
					dayEntry.getValue()
			);
		}).filter(
				element ->
						seenTimestamps.add(
								element.getTimestamp().toEpochSecond(ZoneOffset.UTC)
						)
		).collect(Collectors.toList());
	}

	@SneakyThrows
	public static MarketChart getDailyMarketChart(String coin, String currency, long from, long to){
		MarketChart originalChart = getMarketChart(coin, currency, from, to);

		if(originalChart == null){
			return null;
		}

		originalChart.setPrices(convertToDailyData(originalChart.getPrices()));
		originalChart.setMarketCaps(convertToDailyData(originalChart.getMarketCaps()));
		originalChart.setTotalVolumes(convertToDailyData(originalChart.getTotalVolumes()));

		return originalChart;
	}

	private CoinGeckoAPI(){}
}
