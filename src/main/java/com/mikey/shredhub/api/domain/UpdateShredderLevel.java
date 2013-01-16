package com.mikey.shredhub.api.domain;

public class UpdateShredderLevel {

	private static int POINTS_FOR_NEW_LEVEL = 100;
	private Shredder shredder;
	int newPoints;
	
	public UpdateShredderLevel(Shredder sh, int newPoints) {
		this.shredder = sh;
		this.newPoints = newPoints;
	}
	
	/** 
	 * @return true if leveled up, false otherwise
	 */
	public boolean advanceXp() {
		ShredderLevel sl = shredder.getLevel();
		if ( shouldLevelUp(sl.getXp())) {
			doLevelUp(sl);
			return true;
		} else {
			sl.setXp( sl.getXp() + newPoints);
			return false;
		}		
	}

	private void doLevelUp(ShredderLevel sl) {
		sl.setLevel( sl.getLevel() + 1);
		sl.setXp((sl.getXp() + newPoints) % POINTS_FOR_NEW_LEVEL);		
	}

	private boolean shouldLevelUp(double xp) {
		return (( xp + newPoints)) >= 100;
	}
}
