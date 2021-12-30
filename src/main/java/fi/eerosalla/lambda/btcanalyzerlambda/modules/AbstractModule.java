package fi.eerosalla.lambda.btcanalyzerlambda.modules;

import fi.eerosalla.lambda.btcanalyzerlambda.coingecko.MarketChart;
import lombok.Getter;

@Getter
public abstract class AbstractModule {

	private final String title;
	private final String description;

	AbstractModule(String title, String description){
		this.title = title;
		this.description = description;
	}

	public abstract String getResult(MarketChart marketChart);

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AbstractModule that = (AbstractModule) o;

		if (!title.equals(that.title)) return false;
		return description.equals(that.description);
	}

	@Override
	public int hashCode() {
		int result = title.hashCode();
		result = 31 * result + description.hashCode();
		return result;
	}
}
