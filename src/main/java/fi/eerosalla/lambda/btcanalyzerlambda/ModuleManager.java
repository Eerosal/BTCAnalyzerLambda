package fi.eerosalla.lambda.btcanalyzerlambda;

import fi.eerosalla.lambda.btcanalyzerlambda.coingecko.MarketChart;
import fi.eerosalla.lambda.btcanalyzerlambda.modules.AbstractModule;
import fi.eerosalla.lambda.btcanalyzerlambda.responses.ModuleListResponse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {

	private final List<AbstractModule> modules = new ArrayList<>();

	//client will rebuild the module view if the hash is different (modules have changed)
	@Getter
	private int hash = 7;

	public void addModule(AbstractModule module){
		modules.add(module);

		hash = hash * 31 + module.hashCode();
		hash = hash * 31 + modules.size();
	}

	public ModuleListResponse getResults(MarketChart marketChart){
		return new ModuleListResponse(
				this.getHash(),
				modules.stream().map(
						module -> new ModuleListResponse.Result(
								module,
								marketChart == null? "" : module.getResult(marketChart)
						)
				).collect(Collectors.toList())
		);
	}
}
