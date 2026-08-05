package fr.shuvly.paper.maditem.common;

import fr.shuvly.paper.maditem.MadItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

public enum DefaultItem
{

    ITEM_NOT_FOUND(
        new MadItem(Material.BARRIER)
            .setName(Component.text("Item not found.").color(NamedTextColor.RED))
            .addLore(Component.text("I am a poor dev that can't do his work properly.").color(NamedTextColor.GRAY))
    ),

    ITEM_BUILD_FAIL(
        new MadItem(Material.BARRIER)
            .setName(Component.text("Item failed to build.").color(NamedTextColor.RED))
            .addLore(Component.text("I am a poor dev that can't do his work properly.").color(NamedTextColor.GRAY))
    );


    private final MadItem item;


    DefaultItem(MadItem item)
    {
        this.item = item.setUntakable(true);
    }


    public MadItem getItem()
    {
        return this.item;
    }

}
