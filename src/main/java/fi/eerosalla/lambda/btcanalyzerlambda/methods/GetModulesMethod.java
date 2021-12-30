package fi.eerosalla.lambda.btcanalyzerlambda.methods;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import fi.eerosalla.lambda.btcanalyzerlambda.BtcAnalyzerLambda;

public class GetModulesMethod implements Method {

	private final BtcAnalyzerLambda analyzer;
	public GetModulesMethod(BtcAnalyzerLambda analyzer) {
		this.analyzer = analyzer;
	}

	@Override
	public Object run(APIGatewayProxyRequestEvent event, Context context) {
		return analyzer.moduleManager.getResults(null);
	}
}
