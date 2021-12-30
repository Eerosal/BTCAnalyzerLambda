package fi.eerosalla.lambda.btcanalyzerlambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.google.gson.Gson;
import fi.eerosalla.lambda.btcanalyzerlambda.methods.Method;
import fi.eerosalla.lambda.btcanalyzerlambda.methods.GetModulesMethod;
import fi.eerosalla.lambda.btcanalyzerlambda.methods.RunAnalysisMethod;
import fi.eerosalla.lambda.btcanalyzerlambda.modules.DownwardTrendFinderModule;
import fi.eerosalla.lambda.btcanalyzerlambda.modules.HighestTradingVolumeFinderModule;
import fi.eerosalla.lambda.btcanalyzerlambda.modules.OPTimeMachineModule;
import fi.eerosalla.lambda.btcanalyzerlambda.modules.TimeMachineModule;

import java.util.HashMap;
import java.util.Map;


public class BtcAnalyzerLambda {

	public static final Gson GSON = new Gson();

	public final ModuleManager moduleManager = new ModuleManager();

	private final HashMap<String, Method> methodMap = new HashMap<>();

	public BtcAnalyzerLambda(){
		methodMap.put("GetModules", new GetModulesMethod(this));
		methodMap.put("RunAnalysis", new RunAnalysisMethod(this));

		moduleManager.addModule(new DownwardTrendFinderModule());
		moduleManager.addModule(new HighestTradingVolumeFinderModule());
		moduleManager.addModule(new TimeMachineModule());
		moduleManager.addModule(new OPTimeMachineModule());
	}

	public Map<String, Object> mainHandler(APIGatewayProxyRequestEvent event, Context context) {
		// response for API Gateway
		HashMap<String, Object> map = new HashMap<>();

		map.put("statusCode", 200);
		map.put("isBase64Encoded", false);

		HashMap<String, String> headerMap = new HashMap<>();
		headerMap.put("Access-Control-Allow-Origin", "*");
		headerMap.put("Access-Control-Allow-Credentials", "true");

		map.put("headers", headerMap);

		String methodName;
		if(event.getPathParameters() != null
			&& (methodName = event.getPathParameters().get("proxy")) != null){

			Method method = methodMap.get(methodName);
			if(method != null){

				map.put("body", GSON.toJson(method.run(event, context)));

			} else {
				map.put("body", "{\"error\": \"method not found\"}");
			}
		} else {
			map.put("body", "{\"error\": \"method not specified\"}");
		}

		return map;
	}


}