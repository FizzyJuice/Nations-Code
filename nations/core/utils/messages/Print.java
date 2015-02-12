package nations.core.utils.messages;

import nations.core.player_manager.NPlayer;
import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Print {

    public static void toConsole(String message) {
        System.out.println(">> [Nations]: "+message);
    }

    public static void toPlayer(NPlayer nplayer, String message) {
        nplayer.getPlayer().sendMessage(message);
    }

    public static void toActionBar(Player player, String message) {
        IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \""+message.toString()+"\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte)2);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(ppoc);
    }

}
