package fi.eerosalla.lambda.btcanalyzerlambda.methods;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

public interface Method {

	Object run(APIGatewayProxyRequestEvent event, Context context);

}
