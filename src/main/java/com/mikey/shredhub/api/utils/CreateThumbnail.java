package com.mikey.shredhub.api.utils;

import java.io.DataInputStream;
import java.io.IOException;

public class CreateThumbnail {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(CreateThumbnail.class);

	/**
	 * Function that takes the full path of a video file
	 * and creates a thumbnail jpg image of it, 
	 * by executing the unix program ffmpeg
	 * 
	 * @param videoPath
	 * @return true if all went well
	 */
	public boolean createThumbnail(String videoPath) {
		
		try {
			String ls_str;
			
			// TODO: Create a regex to parse the video path!! 
			// super duper important for security reasons
			if ( !videoPath.matches("(^[\\w/\\.-]+)\\.\\w*$") ) {
				logger.info("Wrong argument: " + videoPath);
				return false;
			}
			
			String strippedPath = videoPath.replaceAll("(^[\\w/\\.-]+)\\.\\w*$", "$1");
			
			StringBuilder cmd =  new StringBuilder("ffmpeg  -itsoffset -4  -i ");
			cmd.append(videoPath).append(" -vcodec mjpeg -vframes 1 -an -f rawvideo ")
			.append(strippedPath).append(".jpg");
			
			logger.info("Will execute: " + cmd.toString() );
			
			Process ls_proc = Runtime.getRuntime().exec(cmd.toString());			

			DataInputStream ls_in = new DataInputStream(
					ls_proc.getInputStream());

			try {
				logger.info("Result: ");
				while ((ls_str = ls_in.readLine()) != null) {
					logger.info(ls_str);
				}
				
				return true;
			} catch (IOException e) {
				logger.info(e.getMessage());
				return false;
			}
		} catch (IOException e1) {
			logger.info(e1.getMessage());
			return false;
		}
	}

}
