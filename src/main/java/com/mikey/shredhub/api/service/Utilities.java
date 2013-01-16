package com.mikey.shredhub.api.service;

import java.sql.Date;
import java.util.Calendar;

public class Utilities {

	public static Date getNow() {
		return new Date( Calendar.getInstance().getTime().getTime() );
	}
}
