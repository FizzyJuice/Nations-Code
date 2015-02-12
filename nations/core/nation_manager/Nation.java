package nations.core.nation_manager;

import nations.core.NationsCore;
import nations.core.player_manager.NPlayer;
import nations.core.player_manager.PlayerConfig;
import nations.core.utils.file.IfNull;
import nations.core.utils.messages.Print;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class Nation extends NationConfig{

    public static Nation instance;

    private String owners_group;
    private String default_group;
    private boolean locked;
    private List<String> members;
    private List<String> groups;
    private List<String> chunks;
    private List<String> enemies;
    private List<String> allies;
    private List<NChunk> nchunks;
    private Collection<NPlayer> invited_players;

    public Nation(String name, NationsCore nc, IfNull if_null) {
        super(name, nc, if_null);
        instance = this;
        owners_group = super.getOwnerGroup();
        default_group = super.getDefaultGroup();
        locked = super.isLocked();
        members = super.getPlayers();
        groups = super.getGroups();
        chunks = super.getChunks();
        enemies = super.getNationsEnemies();
        allies = super.getNationsAllies();
        nchunks = new ArrayList<NChunk>();
        if(!chunks.isEmpty()){
            for(String chunk_name : chunks) {
                NChunk nchunk = new NChunk(this, chunk_name, super.getChunksPower(chunk_name));
                nchunks.add(nchunk);
            }
        }
        invited_players = new ArrayList<NPlayer>();
        Print.toConsole("Loaded nation "+name+". Members: "+members.size()+", chunks: "+chunks.size());
    }

    public String getOwnerGroup() {
        return owners_group;
    }

    public void setOwnerGroup(String group, boolean auto_save) {
        owners_group = group;
        super.setOwnerGroup(group);
        if(auto_save){
            saveConfig();
        }
    }

    public String getDefaultGroup() {
        return default_group;
    }

    public void setDefaultGroup(String group, boolean auto_save) {
        default_group = group;
        super.setDefaultGroup(group);
        if(auto_save){
            saveConfig();
        }
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked, boolean auto_save) {
        this.locked = locked;
        super.setLocked(locked);
        if(auto_save){
            saveConfig();
        }
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members, boolean auto_save) {
        this.members = members;
        super.setPlayers(members);
        if(auto_save){
            saveConfig();
        }
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups, boolean auto_save) {
        this.groups = groups;
        super.setGroups(groups);
        if(auto_save){
            saveConfig();
        }
    }

    public List<String> getChunks() {
        return chunks;
    }

    public void setChunks(List<String> chunks, boolean auto_save) {
        this.chunks = chunks;
        super.setChunks(chunks);
        if(auto_save){
            saveConfig();
        }
    }

    public List<String> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<String> enemies, boolean auto_save) {
        this.enemies = enemies;
        super.setNationsEnemies(enemies);
        if(auto_save) {
            saveConfig();
        }
    }

    public List<String> getAllies() {
        return allies;
    }

    public void setAllies(List<String> allies, boolean auto_save) {
        this.allies = allies;
        super.setNationsAllies(allies);
        if(auto_save){
            saveConfig();
        }
    }

    public List<NChunk> getNChunks() {
        return nchunks;
    }

    public void setNChunks(List<NChunk> nchunks) {
        this.nchunks = nchunks;
    }

    public Collection<NPlayer> getInvitedPlayers() {
        return invited_players;
    }

    public void addInvitedPlayer(final NPlayer nplayer) {
        invited_players.add(nplayer);
        NationsCore.getInstance().getServer().getScheduler().runTaskLater(NationsCore.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(invited_players.contains(nplayer) && nplayer != null){
                    invited_players.remove(nplayer);
                }
            }
        }, 60 * 20L);
    }

    public boolean playerHasPermissions(NPlayer nplayer, NationsPermissionEnum[] permissions_array, boolean owner_counts) {
        String group_name = getPlayersGroup(nplayer.getPlayer().getUniqueId().toString());
        if(group_name != null){
            if(owner_counts){
                if(group_name.equals(owners_group)){
                    return true;
                }
            }
            List<String> players_permissions = getGroupsPermissions(group_name);
            if(!players_permissions.isEmpty()){
                for(NationsPermissionEnum npe : permissions_array){
                    if(players_permissions.contains(npe.getPermission())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void addEnemy(Nation nation) {
        enemies.add(nation.getName());
        this.setEnemies(enemies, true);
    }

    public void removeEnemy(Nation nation) {
        enemies.remove(nation.getName());
        this.setEnemies(enemies, true);
    }

    public NChunk getNChunk(String chunk_name) {
        for(NChunk nchunk : nchunks) {
            if(nchunk.getName().equals(chunk_name)) {
                return nchunk;
            }
        }
        return null;
    }

    public void addPlayer(NPlayer nplayer, String group_name) {
        if(invited_players.contains(nplayer)){
            invited_players.remove(nplayer);
        }
        nplayer.setNation(this, true);
        members.add(nplayer.getPlayer().getUniqueId().toString());
        setMembers(members, false);
        this.setPlayerGroup(nplayer, group_name, true);
        Print.toConsole("Player: "+nplayer.getPlayer().getName()+" has been added to the nation with group: "+group_name);
    }

    public void removePlayer(NPlayer nplayer) {
        if(invited_players.contains(nplayer)){
            invited_players.remove(nplayer);
        }
        nplayer.setNation(null, true);
        members.remove(nplayer.getPlayer().getUniqueId().toString());
        setMembers(members, false);
        this.setPlayerGroup(nplayer, null, true);
    }

    public String getPlayersGroup(NPlayer nplayer) {
        return getPlayersGroup(nplayer.getPlayer().getUniqueId().toString());
    }

    public void setPlayerGroup(NPlayer nplayer, String group_name, boolean auto_save) {
        if(group_name == null){
            config.set("players.data."+nplayer.getPlayer().getUniqueId().toString(), null);
        }else{
            System.out.print("2");
            super.setPlayersGroup(nplayer.getPlayer().getUniqueId().toString(), group_name);
        }
        if(auto_save){
            saveConfig();
        }
    }

    public void deleteNation() {
        for(String string_uuid : members) {
            NPlayer nplayer = NationsCore.getInstance().player_manager.getNPlayerByUuid(UUID.fromString(string_uuid));
            if(nplayer != null) {
                nplayer.setNation(null, true);
            }else{
                PlayerConfig player_config = new PlayerConfig(string_uuid, getPlugin(), IfNull.IGNORE);
                if(player_config.getConfig() != null){
                    player_config.setNatioName(NULL_VALUE);
                    player_config.saveConfig();
                }
            }
        }
        getFile().delete();
        NationsCore.getInstance().nations_manager.getNations().remove(this);
        instance = null;
        name = null;
        main_folder = null;
        sub_folder = null;
        file = null;
        config = null;
        owners_group = null;
        default_group = null;
        locked = false;
        members = null;
        groups = null;
        chunks = null;
        enemies = null;
        allies = null;
        nchunks = null;
        invited_players = null;
    }
}
