package fi.eerosalla.lambda.btcanalyzerlambda.responses;

public class ErrorResponse {

	private final String error;

	public ErrorResponse(String error){
		this.error = error;
	}

	public String toString() {
		return "{error:\"" + this.error + "\"}";
	}
}
