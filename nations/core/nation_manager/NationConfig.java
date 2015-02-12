package nations.core.nation_manager;

import nations.core.NationsCore;
import nations.core.utils.file.AbstractFile;
import nations.core.utils.file.IfNull;
import nations.core.utils.messages.Print;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class NationConfig extends AbstractFile{

    private NationsCore nc;

    public NationConfig(String name, NationsCore nc, IfNull if_null) {
        super(name, if_null);
        this.nc = nc;
        initConfig();
    }

    public NationsCore getPlugin() {
        return nc;
    }

    public boolean initConfig() {
        try{
            main_folder = nc.getDataFolder();
            sub_folder = new File(main_folder, NationsCore.NATIONS_LIBRARY);
            file = new File(sub_folder, name+".yml");
            if(file.exists() && file.isFile()){
                config = YamlConfiguration.loadConfiguration(file);
                Print.toConsole("Loaded nations config: "+name);
            }else{
               if(if_null.equals(IfNull.CREATE)){
                   main_folder.mkdir();
                   sub_folder.mkdir();
                   file.createNewFile();
                   config = YamlConfiguration.loadConfiguration(file);
                   config.set("owner_group", "Owner");
                   config.set("default_group", "Default");
                   config.set("locked", true);
                   List<String> players = new ArrayList<String>();
                   config.set("players.list", players);
                   config.set("players.data", NULL_VALUE);
                   List<String> groups = new ArrayList<String>();
                   groups.add("Owner");
                   groups.add("Default");
                   config.set("groups.list", groups);
                   List<String> owners_permissions = new ArrayList<String>();
                   owners_permissions.add(NationsPermissionEnum.STAR.getPermission());
                   config.set("groups.data.Owner.permissions", owners_permissions);
                   List<String> default_permissions = new ArrayList<String>();
                   default_permissions.add(NationsPermissionEnum.NATIONS_CHAT.getPermission());
                   default_permissions.add(NationsPermissionEnum.NATIONS_CHEST.getPermission());
                   default_permissions.add(NationsPermissionEnum.BREAK_BLOCKS.getPermission());
                   default_permissions.add(NationsPermissionEnum.PLACE_BLOCKS.getPermission());
                   default_permissions.add(NationsPermissionEnum.INTERACT_BLOCKS.getPermission());
                   config.set("groups.data.Default.permissions", default_permissions);
                   List<String> chunks = new ArrayList<String>();
                   config.set("chunks.list", chunks);
                   config.set("chunks.data", NULL_VALUE);
                   config.set("nations.relations.enemies",new ArrayList<String>());
                   config.set("nations.relations.allies", new ArrayList<String>());
                   saveConfig();
                   Print.toConsole("Created nations config: "+name);
               }
        }
    }catch(Exception e){
        e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getOwnerGroup() {
        return config.getString("owner_group");
    }

    public void setOwnerGroup(String group) {
        config.set("owner_group", group);
    }

    public String getDefaultGroup() {
        return config.getString("default_group");
    }

    public void setDefaultGroup(String group) {
        config.set("default_group", group);
    }

    public boolean isLocked() {
        return config.getBoolean("locked");
    }

    public void setLocked(boolean locked) {
        config.set("locked", locked);
    }

    public List<String> getPlayers() {
        return config.getStringList("players.list");
    }

    public void setPlayers(List<String> players) {
        config.set("players.list", players);
    }

    public String getPlayersGroup(String uuid) {
        return config.getString("players.data."+uuid+".group");
    }

    public void setPlayersGroup(String uuid, String group) {
        config.set("players.data."+uuid+".group", group);
    }

    public List<String> getGroups() {
        return config.getStringList("groups.list");
    }

    public void setGroups(List<String> groups) {
        config.set("groups.list", groups);
    }

    public List<String> getGroupsPermissions(String group) {
        return config.getStringList("groups.data."+group+".permissions");
    }

    public void setGroupsPermissions(String group, List<String> permissions) {
        config.set("groups.data."+group+".permissions", permissions);
    }

    public List<String> getChunks() {
        return config.getStringList("chunks.list");
    }

    public void setChunks(List<String> chunks) {
        config.set("chunks.list", chunks);
    }

    public double getChunksPower(String chunk_name) {
        return config.getDouble("chunks.data."+chunk_name+".power");
    }

    public void setChunkPower(String chunk_name, double power) {
        config.set("chunks.data."+chunk_name+".power", power);
    }

    public List<String> getNationsEnemies() {
        return config.getStringList("nations.relations.enemies");
    }

    public void setNationsEnemies(List<String> enemies) {
        config.set("nations.relations.enemies", enemies);
    }

    public List<String> getNationsAllies() {
        return config.getStringList("nations.relations.allies");
    }

    public void setNationsAllies(List<String> enemies) {
        config.set("nations.relations.allies", enemies);
    }
}