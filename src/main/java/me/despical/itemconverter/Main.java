package me.despical.itemconverter;

import me.despical.commandframework.Command;
import me.despical.commandframework.CommandArguments;
import me.despical.commandframework.CommandFramework;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Despical
 * <p>
 * Created at 27.12.2023
 */
public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        var commandFramework = new CommandFramework(this);
        commandFramework.registerCommands(this);
    }

    @Command(
            name = "convert",
            senderType = Command.SenderType.PLAYER,
            min = 3,
            usage = "/convert <id> <slot> <display name>",
            allowInfiniteArgs = true
    )
    public void convert(CommandArguments arguments) {
        final var player = (Player) arguments.getSender();
        final var item = player.getInventory().getItemInMainHand();
        final var config = getConfig();
        final var path = "selector-%d.".formatted(arguments.getArgumentAsInt(0));

        config.set(path + "material", item.getType().name());
        config.set(path + "slot", arguments.getArgumentAsInt(1));
        config.set(path + "hide_attributes", true);
        config.set(path + "display_name", String.join(" ", Arrays.copyOfRange(arguments.getArguments(), 2, arguments.getArgumentsLength())));
        config.set(path + "lore", item.getItemMeta().getLore() == null ? new ArrayList<>() : item.getItemMeta().getLore().stream().map(s -> s.replace('§', '&')).toList());
        config.set(path + "left_click_commands", List.of("[close]"));
        saveConfig();

        player.sendMessage("Item converted.");
    }
}