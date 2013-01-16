package com.mikey.shredhub.api.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

public class LocalImageFileSaver extends FileSaver {
	private String imgPathDeploy;
	private final String imgPath = "/Users/michaekg/michaekg-MyMaster/Shredhub-jsp/src/main/webapp/resources/images/";
	

	public LocalImageFileSaver(String deployPath) {
		imgPathDeploy = deployPath;
	}
	
	@Override
	// TODO: Add some logic here to make sure we get the right file type
	protected void validateFileName(MultipartFile image) throws ImageUploadException {
		//if (!image.getContentType().equals("image/jpeg")) {
			//throw new ImageUploadException("Only JPG images accepted");
		//}
	}

	@Override
	protected boolean concreteSaveFile(MultipartFile file, String filename ) {
	
		try {
			if (!file.isEmpty()) {
				
				saveFile(imgPath + filename, file);
				saveFile(imgPathDeploy + filename, file);
				return true;
			}
		} catch (ImageUploadException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	
	public static void saveFile(String filename, MultipartFile image)
			throws ImageUploadException {
		try {
			File file = new File(filename);
			FileUtils.writeByteArrayToFile(file, image.getBytes());
		} catch (IOException e) {
			System.out.println("Failed to save image: " + e.getMessage() );
			throw new ImageUploadException("Unable to save image", e);
		}
	}


}
