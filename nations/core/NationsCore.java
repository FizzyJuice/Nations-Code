package nations.core;

import nations.core.nation_manager.NationsManager;
import nations.core.player_manager.PlayerManager;
import nations.core.utils.messages.Print;
import nations.core.utils.properties.Properties;
import org.bukkit.plugin.java.JavaPlugin;


public class NationsCore extends JavaPlugin{

    public static final String PROPERTIES_LIBRARY = "Properties";
    public static final String NATIONS_LIBRARY = "Nations Library";
    public static final String PLAYER_LIBRARY = "Players Library";

    public static NationsCore instance;
    public Properties properties;
    public NationsManager nations_manager;
    public PlayerManager player_manager;
    public Commands commands;

    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        properties = new Properties(PROPERTIES_LIBRARY, this);
        nations_manager = new NationsManager(NATIONS_LIBRARY, this);
        player_manager = new PlayerManager(PLAYER_LIBRARY, this);
        commands = new Commands(this);
        long end = System.currentTimeMillis();
        long time = end - start;
        Print.toConsole("Plugin start up: "+time+" ms.");
    }

    public void onDisable() {
        nations_manager.saveAll();
        player_manager.saveAll();
    }

    public static NationsCore getInstance() {
        return instance;
    }

    public void disablePlugin() {
        Print.toConsole("DISABLING PLUGIN!");
        getServer().getPluginManager().disablePlugin(this);
    }
}
