package fr.shuvly.paper.madgui.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import fr.shuvly.paper.madgui.MadGui;
import fr.shuvly.paper.madgui.handler.click.ClickHandler;

public class ClickListener
	implements Listener
{
	
	/**
	 * Click handling.
	 */
	@EventHandler(ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event)
	{
		if (!(event.getWhoClicked() instanceof Player player) ||
			event.getClickedInventory() == null ||
			event.getCurrentItem() == null ||
			event.getCurrentItem().getType() == Material.AIR) {
			return;
		}

		final Inventory inventory = event.getClickedInventory();
		final InventoryHolder holder = inventory.getHolder();

		if (!(holder instanceof MadGui gui)) {
			return;
		}

        final ClickType clickType = event.getClick();
		final ItemStack item = event.getCurrentItem();
		final int slot = event.getSlot();

		ClickHandler handler = gui.getSlotAction(slot);
		if (handler != null && handler.getClickTypes().contains(clickType)) {
			handler.getClickAction().execute(player, gui, item, slot);
			event.setCancelled(true);
		} else if (gui.areItemsLockedIn()) {
			event.setCancelled(true);
		}
	}

	/**
	 * Called for preventing items from moving from a MadGui to another
	 * inventory (e.g. player's inventory) if {@link MadGui#areItemsLockedIn()} is true.
	 * @apiNote Don't prevent the player from moving an item to his inventory by using his mouse.
	 *          This is a Minecraft issue, so it is pretty much unfixable.
	 */
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onItemDrag(InventoryClickEvent event)
	{
		final InventoryView view = event.getView();

		if (!(view.getTopInventory().getHolder() instanceof MadGui gui)) {
			return;
		} else {
            view.getCursor();
        }

        if (!gui.areItemsLockedIn()) {
			return;
		}

		if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY ||
			event.getAction() == InventoryAction.HOTBAR_SWAP) {
			event.setCancelled(true);
		}
	}

}
