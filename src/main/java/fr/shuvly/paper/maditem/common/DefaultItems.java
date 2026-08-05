package fr.shuvly.paper.maditem.common;

import fr.shuvly.paper.maditem.MadItem;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;

/**
 * This enum stores the default items.
 */
public enum DefaultItems
{

    ITEM_NOT_FOUND(
        new MadItem(Material.BARRIER)
            .setName(Component.text(ChatColor.RED + "Item not found."))
            .addLore(Component.text("" + ChatColor.GRAY + ChatColor.ITALIC + "I am a poor dev that can't do his work properly."))
    ),

    ITEM_BUILD_FAIL(
        new MadItem(Material.BARRIER)
            .setName(Component.text(ChatColor.RED + "Item failed to build."))
            .addLore(Component.text("" + ChatColor.GRAY + ChatColor.ITALIC + "I am a poor dev that can't do his work properly."))
    );


    private final MadItem item;


    /**
     * Creates a new Default Item.
     *
     * @param   item    Item
     */
    DefaultItems(MadItem item)
    {
        this.item = item
            .setUntakable(true);
    }


    /**
     * @return  Item
     */
    public MadItem getItem()
    {
        return this.item;
    }

}
