package me.despical.itemconverter;

import me.despical.commandframework.Command;
import me.despical.commandframework.CommandArguments;
import me.despical.commandframework.CommandFramework;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    //'kask1to2':
    //    from-item: 1.slottaki item
    //    from-name: "1.slottaki item ismi"
    //    to-item: 2.slottaki item
    //    to-name: "2.slottaki item ismi"
    //    to-lore:
    //      - "2. itemin loru"
    //      - "en altta duracak random küfür"
    //    chance: 50
    //    coin: 100
    //    enchantments:
    //      - PROTECTION_ENVIRONMENTAL:1
    //    conflict-enchantments:
    //      - SHARPNESS
    @Command(
            name = "convert2",
            senderType = Command.SenderType.PLAYER,
            min = 3,
            usage = "/convert <path> <chance> <coin>"
    )
    public void convert2(CommandArguments arguments) {
        final var player = (Player) arguments.getSender();
        final var item1 = player.getInventory().getItem(0);
        final var item2 = player.getInventory().getItem(1);
        final var config = getConfig();
        final var path = "%s.".formatted(arguments.getArgument(0));

        config.set(path + "from-item", item1.getType().name());
        config.set(path + "from-name", item1.getItemMeta().getDisplayName());
        config.set(path + "to-item", item2.getType().name());
        config.set(path + "to-name", item2.getItemMeta().getDisplayName());
        var lore = item2.getItemMeta().getLore() == null ? new ArrayList<String>() : item2.getItemMeta().getLore().stream().collect(Collectors.toList());
        lore.add("BERKE");

        config.set(path + "to-lore", lore);
        config.set(path + "chance", arguments.getArgumentAsInt(1));
        config.set(path + "coin", arguments.getArgumentAsInt(2));

        List<String> enchantments = item2.getEnchantments().entrySet().stream().map(enchantment -> enchantment.getKey().getName() + ":" + enchantment.getValue()).toList();
        config.set(path + "enchantments", enchantments);
        config.set(path + "conflict-enchantments", new ArrayList<>());

        saveConfig();

        player.sendMessage("Items converted.");
    }
}