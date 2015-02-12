package nations.core.nation_manager;

import nations.core.NationsCore;
import nations.core.player_manager.NPlayer;
import nations.core.utils.file.AbstractFile;
import nations.core.utils.file.IfNull;
import nations.core.utils.messages.Print;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NationsManager extends AbstractFile{

    public static final String SAFE_ZONE = "Safe-zone";
    public static final String WAR_ZONE = "War-zone";
    public static final String NEUTRAL_ZONE = "Neutral";

    public static char[] VALID_CHARACTERS = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();

    private NationsCore nc;
    private Collection<Nation> nations;

    public NationsManager(String name, NationsCore nc) {
        super(name, IfNull.CREATE);
        this.nc = nc;
        if(!initConfig()){
            nc.disablePlugin();
        }
        nations = new ArrayList<Nation>();
        loadNationsZonesData(new String[] {SAFE_ZONE, WAR_ZONE, NEUTRAL_ZONE});
        loadNationsData();
    }

    public boolean initConfig() {
        try{
            main_folder = nc.getDataFolder();
            sub_folder = new File(main_folder, name);
            if(!sub_folder.exists()){
                if(if_null.equals(IfNull.CREATE)){
                    main_folder.mkdir();
                    sub_folder.mkdir();
                    Print.toConsole("Created nations library: "+name);
                }
            }else{
                Print.toConsole("Loaded nations library: " + name);
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Collection<Nation> getNations() {
        return nations;
    }

    public Nation getNationByName(String nation_name) {
        for(Nation nation : nations) {
            if(nation.getName().equalsIgnoreCase(nation_name)) {
                return nation;
            }
        }
        return null;
    }

    public Nation getNationByChunk(String chunk_name) {
        for(Nation nation : nations) {
            NChunk nchunk = nation.getNChunk(chunk_name);
            if(nchunk != null){
                return nation;
            }else{
                continue;
            }
        }
        return null;
    }

    public String newChunkName(Chunk chunk) {
        return chunk.getWorld().getName()+"<s>"+chunk.getX()+"<s>"+chunk.getZ();
    }

    public String trimYamlFromName(String original_name) {
        char[] original_names_character_array = original_name.toCharArray();
        int cut_at = original_names_character_array.length - 4;
        StringBuilder string_builder = new StringBuilder();
        for(int i = 0; i < cut_at; i++) {
            string_builder.append(original_names_character_array[i]);
        }
        String new_name = string_builder.toString();
        return new_name;
    }

    public void loadNationsData() {
        List<File> files = new ArrayList<File>();
        for(File file : sub_folder.listFiles()) {
            if(file.isFile()){
                files.add(file);
            }
        }
        if(files.isEmpty()){
            return;
        }
        for(File file : files) {
            String nation_name = trimYamlFromName(file.getName());
            if(nation_name.equals(SAFE_ZONE) || nation_name.equals(WAR_ZONE) || nation_name.equals(NEUTRAL_ZONE)) {
                continue;
            }
            Nation nation = null;
            try{
                nation = new Nation(nation_name, nc, IfNull.CREATE);
            }catch(Exception e){
                continue;
            }
            nations.add(nation);
        }
    }

    public void loadNationsZonesData(String[] zones) {
        for(String zone_name : zones) {
            Nation nation = new Nation(zone_name, nc, IfNull.CREATE);
            nations.add(nation);
        }
    }

    public boolean validNationName(String nation_name) {
        char[] nations_name_characters = nation_name.toCharArray();
        for(char c : nations_name_characters) {
            String a = c+"";
            boolean contains = false;
            for(char cc : VALID_CHARACTERS) {
                String b = cc+"";
                if(a.equalsIgnoreCase(b)){
                    contains = true;
                    break;
                }
            }
            if(!contains){
                return false;
            }
        }
        return true;
    }

    public Player isPlayer(CommandSender cs) {
        if(cs instanceof Player){
            return (Player)cs;
        }
        return null;
    }

    public void createNation(CommandSender cs, String nation_name) {
        Player player = isPlayer(cs);
        if(player != null){
            NPlayer nplayer = nc.player_manager.getNPlayerByPlayer(player);
            if(nplayer != null){
                Nation nation = nplayer.getNation();
                if(nation == null){
                    nation = getNationByName(nation_name);
                    if(nation == null){
                        if(validNationName(nation_name)){
                            if(!nation_name.equalsIgnoreCase(SAFE_ZONE) && !nation_name.equalsIgnoreCase(WAR_ZONE)){
                                nation = new Nation(nation_name, nc, IfNull.CREATE);
                                nation.addPlayer(nplayer, nation.getOwnerGroup());
                                nations.add(nation);
                            }
                        }
                    }
                }
            }
        }
    }

    public void deleteNation(CommandSender cs) {
        Player player = isPlayer(cs);
        if(player != null){
            NPlayer nplayer = nc.player_manager.getNPlayerByPlayer(player);
            if(nplayer != null){
                Nation nation = nplayer.getNation();
                if(nation != null){
                    if(nation.playerHasPermissions(nplayer, new NationsPermissionEnum[] {NationsPermissionEnum.STAR, NationsPermissionEnum.DISBAND_NATION}, true)){
                        nation.deleteNation();
                        nation = null;
                    }
                }
            }
        }
    }

    public void invitePlayer(CommandSender cs, String player_name) {
        Player player = isPlayer(cs);
        if(player != null){
            NPlayer nplayer = nc.player_manager.getNPlayerByPlayer(player);
            if(nplayer != null){
                Nation nation = nplayer.getNation();
                if(nation != null){
                    if(nation.playerHasPermissions(nplayer, new NationsPermissionEnum[] {NationsPermissionEnum.STAR, NationsPermissionEnum.ADMIN, NationsPermissionEnum.INVITE_PLAYERS}, true)){
                        Player target = nc.getServer().getPlayer(player_name);
                        if(target != null){
                            NPlayer target_nplayer = nc.player_manager.getNPlayerByPlayer(target);
                            if(target != null){
                                nation.addInvitedPlayer(target_nplayer);
                            }
                        }
                    }
                }
            }
        }
    }

    public void joinNation(CommandSender cs, String nation_name) {
        Player player = isPlayer(cs);
        if(player != null){
            NPlayer nplayer = nc.player_manager.getNPlayerByPlayer(player);
            if(nplayer != null){
                Nation nation = nplayer.getNation();
                if(nation == null){
                   if(!nation_name.equalsIgnoreCase(SAFE_ZONE) && !nation_name.equalsIgnoreCase(WAR_ZONE) && !nation_name.equalsIgnoreCase(NEUTRAL_ZONE)){
                        Nation nation_to_join = getNationByName(nation_name);
                        if(nation_to_join != null){
                            if(nation_to_join.isLocked()){
                                if(nation_to_join.getInvitedPlayers().contains(nplayer)){
                                    nation_to_join.addPlayer(nplayer, nation_to_join.getDefaultGroup());
                                }
                            }else{
                                nation_to_join.addPlayer(nplayer, nation_to_join.getDefaultGroup());
                            }
                        }
                    }
                }
            }
        }
    }

    public void leaveNation(CommandSender cs) {
        Player player = isPlayer(cs);
        if(player != null){
            NPlayer nplayer = nc.player_manager.getNPlayerByPlayer(player);
            if(nplayer != null){
                Nation nation = nplayer.getNation();
                if(nation != null){
                    if(nation.getPlayersGroup(nplayer).equals(nation.getOwnerGroup())){
                        Collection<String> owners_uuids = new ArrayList<String>();
                        for(String uuid : nation.getMembers()){
                            if(nation.getPlayersGroup(uuid).equals(nation.getOwnerGroup())){
                                owners_uuids.add(uuid);
                            }
                        }
                        if(owners_uuids.contains(nplayer.getPlayer().getUniqueId().toString())){
                           if(owners_uuids.size() > 1){
                               nation.removePlayer(nplayer);
                           }else{
                                Print.toPlayer(nplayer, "YOU ARE THE LAST OWNER!");
                                Print.toPlayer(nplayer, "Disband the nation, or promote another player to owner and then leave.");
                           }
                        }
                    }else{
                        nation.removePlayer(nplayer);
                    }
                }
            }
        }
    }

    public void saveAll() {
        for(Nation nation : nations) {
            nation.saveConfig();
        }
    }
}
