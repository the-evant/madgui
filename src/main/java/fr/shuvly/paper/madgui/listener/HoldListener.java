package fr.shuvly.paper.madgui.listener;

import fr.shuvly.paper.madgui.handler.hold.HoldHandler;
import fr.shuvly.paper.madgui.manager.MadItemRegistry;
import fr.shuvly.paper.maditem.MadItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class HoldListener
    implements Listener
{

    public void processHold(Player player, ItemStack item, boolean activeItem)
    {
        final String actionId = MadItem.getActionId(item);

        if (actionId == null) {
            return;
        }

        final int slot = player.getInventory().getHeldItemSlot();
        final List<HoldHandler> holdHandlers = MadItemRegistry.getHoldHandlers(actionId);

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

    public void refreshArmorState(Player player)
    {
        final PlayerInventory inventory = player.getInventory();

        for (ItemStack armorPiece : inventory.getArmorContents()) {
            if (armorPiece == null ||
                armorPiece.getType() == Material.AIR ||
                !armorPiece.hasItemMeta()) {
                continue;
            }
            
            String actionId = MadItem.getActionId(armorPiece);
            boolean hasHandlers = actionId != null && MadItemRegistry.getHoldHandlers(actionId) != null;
            this.processHold(player, armorPiece, hasHandlers);
        }
    }

    @EventHandler
    public void onInteract(PlayerItemHeldEvent event)
    {
        final Player player = event.getPlayer();
        final ItemStack item = player.getInventory().getItem(event.getNewSlot());
        final ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());

        if (item != null && item.getType() != Material.AIR) {
            this.processHold(player, item, true);
        }

        if (previousItem != null && previousItem.getType() != Material.AIR) {
            this.processHold(player, previousItem, false);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event)
    {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItemDrop().getItemStack();

        if (item.getType() == Material.AIR) {
            return;
        }

        this.processHold(player, item, false);
    }
}
