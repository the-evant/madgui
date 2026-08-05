package fr.shuvly.paper.madgui.listener;

import java.util.List;

import fr.shuvly.paper.maditem.MadItem;
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
import fr.shuvly.paper.madgui.handler.click.ClickManager;

public class ClickListener
	implements Listener
{
	
	private final ClickManager clickManager;
	
	
	/**
	 * Creates a new instance of MadGuiClickListener, with
	 * a click manager for click handling.
	 * 
	 * @param	clickManager	Click manager
	 */
	public ClickListener(ClickManager clickManager)
	{
		this.clickManager = clickManager;
	}


	/**
	 * Executes the click actions linked to the clicked item.
	 *
	 * @param	player		Player
	 * @param	clickType	Click type
	 * @param	gui			GUI
	 * @param	item		Item
	 * @param	slot		Slot
	 */
	private boolean processClick(
		Player player,
		ClickType clickType,
		MadGui gui,
		ItemStack item,
		int slot
	)
	{
		final String actionId = MadItem.getActionId(item);
		if (actionId == null) {
			return false;
		}
		final List<ClickHandler> handlers = this.clickManager.getHandlers().get(actionId);

		if (handlers == null) {
			return false;
		}

		handlers.stream()
			.filter((ClickHandler handler) -> {
				if (handler.isWhitelistOn() && !handler.isGuiInWhitelist(gui.getTitle())) {
					return false;
				}
				return handler.getClickTypes().contains(clickType);
			})
			.forEach((ClickHandler handler) ->
				handler.getClickAction().execute(player, gui, item, slot));

		return true;
	}

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

        final ClickType clickType = event.getClick();
		final ItemStack item = event.getCurrentItem();
		final int slot = event.getSlot();

		final MadGui gui = holder instanceof MadGui
			? (MadGui) holder
			: new MadGui(inventory);

		if (item == null || item.getType() == Material.AIR) {
			return;
		}

		final boolean cancelEvent = this.processClick(player, clickType, gui, item, slot);

		event.setCancelled(cancelEvent);
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
