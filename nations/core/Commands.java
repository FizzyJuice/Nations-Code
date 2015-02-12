package nations.core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class Commands implements CommandExecutor {

    private NationsCore nc;

    public Commands(NationsCore nc) {
        this.nc = nc;
        nc.getCommand("nation").setExecutor(this);
    }

    public boolean onCommand(CommandSender cs, Command cmd, String cl, String[] args) {
        if(cmd.getName().equalsIgnoreCase("nation")){
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("disband")){
                    nc.nations_manager.deleteNation(cs);
                }
                if(args[0].equalsIgnoreCase("leave")){
                    nc.nations_manager.leaveNation(cs);
                }
            }else if(args.length == 2){
                if(args[0].equalsIgnoreCase("create") && args[1] != null){
                    nc.nations_manager.createNation(cs, args[1].toString());
                }
                if(args[0].equalsIgnoreCase("invite") && args[1] != null){
                    nc.nations_manager.invitePlayer(cs, args[1].toString());
                }
                if(args[0].equalsIgnoreCase("join") && args[1] != null){
                    nc.nations_manager.joinNation(cs, args[1].toString());
                }
            }
        }
        return true;
    }

}
