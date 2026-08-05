package fr.shuvly.paper.madgui.page;

import fr.shuvly.paper.madgui.PaginatedMadGui;
import fr.shuvly.paper.maditem.MadItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Page
{

    private final PaginatedMadGui parent;
    private final Map<Integer, MadItem> items;


    /**
     * Creates a new instance of Page.
     */
    public Page(PaginatedMadGui parent)
    {
        this.parent = parent;
        this.items = new HashMap<>();
    }


    /**
     * @param   slot    Slot index
     * @return  Returns true if an item is already at this slot.
     */
    private boolean isItemInParentGui(int slot)
    {
        final ItemStack item = this.parent.getInventory().getItem(slot);

        return item != null && item.getType() != Material.AIR;
    }

    /**
     * Sets an item at a given slot.
     *
     * @param   slot                        Slot index
     * @param   item                        Item
     * @throws  IllegalArgumentException    Slot already in use
     */
    public void setItem(int slot, MadItem item)
        throws IllegalArgumentException
    {
        if (isItemInParentGui(slot)) {
            throw new IllegalArgumentException("Item can't be placed at the slot " + slot + ".");
        }
        this.items.put(slot, item);
    }

    /**
     * Adds an item to the page.
     *
     * @param   item                        Item to add
     * @throws  IllegalArgumentException    No slot left
     */
    public void addItem(MadItem item)
        throws IllegalArgumentException
    {
        for (int k = 0; k < this.parent.getTotalSize(); k++) {
            if (this.isItemInParentGui(k) || this.items.containsKey(k)) {
                continue;
            }

            this.items.put(k, item);
            return;
        }
        throw new IllegalArgumentException("No slot left.");
    }

    /**
     * Removes an item from the page.
     *
     * @param   slot                        Slot index
     * @throws  IllegalArgumentException    Unreachable slot (not in the page but in the parent GUI)
     */
    public void removeItem(int slot)
        throws IllegalArgumentException
    {
        if (this.isItemInParentGui(slot)) {
            throw new IllegalArgumentException("Item can't be removed from the slot " + slot + ".");
        }
        this.items.remove(slot);
    }

    /**
     * Clears page items.
     */
    public void clearItems()
    {
        this.items.clear();
    }

    /**
     * @param   slot    Slot index
     * @return  Item at slot
     */
    public MadItem getItem(int slot)
    {
        return this.items.get(slot);
    }

    /**
     * @return  Page items
     */
    public Map<Integer, MadItem> getItems()
    {
        return this.items;
    }

}
