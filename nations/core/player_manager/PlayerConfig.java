package nations.core.player_manager;

import nations.core.NationsCore;
import nations.core.utils.file.AbstractFile;
import nations.core.utils.file.IfNull;
import nations.core.utils.messages.Print;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class PlayerConfig extends AbstractFile{

    private NationsCore nc;

    public PlayerConfig(String name, NationsCore nc, IfNull if_null) {
        super(name, if_null);
        this.nc = nc;
        initConfig();
    }

    public boolean initConfig() {
        try{
            main_folder = nc.getDataFolder();
            sub_folder = new File(main_folder, NationsCore.PLAYER_LIBRARY);
            file = new File(sub_folder, name+".yml");
            if(file.exists() && file.isFile()){
                config = YamlConfiguration.loadConfiguration(file);
                Print.toConsole("Loaded player config: " + name);
            }else{
                if(if_null.equals(IfNull.CREATE)){
                    main_folder.mkdir();
                    sub_folder.mkdir();
                    file.createNewFile();
                    config = YamlConfiguration.loadConfiguration(file);
                    config.set("nation", NULL_VALUE);
                    config.set("kills", 0);
                    config.set("mob_kills", 0);
                    config.set("deaths", 0);
                    config.set("rating", 0.00);
                    saveConfig();
                    Print.toConsole("Created player config: "+name);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getNationName() {
        return config.getString("nation");
    }

    public void setNatioName(String nation) {
        config.set("nation", nation);
    }

    public int getKills() {
        return config.getInt("kills");
    }

    public void setKills(int kills) {
        config.set("kills", kills);
    }

    public int getMobKills() {
        return config.getInt("mob_kills");
    }

    public void setMobKills(int mob_kills) {
        config.set("mob_kills", mob_kills);
    }

    public int getDeaths() {
        return config.getInt("deaths");
    }

    public void setDeaths(int deaths) {
        config.set("deaths", deaths);
    }

    public double getRating() {
        return config.getDouble("rating");
    }

    public void setRating(double rating) {
        config.set("rating", rating);
    }

}
