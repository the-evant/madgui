package fr.shuvly.paper.madgui.listener;

import fr.shuvly.paper.madgui.MadGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class CloseListener
    implements Listener
{

    /**
     * {@link MadGui#onClose(Player, MadGui)} handling.
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if (!(event.getPlayer() instanceof Player player) ||
            !(event.getInventory().getHolder() instanceof MadGui gui)) {
            return;
        }

        gui.onClose(player, gui);
    }

}
