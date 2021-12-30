package fi.eerosalla.lambda.btcanalyzerlambda.responses;

import fi.eerosalla.lambda.btcanalyzerlambda.modules.AbstractModule;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ModuleListResponse {

	@Data
	@AllArgsConstructor
	public static class Result {
		private AbstractModule module;
		private String htmlResult;
	}

	private int hash;
	private List<Result> results;

}
