package fr.shuvly.paper.madgui.handler.hold;

import fr.shuvly.paper.madgui.manager.MadGuiAbstractManager;
import fr.shuvly.paper.maditem.MadItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class HoldManager
    extends MadGuiAbstractManager<HoldHandler>
{

    /**
     * Executes the interaction actions linked to the held item.
     *
     * @param	player  Player
     * @param	item	Item
     */
    public void processHold(Player player, ItemStack item, boolean activeItem)
    {
        final String actionId = MadItem.getActionId(item);
        if (actionId == null) {
            return;
        }
        final int slot = player.getInventory().getHeldItemSlot();
        final List<HoldHandler> holdHandlers = super.getHandlers().get(actionId);

        if (holdHandlers == null || holdHandlers.isEmpty()) {
            return;
        }

        if (activeItem) {
            holdHandlers.forEach((HoldHandler handler) ->
                handler.getHoldAction().execute(player, item, slot));
        } else {
            holdHandlers.forEach((HoldHandler handler) ->
                handler.getNotHoldAction().execute(player, item, slot));
        }
    }

    /**
     * Refreshes Armor state. Because when directly setting an
     * armor to a player, slot modification event is not fired.
     *
     * @param   player  Player
     */
    public void refreshArmorState(Player player)
    {
        final PlayerInventory inventory = player.getInventory();

        for (ItemStack armorPiece : inventory.getArmorContents()) {
            if (armorPiece == null ||
                armorPiece.getType() == Material.AIR ||
                !armorPiece.hasItemMeta()) {
                continue;
            }
            this.processHold(
                player,
                armorPiece,
                super.getHandlers().containsKey(MadItem.getActionId(armorPiece))
            );
        }
    }

    @Override
    public MadGuiAbstractManager<HoldHandler> addItemHandlers(MadItem item)
    {
        return super.addItemHandlers(item, item.getHoldHandlers());
    }

}