package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Settings
 * @author Kevin Kamps
 */
public class Settings {

	public static final String CONVERT_FROM = "convert_from";
	public static final String CONVERT_TO = "convert_to";
	public static final String CONVERT_COMMAND = "convert_command";
	public static final String CONVERT_SOURCE = "convert_source";
	public static final String CONVERT_DESITNATION = "convert_destination";
	
	private static final String DELIMITER = "=";
	private static final String COMMENT  = "#";
	
	private static final String SETTINGS_FILE = "settings.ini";	
	
	private static Settings instance = null;
	Map<String, String> settings;
	
	public static final Settings getInstance() {
		if(instance == null) {
			instance = new Settings();
		}
		return instance;
	}

	/**
	 * Constructor
	 */
	public Settings() {
		settings = new HashMap<String, String>();
		load();
	}

	/**
	 * Gets a setting value as string
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return settings.get(key);
	}
	
	/**
	 * Gets a setting value as a float
	 * @param key
	 * @return
	 */
	public float getFloat(String key) {
		return Float.parseFloat(settings.get(key));
	}
	
	/**
	 * Loads all settings
	 */
	private void load() {
		try {
			File settingsFile = new File(SETTINGS_FILE);
			if(!settingsFile.exists()) {
				return; //skip loading file because its not there!
			}
			
			BufferedReader infile = new BufferedReader(new FileReader(settingsFile));
			
			String line = null;
			while((line = infile.readLine()) != null) {
				if(line.startsWith(COMMENT) || line.trim().length() == 0) {
					continue;
				}
				String[] setting = line.split(DELIMITER, 2);
				settings.put(setting[0].trim().toLowerCase(), setting[1].trim());
			}
			
			infile.close();
		} catch(IOException e) {
			System.out.println("Error while reading settings file");
		}
	}

	/**
	 * Overloads the settings in the settings.ini file
	 * @param args
	 */
	public void overloadSettings(String[] args) {
		if(args.length%2 == 1) {
			throw new IllegalArgumentException("You forgot to supply a value for "+args[args.length-1]);
		}
		
		for (int i = 0; i < args.length; i+=2) {
			final String name = args[i];
			final String value = args[i+1];
			settings.put(name.toLowerCase(), value);
		}
	}
}

