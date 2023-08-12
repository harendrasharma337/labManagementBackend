package com.labmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
public class FileUploadConfig {

	@Value("${file.uploadDir}")
	private String uploadDir;
}