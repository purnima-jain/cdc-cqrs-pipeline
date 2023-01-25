package com.purnima.jain.order.exception.apierror;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ApiError {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;

	private String message;

	public ApiError(String message) {
		timestamp = LocalDateTime.now();
		this.message = message;
	}

}
