package com.quickship.common.response;

import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
	
	private boolean success;
	private String message;
	private T data;

}
