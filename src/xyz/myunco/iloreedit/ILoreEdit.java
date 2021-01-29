package xyz.myunco.iloreedit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class ILoreEdit extends JavaPlugin {
    public static final String PREFIX = "§3[§9ILoreEdit§3] ";
    public static String version;
    public static ILoreEdit plugin;
    public static List<String> list0 = Arrays.asList("help", "version", "reload");
    public static List<String> list1 = Arrays.asList("name", "lore", "clear");
    public static List<String> list2 = Arrays.asList("add", "set", "ins", "del");
    public static List<String> list3 = Arrays.asList("name", "lore");
    public static ProtocolManager pm;
    public static PluginCommand iItemCommand;

    @Override
    public void onEnable() {
        plugin = this;
        version = getDescription().getVersion();
        ConfigLoader.load();
        iItemCommand = Bukkit.getPluginCommand("iItem");
        iItemCommand.setExecutor(this);
        pm = ProtocolLibrary.getProtocolManager();
        pm.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.CHAT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.CHAT) {
                    String msg = event.getPacket().getStrings().read(0);
                    if (msg.startsWith("/")) {
                        switch (Util.getTextLeft(msg, " ").toLowerCase()) {
                            case "/iitem":
                            case "/it":
                            case "/ll":
                            case "/ii":
                                Player p = event.getPlayer();
                                if (p.hasPermission("iloreedit.use")) {
                                    Util.commandIItem(msg, p);
                                }
                        }
                    }
                }
            }
        });
        Bukkit.getConsoleSender().sendMessage(PREFIX + Language.enable);
    }

    @Override
    public void onDisable() {
        pm.removePacketListeners(this);
        Bukkit.getConsoleSender().sendMessage(PREFIX + Language.disable);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        switch (cmd.getName()) {
            case "iItem":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Language.MESSAGE_PREFIX + Language.canOnlyPlayer);
                }
                break;
            case "ILoreEdit":
                if (args.length != 1) {
                    return false;
                }
                switch (args[0].toLowerCase()) {
                    case "help":
                        sender.sendMessage(Language.helpMsg);
                        break;
                    case "version":
                        sender.sendMessage(Language.MESSAGE_PREFIX + "§bVersion§e: §a" + version);
                        break;
                    case "reload":
                        ConfigLoader.load();
                        sender.sendMessage(Language.MESSAGE_PREFIX + Language.reloaded);
                }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        //item1name2
        //item1lore2
        if ("iItem".equals(cmd.getName())) {
            if (args.length == 1) {
                if (args[0].equals("")) {
                    return list1;
                }
                return Util.getTabList(args, list1, 0);
            } else if (args.length == 2) {
                switch (args[0]) {
                    case "lore":
                        if (args[1].equals("")) {
                            return list2;
                        }
                        return Util.getTabList(args, list2, 1);
                    case "clear":
                        if (args[1].equals("")) {
                            return list3;
                        }
                        return Util.getTabList(args, list3, 1);
                }
            }
        } else if ("ILoreEdit".equals(cmd.getName())) {
            if (args.length == 1) {
                if ("".equals(args[0])) {
                    return list0;
                }
                return Util.getTabList(args, list0, 0);
            }
        }
        return null;
    }
}
