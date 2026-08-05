package fr.shuvly.paper.maditem;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import fr.shuvly.paper.maditem.common.DefaultItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class MadSkull extends MadItem
{

    /**
     * Creates a new HashSkull.
     */
    public MadSkull()
    {
        this(1);
    }

    /**
     * Creates a new HashSkull.
     *
     * @param   amount  Amount of items
     */
    public MadSkull(int amount)
    {
        super(new ItemStack(Material.PLAYER_HEAD, amount));
    }

    /**
     * Creates a HashSkull from an existing {@link MadItem}.
     *
     * @param   item    Item
     */
    public MadSkull(MadItem item)
    {
        this(item.getItemStack().getAmount());
        super.setItemMeta(item.getItemMeta());
    }


    /**
     * @return  Item's meta as {@link SkullMeta}.
     */
    private SkullMeta getSkullMeta()
    {
        return (SkullMeta) super.getItemMeta();
    }

    /**
     * Sets skull's texture to a player's head.
     * To set skull's texture to a custom texture,
     * use {@link MadSkull#setTexture(String)}.
     * </p>
     * {@link Bukkit#getOfflinePlayer(String)} is used because
     * no other options are available right now.
     * One solution is to get targeted player's UUID from
     * Mojang's API, but it is kinda overkill.
     *
     * @param   playerName  Targeted player's name
     * @return  Itself
     */
    public MadSkull setOwner(String playerName)
    {
        this.getSkullMeta().setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        return this;
    }

    public MadItem setTexture(String texture)
    {
        if (texture.isEmpty()) {
            return this;
        }

        try {
            final SkullMeta skullMeta = this.getSkullMeta();
            final PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());

            profile.setProperty(new ProfileProperty("textures", texture));
            skullMeta.setPlayerProfile(profile);
            
            return this;
        } catch (Exception exception) {
            return DefaultItems.ITEM_BUILD_FAIL.getItem();
        }
    }

}
