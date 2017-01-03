package model.dto;

import java.io.Serializable;

public class DataRoot<T> implements Serializable {
	private String result;
	private T data;
	private Condition condition;

	public DataRoot(String result, T data,Condition condition) {
		super();
		this.result = result;
		this.data = data;
		this.condition = condition;
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}
}
