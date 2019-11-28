package jv.android.utils.http;

public class CustomHttpResult {

	boolean success;
	String exception;
	int serverRequestCode;
	String httpResult;
	
	public CustomHttpResult() {
		serverRequestCode = 0;
		httpResult = "";
		success = false;
		exception = "";
	}

	public int getServerRequestCode() {
		return serverRequestCode;
	}

	public void setServerRequestCode(int serverRequestCode) {
		this.serverRequestCode = serverRequestCode;
	}

	public String getHttpResult() {
		return httpResult;
	}

	public void setHttpResult(String httpResult) {
		this.httpResult = httpResult;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}	
}
