package com.kaisen.common.result;

import java.io.Serializable;

public class CallServiceResult<T> implements Serializable {
	private static final long serialVersionUID = -6162700658277353549L;
	private ResultEnum resultEnum = ResultEnum.SUCCESS;
	private T returnObject;

	public ResultEnum getResultEnum() {
		return resultEnum;
	}

	public CallServiceResult<T> setResultEnum(ResultEnum resultEnum) {
		this.resultEnum = resultEnum;
		return this;
	}

	public T getReturnObject() {
		return returnObject;
	}

	public CallServiceResult<T> setReturnObject(T returnObject) {
		this.returnObject = returnObject;
		return this;
	}

	public boolean isSuccessful() {
		return this.getResultEnum() != null
				&& this.getResultEnum().getResultCode().intValue() == 0;
	}
}
