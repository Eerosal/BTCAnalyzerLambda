package fi.eerosalla.lambda.btcanalyzerlambda.coingecko;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class RawMarketChart {

	@SerializedName("prices")
	private List<List<String>> prices;

	@SerializedName("market_caps")
	private List<List<String>> marketCaps;

	@SerializedName("total_volumes")
	private List<List<String>> totalVolumes;

}
