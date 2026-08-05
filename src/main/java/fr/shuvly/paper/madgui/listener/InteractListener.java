package fr.shuvly.paper.madgui.listener;

import java.util.List;

import fr.shuvly.paper.maditem.MadItem;
import fr.shuvly.paper.madgui.manager.MadItemRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.shuvly.paper.madgui.handler.interact.InteractHandler;

public class InteractListener
	implements Listener
{

	private boolean processInteraction(
		Player player,
		Action interactType,
		ItemStack item
	)
	{
		final String actionId = MadItem.getActionId(item);

		if (actionId == null) {
			return false;
		}
		
		final int slot = player.getInventory().getHeldItemSlot();
		final List<InteractHandler> interactHandlers = MadItemRegistry.getInteractHandlers(actionId);

		if (interactHandlers == null || interactHandlers.isEmpty()) {
			return false;
		}

		interactHandlers.stream()
			.filter((InteractHandler handler) ->
				handler.getInteractTypes().contains(interactType))
			.forEach((InteractHandler handler) ->
				handler.getInteractAction().execute(player, item, slot));

		return true;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event)
	{
		if (event.getItem() == null) {
			return;
		}

		final Player player = event.getPlayer();
		final Action interactType = event.getAction();
		final ItemStack item = event.getItem();

		event.setCancelled(this.processInteraction(player, interactType, item));
	}

}
