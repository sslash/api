package com.mikey.shredhub.api.utils;

import org.springframework.web.multipart.MultipartFile;

public abstract class FileSaver {

	public boolean saveFile(MultipartFile file, String filename ) throws ImageUploadException {
		validateFileName(file);		
		return concreteSaveFile(file, filename);
	}
	
	abstract protected void validateFileName(MultipartFile filename) throws ImageUploadException;
	abstract protected boolean concreteSaveFile(MultipartFile file, String filename );
}
