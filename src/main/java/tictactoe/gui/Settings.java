package tictactoe.gui;

import java.util.prefs.Preferences;

/**
 * Static object to store window size and location as a preference object.
 */
public class Settings {

	/**
	 * ENUM types to reference settings items that are stored.
	 * Store (x, y) coordinate of left upper window corner. 
	 */
	private enum Pref {
		XCOORDINATE,
		YCOORDINATE
	};

	static private Preferences preferences =
		Preferences.userNodeForPackage(Settings.class);
	
	/**
	 * Returns the last stored x-coordinate of the top left corner of the window.
	 * 
	 * @return The stored x-coordinate of the top left corner, or the default value 450.
	 */
	public static int getXCoordinate() {
		return preferences.getInt(Pref.XCOORDINATE.toString(), 450);
	}

	/**
	 * Returns the last stored y-coordinate of the top left corner of the window.
	 * 
	 * @return The stored y-coordinate of the top left corner, or the default value 10.
	 */
	public static int getYCoordinate() {
		return preferences.getInt(Pref.YCOORDINATE.toString(), 10);
	}

	/**
	 * Saves the window settings.
	 * 
	 * @param x
	 *			The x-coordinate of the top left corner of the window.
	 * @param y
	 *			The y-coordinate of the top left corner of the window.
	 */
	public static void storeSettings(int x, int y) {
		preferences.putInt(Pref.XCOORDINATE.toString(), x);
		preferences.putInt(Pref.YCOORDINATE.toString(), y);
	}

}