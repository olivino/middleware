package distribution;

import java.io.Serializable;

public class RequestHeader implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2086621012352355762L;
	private String context;
	private int requestId;
	private boolean responseExpected;
	private int objectKey;
	private String operation;


	public RequestHeader(final String cont, final int rId, final boolean rExpected, final int oKey, final String op){
		this.setContext(cont);
		this.setRequestId(rId);
		this.setResponseExpected(rExpected);
		this.setObjectKey(oKey);
		this.setOperation(op);
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public boolean isResponseExpected() {
		return responseExpected;
	}

	public void setResponseExpected(boolean responseExpected) {
		this.responseExpected = responseExpected;
	}

	public int getObjectKey() {
		return objectKey;
	}

	public void setObjectKey(int objectKey) {
		this.objectKey = objectKey;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
}
