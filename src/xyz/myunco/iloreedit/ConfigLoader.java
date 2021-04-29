package xyz.myunco.iloreedit;

import java.lang.reflect.Field;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigLoader {
    public static ILoreEdit pl = ILoreEdit.plugin;
    public static FileConfiguration config = pl.getConfig();

    public static void load() {
        pl.saveDefaultConfig();
        for(Field f:Language.class.getFields()) {
        	try {
        		if (config.getString("message."+f.getName())!=null)
        			f.set(null, config.getString("message."+f.getName()));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        StringBuilder helpMsg = new StringBuilder();
        config.getStringList("message.helpMsg").forEach(value -> helpMsg.append(value).append("\n"));
        Language.helpMsg = helpMsg.toString();
    }
}
