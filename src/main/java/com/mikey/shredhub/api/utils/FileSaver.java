package com.mikey.shredhub.api.utils;

import org.springframework.web.multipart.MultipartFile;

public abstract class FileSaver {

	public boolean saveFile(MultipartFile file, String filename ) throws ImageUploadException {
		validateFileName(file);		
		boolean res =  concreteSaveFile(file, filename);
		
		if ( res ) {
			return createAndSaveThumbnail(filename);
		}else {
			return false;
		}
	}
	abstract protected boolean createAndSaveThumbnail(String filename);
	abstract protected void validateFileName(MultipartFile filename) throws ImageUploadException;
	abstract protected boolean concreteSaveFile(MultipartFile file, String filename );
}
