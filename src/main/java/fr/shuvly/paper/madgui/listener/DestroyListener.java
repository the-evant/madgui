package fr.shuvly.paper.madgui.listener;

import fr.shuvly.paper.madgui.handler.destroy.DestroyHandler;
import fr.shuvly.paper.madgui.handler.destroy.DestroyManager;
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

    private final DestroyManager destroyManager;


    /**
     * Creates a new instance of DestroyListener, with
     * a destroy manager for block destroy handling.
     *
     * @param	destroyManager Destroy manager
     */
    public DestroyListener(DestroyManager destroyManager)
    {
        this.destroyManager = destroyManager;
    }


    /**
     * Executes the destroy actions linked to the used item.
     *
     * @param   player          Player who destroyed the block
     * @param   itemUsed        Item used
     * @param   destroyedBlock  Destroyed block
     */
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
        final List<DestroyHandler> destroyHandlers = this.destroyManager.getHandlers().get(actionId);

        if (destroyHandlers == null || destroyHandlers.isEmpty()) {
            return;
        }

        destroyHandlers.forEach((DestroyHandler handler) ->
            handler.getDestroyAction().execute(player, itemUsed, destroyedBlock));
    }

    /**
     * Block destroy handling.
     */
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
