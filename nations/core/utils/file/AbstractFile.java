package nations.core.utils.file;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;

public abstract class AbstractFile {

    public static final String NULL_VALUE = "<No_Value_Placeholder>";

    protected String name;
    protected IfNull if_null;
    protected File main_folder;
    protected File sub_folder;
    protected File file;
    protected YamlConfiguration config;

    public AbstractFile(String name, IfNull if_null) {
        this.name = name;
        this.if_null = if_null;
        main_folder = null;
        sub_folder = null;
        file = null;
        config = null;
    }

    public String getName() {
        return name;
    }

    public IfNull getIfNull() {
        return if_null;
    }

    public File getMainFolder() {
        return main_folder;
    }

    public File getSubFolder() {
        return sub_folder;
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public abstract boolean initConfig();

    public boolean saveConfig() {
        try{
            config.save(file);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
