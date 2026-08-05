package fr.shuvly.paper.madgui.listener;

import fr.shuvly.paper.madgui.handler.hold.HoldHandler;
import fr.shuvly.paper.madgui.manager.MadItemRegistry;
import fr.shuvly.paper.maditem.MadItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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

    public void processHold(
        Player player,
        ItemStack item,
        boolean activeItem,
        int slot
    )
    {
        final String actionId = MadItem.getActionId(item);

        if (actionId == null) {
            return;
        }

        final List<HoldHandler> holdHandlers = MadItemRegistry.getHoldHandlers(actionId);

        if (holdHandlers == null || holdHandlers.isEmpty()) {
            return;
        }

        if (activeItem) {
            holdHandlers.forEach((HoldHandler handler) -> {
                if (handler.getHoldAction() != null) {
                    handler.getHoldAction().execute(player, item, slot);
                }
            });
        } else {
            holdHandlers.forEach((HoldHandler handler) -> {
                if (handler.getNotHoldAction() != null) {
                    handler.getNotHoldAction().execute(player, item, slot);
                }
            });
        }
    }

    public void refreshArmorState(Player player)
    {
        final PlayerInventory inventory = player.getInventory();
        final ItemStack[] armor = inventory.getArmorContents();

        for (int i = 0; i < armor.length; i++) {
            final ItemStack armorPiece = armor[i];

            if (armorPiece == null ||
                armorPiece.getType() == Material.AIR ||
                !armorPiece.hasItemMeta()) {
                continue;
            }

            this.processHold(player, armorPiece, true, 36 + i);
        }
    }

    @EventHandler
    public void onItemHold(PlayerItemHeldEvent event)
    {
        final Player player = event.getPlayer();
        final ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());
        final ItemStack item = player.getInventory().getItem(event.getNewSlot());

        if (previousItem != null && previousItem.getType() != Material.AIR) {
            this.processHold(player, previousItem, false, event.getPreviousSlot());
        }

        if (item != null && item.getType() != Material.AIR) {
            this.processHold(player, item, true, event.getNewSlot());
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event)
    {
        final Player player = event.getPlayer();
        final ItemStack droppedItem = event.getItemDrop().getItemStack();

        if (droppedItem.getType() == Material.AIR) {
            return;
        }

        final ItemStack mainHand = player.getInventory().getItemInMainHand();

        if (mainHand.getType() != droppedItem.getType() || !mainHand.isSimilar(droppedItem)) {
            this.processHold(player, droppedItem, false, player.getInventory().getHeldItemSlot());
        }
    }
}
