package nations.core.player_manager;

import nations.core.NationsCore;
import nations.core.utils.file.AbstractFile;
import nations.core.utils.file.IfNull;
import nations.core.utils.messages.Print;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

public class PlayerManager extends AbstractFile implements Listener{

    private NationsCore nc;
    private Collection<NPlayer> players;

    public PlayerManager(String name, NationsCore nc) {
        super(name, IfNull.CREATE);
        this.nc = nc;
        nc.getServer().getPluginManager().registerEvents(this, nc);
        if(!initConfig()){
            nc.disablePlugin();
        }
        players = new ArrayList<NPlayer>();
        loadOnlinePlayersData();
    }

    public boolean initConfig() {
        try{
            main_folder = nc.getDataFolder();
            sub_folder = new File(main_folder, name);
            if(!sub_folder.exists()){
                if(if_null.equals(IfNull.CREATE)){
                    main_folder.mkdir();
                    sub_folder.mkdir();
                    Print.toConsole("Created player library: "+name);
                }
            }else{
                Print.toConsole("Loaded player library: " + name);
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Collection<NPlayer> getPlayers() {
        return players;
    }

    public NPlayer getNPlayerByName(String player_name) {
        for(NPlayer nplayer : players) {
            if(nplayer.getPlayer().getName().equals(player_name)) {
                return nplayer;
            }
        }
        return null;
    }

    public NPlayer getNPlayerByPlayer(Player player) {
        for(NPlayer nplayer : players) {
            if(nplayer.getPlayer().equals(player)){
                return nplayer;
            }
        }
        return null;
    }

    public NPlayer getNPlayerByUuid(UUID uuid) {
        for(NPlayer nplayer : players) {
            if(nplayer.getPlayer().getUniqueId().equals(uuid)){
                return nplayer;
            }
        }
        return null;
    }

    public UUID getPlayersUuid(String player_name) {
        OfflinePlayer offline_player = nc.getServer().getOfflinePlayer(player_name);
        return offline_player.getUniqueId();
    }

    public PlayerConfig getPlayerConfig(String config_uuid) {
        PlayerConfig player_config = new PlayerConfig(config_uuid, nc, IfNull.IGNORE);
        return (player_config.getConfig() != null ? player_config : null);
    }

    private void loadOnlinePlayersData() {
        Collection<? extends Player> online_players = Arrays.asList(nc.getServer().getOnlinePlayers());
        System.out.print(online_players.size());
        for(Player player : online_players) {
            PlayerConfig player_config = new PlayerConfig(player.getUniqueId().toString(), nc, IfNull.CREATE);
            NPlayer nplayer = new NPlayer(player, player_config);
            players.add(nplayer);
        }
    }

    @EventHandler
    public void onServerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        NPlayer nplayer = getNPlayerByPlayer(player);
        if(nplayer == null) {
            return;
        }
        players.remove(nplayer);
        nplayer.delete();
        nplayer = null;
    }

    @EventHandler
    public void onServerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        NPlayer nplayer = getNPlayerByPlayer(player);
        if(nplayer != null){
            players.remove(nplayer);
            nplayer.delete();
            nplayer = null;
        }
        PlayerConfig player_config = new PlayerConfig(player.getUniqueId().toString(), nc, IfNull.CREATE);
        nplayer = new NPlayer(player, player_config);
        players.add(nplayer);
    }

    public void saveAll() {
        for(NPlayer nplayer : players) {
            nplayer.getPlayerConfig().saveConfig();
        }
    }

}
