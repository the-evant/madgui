package fr.shuvly.paper.madgui.listener;

import fr.shuvly.paper.madgui.handler.destroy.DestroyHandler;
import fr.shuvly.paper.madgui.manager.MadItemRegistry;
import fr.shuvly.paper.maditem.MadItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DestroyListener
    implements Listener
{

    private void processBlockDestroy(
        Player player,
        ItemStack itemUsed,
        Block destroyedBlock
    )
    {
        final String actionId = MadItem.getActionId(itemUsed);

        if (actionId == null) {
            return;
        }
        
        final List<DestroyHandler> destroyHandlers = MadItemRegistry.getDestroyHandlers(actionId);

        if (destroyHandlers == null || destroyHandlers.isEmpty()) {
            return;
        }

        destroyHandlers.forEach((DestroyHandler handler) ->
            handler.getDestroyAction().execute(player, itemUsed, destroyedBlock));
    }

    @EventHandler
    public void onInteract(BlockBreakEvent event)
    {
        final Player player = event.getPlayer();
        final ItemStack itemUsed = player.getInventory().getItemInMainHand();
        final Block destroyedBlock = event.getBlock();

        if (itemUsed.getType() == Material.AIR) {
            return;
        }

        this.processBlockDestroy(player, itemUsed, destroyedBlock);
    }
}
