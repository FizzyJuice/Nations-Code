package nations.core.player_manager;

import nations.core.NationsCore;
import nations.core.nation_manager.Nation;
import nations.core.utils.file.AbstractFile;
import org.bukkit.entity.Player;

public class NPlayer {

    private Player player;
    private PlayerConfig player_config;
    private Nation nation;
    private int kills;
    private int mob_kills;
    private int deaths;
    private double rating;

    public NPlayer(Player player, PlayerConfig player_config) {
        this.player = player;
        this.player_config = player_config;
        nation = NationsCore.getInstance().nations_manager.getNationByName(player_config.getNationName());
        kills = player_config.getKills();
        mob_kills = player_config.getMobKills();
        deaths = player_config.getDeaths();
        rating = player_config.getRating();
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerConfig getPlayerConfig() {
        return player_config;
    }

    public Nation getNation() {
        return nation;
    }

    public void setNation(Nation nation, boolean auto_save) {
        if(nation == null){
            this.nation = null;
            player_config.setNatioName(AbstractFile.NULL_VALUE);
        }else{
            this.nation = nation;
            player_config.setNatioName(nation.getName());
        }
        if(auto_save){
            player_config.saveConfig();
        }
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills, boolean auto_save) {
        this.kills = kills;
        player_config.setKills(kills);
        if(auto_save){
            player_config.saveConfig();
        }
    }

    public int getMobKills() {
        return mob_kills;
    }

    public void setMobKills(int mob_kills, boolean auto_save) {
        this.mob_kills = mob_kills;
        player_config.setMobKills(mob_kills);
        if(auto_save){
            player_config.saveConfig();
        }
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths, boolean auto_save) {
        this.deaths = deaths;
        player_config.setDeaths(deaths);
        if(auto_save){
            player_config.saveConfig();
        }
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating, boolean auto_save) {
        this.rating = rating;
        player_config.setRating(rating);
        if(auto_save){
            player_config.saveConfig();
        }
    }

    public void delete() {
        player_config.saveConfig();
        player = null;
        player_config = null;
        nation = null;
        kills = 0;
        mob_kills = 0;
        deaths = 0;
        rating = 0.00;
    }
}
