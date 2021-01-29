package xyz.myunco.iloreedit;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static String MESSAGE_PREFIX = Language.MESSAGE_PREFIX;

    public static void commandIItem(String msg, Player p) {
        String arg = getTextRight(msg, " ");
        String[] args = getArgs(arg);
        if ("".equals(args[0])) {
            p.sendMessage(ILoreEdit.iItemCommand.getUsage());
            return;
        }
        ItemStack item = p.getInventory().getItemInMainHand();
        //ItemStack item = p.getItemInHand(); 1.7-1.8用
        ItemMeta im = item.getItemMeta();
        if(item.getType() == Material.AIR || im == null) {
            p.sendMessage(MESSAGE_PREFIX + Language.noItem);
            return;
        }
        if (args.length < 2) {
            p.sendMessage(Language.argsError);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "name":
                im.setDisplayName(Util.getTextRight(arg, args[0] + " ").replace("&", "§"));
                p.sendMessage(MESSAGE_PREFIX + Language.editDisplayName);
                break;
            case "lore":
                List<String> lore = im.getLore();
                if (lore == null) {
                    lore = new ArrayList<>();
                }
                int line;
                switch (args[1].toLowerCase()) {
                    case "add":
                        if (args.length < 3) {
                            p.sendMessage(Language.argsError);
                            return;
                        }
                        lore.add(Util.getTextRight(arg, args[1] + " ").replace("&", "§"));
                        im.setLore(lore);
                        p.sendMessage(MESSAGE_PREFIX + Language.addLore);
                        break;
                    case "set":
                        if (args.length < 4) {
                            p.sendMessage(Language.argsError);
                            return;
                        }
                        if (lore.size() == 0) {
                            p.sendMessage(MESSAGE_PREFIX + Language.noLore);
                            return;
                        }
                        try {
                            line = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            p.sendMessage(MESSAGE_PREFIX + Language.invalidLine);
                            return;
                        }
                        if (line > lore.size()) {
                            p.sendMessage(MESSAGE_PREFIX + Language.errorLine);
                            return;
                        } else if (line == 0) {
                            p.sendMessage(MESSAGE_PREFIX + Language.zeroLine);
                            return;
                        }
                        lore.set(line - 1, Util.getTextRight(arg, args[2] + " ").replace("&", "§"));
                        im.setLore(lore);
                        p.sendMessage(MESSAGE_PREFIX + Language.setLore);
                        break;
                    case "ins":
                        if (args.length < 4) {
                            p.sendMessage(Language.argsError);
                            return;
                        }
                        if (lore.size() == 0) {
                            p.sendMessage(MESSAGE_PREFIX + Language.noLore_ins);
                            return;
                        }
                        try {
                            line = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            p.sendMessage(MESSAGE_PREFIX + Language.invalidLine);
                            return;
                        }
                        if (line > lore.size()) {
                            p.sendMessage(MESSAGE_PREFIX + Language.errorLine);
                            return;
                        } else if (line == 0) {
                            p.sendMessage(MESSAGE_PREFIX + Language.zeroLine);
                            return;
                        }
                        lore.add(line - 1, Util.getTextRight(arg, args[2] + " ").replace("&", "§"));
                        im.setLore(lore);
                        p.sendMessage(MESSAGE_PREFIX + Language.insLore);
                        break;
                    case "del":
                        if (args.length > 3) {
                            p.sendMessage(Language.argsError);
                            return;
                        }
                        if (lore.size() == 0) {
                            p.sendMessage(MESSAGE_PREFIX + Language.noLore_del);
                            return;
                        }
                        if (args.length == 2) {
                            line = lore.size();
                        } else {
                            try {
                                line = Integer.parseInt(args[2]);
                            } catch (NumberFormatException e) {
                                p.sendMessage(MESSAGE_PREFIX + Language.invalidLine);
                                return;
                            }
                            if (line > lore.size()) {
                                p.sendMessage(MESSAGE_PREFIX + Language.errorLine);
                                return;
                            } else if (line == 0) {
                                p.sendMessage(MESSAGE_PREFIX + Language.zeroLine);
                                return;
                            }
                        }
                        lore.remove(line - 1);
                        im.setLore(lore);
                        p.sendMessage(MESSAGE_PREFIX + Language.delLore);
                        break;
                    default:
                        p.sendMessage(Language.argsError);
                        return;
                }
                break;
            case "clear":
                String s = args[1].toLowerCase();
                if (s.equals("name")) {
                    im.setDisplayName(null);
                    p.sendMessage(MESSAGE_PREFIX + Language.clearDisplayName);
                    break;
                } else if (s.equals("lore")) {
                    im.setLore(null);
                    p.sendMessage(MESSAGE_PREFIX + Language.clearLore);
                    break;
                } else {
                    p.sendMessage(Language.argsError);
                    return;
                }
            default:
                p.sendMessage(Language.argsError);
                return;
        }
        item.setItemMeta(im);
    }

    public static List<String> getTabList(String[] args, List<String> list, int index) {
        String arg = args[index].toLowerCase();
        List<String> ret = new ArrayList<>();
        for (String value : list) {
            if (value.startsWith(arg)) {
                ret.add(value);
            }
        }
        return ret.size() == 0 ? null : ret;
    }

    public static String getTextLeft(String str, String subStr) {
        int index = str.indexOf(subStr);
        if (index == -1 || subStr.length() > str.length()) {
            return str;
        }
        return str.substring(0, index);
    }

    public static String getTextRight(String str, String subStr) {
        int index = str.indexOf(subStr);
        if (index == -1 || subStr.length() > str.length()) {
            return "";
        }
        return str.substring(index + subStr.length());
    }

    public static String[] getArgs(String arg) {
        return arg.split(" ");
    }
}
