package fr.shuvly.paper.madgui;

import fr.shuvly.paper.madgui.handler.click.ClickHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import fr.shuvly.paper.maditem.MadItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MadGui
	implements InventoryHolder
{

	private final Inventory gui;

	private final int size;
	private final Component title;
	private final boolean areItemsLockedIn;
	private final Map<Integer, ClickHandler> slotActions = new HashMap<>();


	/**
	 * Creates a new GUI.
	 *
	 * @param	title		GUI's title
	 * @param	size		GUI's amount of lines (must be between 1 and 6)
	 */
	public MadGui(Component title, int size)
	{
		this(title, size, true);
	}

	/**
	 * Creates a new GUI.
	 *
	 * @param	title				GUI's title
	 * @param	size				GUI's amount of lines (must be between 1 and 6)
	 * @param 	areItemsLockedIn	Are items present in the gui locked inside the inventory?
	 */
	public MadGui(
		Component title,
		int size,
		boolean areItemsLockedIn
	)
	{
		if (size < 1 || size > 6) {
			throw new IllegalArgumentException("Invalid size. A GUI can only have 1 to 6 lines.");
		}
		
		this.title = title;
		this.size = size;
		this.areItemsLockedIn = areItemsLockedIn;

		this.gui = Bukkit.createInventory(this, this.getTotalSize(), this.title);
	}

	/**
	 * Creates a new instance of MadGui from an existing Inventory.
	 *
	 * @param	inventory	Inventory
	 */
	public MadGui(Inventory inventory)
	{
		this(inventory, true);
	}

	/**
	 * Creates a new instance of MadGui from an existing Inventory.
	 * todo(l.85): Try to get the inventory's title.
	 *
	 * @param	inventory	Inventory
	 * @param 	areItemsLockedIn	Are items present in the gui locked inside the inventory?
	 */
	public MadGui(Inventory inventory, boolean areItemsLockedIn)
	{
		this.gui = inventory;
		
		this.size = this.gui.getSize() / 9;
		this.title = Component.text("??");
		this.areItemsLockedIn = areItemsLockedIn;
	}

	
	/**
	 * Opens the GUI for a player.
	 * 
	 * @param	player	Player
	 */
	public void open(Player player)
	{
		player.getOpenInventory().close();
		player.openInventory(this.gui);
	}
	
	/**
	 * Closes the GUI for a player.
	 * 
	 * @param	player	Player
	 */
	public void close(Player player)
	{
		player.closeInventory();
	}

	/**
	 * Function to execute when the GUI is closed by any way.
	 *
	 * @param	player	Player
	 * @param	gui		Closed GUI
	 * @apiNote By default, this function is empty.</br>
	 * 			Override it if you want to execute some instructions on GUI close.
	 */
	public void onClose(Player player, MadGui gui) {}

	/**
	 * Adds an item in the GUI to the first free slot.
	 *
	 * @param	item	Item to add
	 * @return	Itself
	 */
	public MadGui addItem(ItemStack item)
	{
		this.gui.addItem(item);
		return this;
	}

	public MadGui addItem(ItemStack item, ClickHandler handler)
	{
		int slot = this.gui.firstEmpty();

		if (slot != -1) {
			this.gui.setItem(slot, item);

			if (handler != null) {
				this.slotActions.put(slot, handler);
			}
		}

		return this;
	}

	/**
	 * Adds an item in the GUI to the first free slot.
	 *
	 * @param	item	Item to add
	 * @return	Itself
	 */
	public MadGui addItem(MadItem item)
	{
		ClickHandler handler = null;

		if (item.getClickHandlers() != null && !item.getClickHandlers().isEmpty()) {
			handler = item.getClickHandlers().getFirst();
		}

		return this.addItem(item.getItemStack(), handler);
	}

	/**
	 * Sets an item in the GUI at a given index.
	 * 
	 * @param	index	Slot index
	 * @param	item	Item
	 * @return	Itself
	 */
	public MadGui setItem(int index, ItemStack item)
	{
		this.gui.setItem(index, item);
		return this;
	}

	public MadGui setItem(int index, ItemStack item, ClickHandler handler)
	{
		this.gui.setItem(index, item);

		if (handler != null) {
			this.slotActions.put(index, handler);
		}

		return this;
	}
	
	/**
	 * Sets an item in the GUI at a given index.
	 * 
	 * @param	index	Slot index
	 * @param	item	Item to add
	 * @return	Itself
	 */
	public MadGui setItem(int index, MadItem item)
	{
		ClickHandler handler = null;

		if (item.getClickHandlers() != null && !item.getClickHandlers().isEmpty()) {
			handler = item.getClickHandlers().getFirst();
		}

		return this.setItem(index, item.getItemStack(), handler);
	}
	
	public ClickHandler getSlotAction(int slot) {
		return this.slotActions.get(slot);
	}
	
	/**
	 * Removes an item from the GUI.
	 * 
	 * @param	index	Slot index
	 * @return	Itself
	 */
	public MadGui removeItem(int index)
	{
		this.gui.setItem(index, null);
		return this;
	}

	/**
	 * Replaces every item of a certain name by another item.
	 * fixme: You may want to perform further checks, not only the name.
	 *
	 * @param	toReplace		Name of the item to replace
	 * @param	toReplaceWith	Replacing item
	 * @return	Itself
	 */
	public MadGui replaceAll(Component toReplace, MadItem toReplaceWith)
	{
		for (int k = 0; k < this.getTotalSize(); k++) {
			final ItemStack item = this.gui.getItem(k);

			if (item == null || item.getType() == Material.AIR) {
				continue;
			}

			if (Objects.equals(item.getItemMeta().displayName(), toReplace)) {
				this.gui.setItem(k, toReplaceWith.getItemStack());
			}
		}

		return this;
	}
	
	/**
	 * Updates player's current open inventory.
	 * 
	 * @param	player	Player
	 * @return	Itself
	 */
	public MadGui update(Player player)
	{
		player.updateInventory();
		return this;
	}
	

	/**
	 * @return	GUI's inventory
	 */
	@Override
	public Inventory getInventory()
	{
		return this.gui;
	}
	
	/**
	 * @return	GUI's number of lines
	 */
	public int getSize()
	{
		return this.size;
	}
	
	/**
	 * @return	GUI's number of slots
	 * @apiNote	One line is 9 slots.
	 */
	public int getTotalSize()
	{
		return this.size * 9;
	}
	
	/**
	 * @return	GUI's title
	 */
	public Component getTitle()
	{
		return this.title;
	}

	/**
	 * @return	True if items are locked inside the gui
	 */
	public boolean areItemsLockedIn()
	{
		return this.areItemsLockedIn;
	}

}
