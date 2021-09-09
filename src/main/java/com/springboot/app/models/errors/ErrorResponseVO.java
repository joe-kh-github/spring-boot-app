package com.springboot.app.models.errors;

import java.util.List;

public class ErrorResponseVO {

	private List<ErrorDetailsVO> errors;
	private int status;

	public List<ErrorDetailsVO> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorDetailsVO> errors) {
		this.errors = errors;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
