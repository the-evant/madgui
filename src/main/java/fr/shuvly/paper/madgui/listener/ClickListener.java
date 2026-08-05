package fr.shuvly.paper.madgui.listener;

import fr.shuvly.paper.madgui.manager.MadItemRegistry;
import fr.shuvly.paper.maditem.MadItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import fr.shuvly.paper.madgui.MadGui;
import fr.shuvly.paper.madgui.handler.click.ClickHandler;

import java.util.List;

public class ClickListener
	implements Listener
{
	
	@EventHandler(ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event)
	{
		if (!(event.getWhoClicked() instanceof Player player) ||
			event.getView().getTopInventory().getHolder() == null) {
			return;
		}

		final Inventory clickedInv = event.getClickedInventory();
		final InventoryView view = event.getView();
		final Inventory topInv = view.getTopInventory();
		
		if (clickedInv == null) {
			return;
		}

		final InventoryHolder topHolder = topInv.getHolder();

		if (topHolder instanceof MadGui gui) {
			if (clickedInv.equals(topInv)) {
				final ClickType clickType = event.getClick();
				final ItemStack item = event.getCurrentItem();
				final int slot = event.getSlot();

				if (item == null || item.getType() == Material.AIR) {
					if (gui.areItemsLockedIn()) {
						event.setCancelled(true);
					}
					return;
				}

				final ClickHandler handler = gui.getSlotAction(slot);

				if (handler != null && handler.getClickTypes().contains(clickType)) {
					handler.getClickAction().execute(player, gui, item, slot);
					player.updateInventory();
					event.setCancelled(true);
				} else if (gui.areItemsLockedIn()) {
					event.setCancelled(true);
				}
				return;
			}
			
			if (gui.areItemsLockedIn()) {
				final InventoryAction action = event.getAction();

				if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY ||
					action == InventoryAction.COLLECT_TO_CURSOR) {
					event.setCancelled(true);
				}
			}
		}

		final ItemStack clicked = event.getCurrentItem();

		if (clicked != null && clicked.getType() != Material.AIR) {
			final String actionId = MadItem.getActionId(clicked);

			if (actionId == null) {
				return;
			}

			final List<ClickHandler> handlers = MadItemRegistry.getClickHandlers(actionId);

			if (handlers == null) {
				return;
			}

			for (ClickHandler handler : handlers) {
				if (handler.getClickTypes().contains(event.getClick())) {
					handler.getClickAction().execute(player, null, clicked, event.getSlot());
					player.updateInventory();
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryDrag(InventoryDragEvent event)
	{
		final InventoryView view = event.getView();

		if (!(view.getTopInventory().getHolder() instanceof MadGui gui)) {
			return;
		}

		if (!gui.areItemsLockedIn()) {
			return;
		}

		for (int rawSlot : event.getRawSlots()) {
			if (rawSlot < view.getTopInventory().getSize()) {
				event.setCancelled(true);
				break;
			}
		}
	}
}
