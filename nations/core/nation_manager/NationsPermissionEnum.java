package nations.core.nation_manager;

public enum NationsPermissionEnum {

    STAR("star", "All permissions"),
    ADMIN("admin", "Most permissions"),
    SET_LOCK("set_lock", "Allows the groups players to lock or unlock nation"),
    DISBAND_NATION("disband_nation", "Allows player to disband the nation"),
    INVITE_PLAYERS("invite_players", "Allows the groups players to invite players to the nation"),
    KICK_PLAYERS("kick_players", "Allows the groups players to kick players from the nation"),
    ENEMY_NATION("enemy_nation", "Allows the groups players to enemy nations"),
    ALLY_NATION("ally_nation", "Allows the groups players to ally nations"),
    RANK_PLAYERS("rank_players", "Allows to groups players to rank players within the nation"),
    CANT_BE_RANK("cant_be_rank", "Players within the group cant be ranked by players without the nations.permissions.star permission"),
    NATIONS_CHAT("nations_chat", "Allows groups players to chat in their nations private chat"),
    NATIONS_CHEST("nations_chest", "Allows groups players to access the nations chest"),
    CLAIM_CHUNK("claim_chunk", "Allows groups players to claim chunks for the nation"),
    UNCLAIM_CHUNK("un-claim_chunk", "Allows groups players to un-claim chunks for the nation"),
    POWER_UP_CHUNK("power_up_chunk", "Allows groups players to power up a chunk"),
    BREAK_BLOCKS("break_blocks", "Allows the groups players to break blocks within your nations claimed chunks"),
    PLACE_BLOCKS("place_blocks", "Allows the groups players to place blocks within your nations claimed chunks"),
    INTERACT_BLOCKS("interact_blocks", "Allows the groups players to interact with blocks within your nations claimed chunks");

    private String permission;
    private String description;

    private NationsPermissionEnum(String permission, String description) {
        this.permission = permission;
        this.description = description;
    }

    public String getPermission() {
        return ("nations.permission."+permission).toLowerCase();
    }

    public String getDescription() {
        return description;
    }

}
