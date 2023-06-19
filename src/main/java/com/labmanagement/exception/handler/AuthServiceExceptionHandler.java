package com.labmanagement.exception.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.labmanagement.bean.Status;
import com.labmanagement.exception.ExceptionMessages;
import com.labmanagement.exception.ExceptionResponse;
import com.labmanagement.exception.UserIsNotFoundException;
import com.labmanagement.exception.UserNotAutharizedException;
import com.labmanagement.response.APIResponse;
import com.labmanagement.response.ErrorResponse;

@RestControllerAdvice
public class AuthServiceExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<APIResponse<ErrorResponse>> handleMethodArgumentException(
			MethodArgumentNotValidException exception) {
		List<ErrorResponse> errors = new ArrayList<>();
		exception.getBindingResult().getFieldErrors().forEach(error -> {
			ErrorResponse errorResponse = new ErrorResponse(error.getField(), error.getDefaultMessage());
			errors.add(errorResponse);
		});
		return new ResponseEntity<>(APIResponse.<ErrorResponse>builder().status(Status.FAILED.toString())
				.message(ExceptionMessages.VALIDATION_FAILED).errors(errors).build(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserNotAutharizedException.class)
	public ResponseEntity<ExceptionResponse<Object>> handleUnAutherizedException(UserNotAutharizedException exception) {
		return new ResponseEntity<>(
				ExceptionResponse.builder().status(Status.FAILED.toString()).message(exception.getMessage()).build(),
				HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(UserIsNotFoundException.class)
	public ResponseEntity<ExceptionResponse<Object>> handleUserNotActiveException(UserIsNotFoundException exception) {
		return new ResponseEntity<>(
				ExceptionResponse.builder().status(Status.FAILED.toString()).message(exception.getMessage()).build(),
				HttpStatus.NOT_FOUND);
	}
}
