package com.mikey.shredhub.api.utils;

public class ImageUploadException extends Exception { 
	
	public ImageUploadException(String errorMessage) {
		super(errorMessage);
	}

	public ImageUploadException(String errorMessage, Exception e) {
		super(errorMessage, e);
	}
}
