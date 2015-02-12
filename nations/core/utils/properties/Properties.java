package nations.core.utils.properties;

import nations.core.NationsCore;
import nations.core.utils.file.AbstractFile;
import nations.core.utils.file.IfNull;
import nations.core.utils.messages.Print;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Properties extends AbstractFile {

    private NationsCore nc;

    public Properties(String name, NationsCore nc) {
        super(name, IfNull.CREATE);
        this.nc = nc;
        if(!initConfig()){
            nc.disablePlugin();
        }
    }

    public boolean initConfig() {
        try{
            main_folder = nc.getDataFolder();
            sub_folder = new File(main_folder, NationsCore.PROPERTIES_LIBRARY);
            file = new File(sub_folder, name+".yml");
            if(file.exists() && file.isFile()) {
                config = YamlConfiguration.loadConfiguration(file);
                Print.toConsole("Loaded properties config: " + name);
            }else{
                if(if_null.equals(IfNull.CREATE)){
                    main_folder.mkdir();
                    sub_folder.mkdir();
                    file.createNewFile();
                    config = YamlConfiguration.loadConfiguration(file);
                    saveConfig();
                    Print.toConsole("Created properties config: "+name);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
