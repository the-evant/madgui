package fr.shuvly.paper.maditem;

import java.util.ArrayList;
import java.util.List;

import fr.shuvly.paper.madgui.MadGui;
import fr.shuvly.paper.madgui.handler.destroy.DestroyHandler;
import fr.shuvly.paper.madgui.handler.hold.HoldHandler;
import fr.shuvly.paper.madgui.handler.hit.HitHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import fr.shuvly.paper.madgui.manager.MadGuiManager;
import fr.shuvly.paper.madgui.handler.click.ClickHandler;
import fr.shuvly.paper.madgui.handler.interact.InteractHandler;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class MadItem
{
	
	private final ItemStack itemStack;
	private ItemMeta itemMeta;

	private List<ClickHandler> clickHandlers;
	private List<InteractHandler> interactHandlers;
	private List<HoldHandler> holdHandlers;
	private List<HitHandler> hitHandlers;
	private List<DestroyHandler> destroyHandlers;

	
	/**
	 * Creates a new MadItem.
	 * 
	 * @param	type	Material type
	 */
	public MadItem(Material type)
	{
		this(type, 1, null);
	}
	
	/**
	 * Creates a new MadItem.
	 * 
	 * @param	type	Material type
	 * @param	amount	Amount of items
	 */
	public MadItem(Material type, int amount)
	{
		this(type, amount, null);
	}
	
	/**
	 * Creates a new MadItem.
	 * 
	 * @param	type	Material type
	 * @param	data	Item's data
	 */
	public MadItem(Material type, Byte data)
	{
		this(type, 1, data);
	}
	
	/**
	 * Creates a new MadItem.
	 * 
	 * @param	type	Material type
	 * @param	amount	Amount of items
	 * @param	data	Item's data
	 */
	public MadItem(Material type, int amount, Byte data)
	{
		this.itemStack = new ItemStack(type, amount, data == null ? (byte) 0 : data);
		this.itemMeta = this.itemStack.getItemMeta();
	}

	/**
	 * Creates a MadItem from an existing MadItem.
	 *
	 * @param	item	MadItem
	 */
	public MadItem(MadItem item)
	{
		this.itemStack = item.getItemStack().clone();
		this.itemMeta = item.getItemMeta().clone();
		this.itemStack.setItemMeta(this.itemMeta);
		this.clickHandlers = item.getClickHandlers();
		this.interactHandlers = item.getInteractHandlers();
		this.holdHandlers = item.getHoldHandlers();
		this.hitHandlers = item.getHitHandlers();
		this.destroyHandlers = item.getDestroyHandlers();
	}

	/**
	 * Creates a MadItem from an existing ItemStack.
	 * 
	 * @param	item	ItemStack
	 */
	public MadItem(ItemStack item)
	{
		this.itemStack = item;
		this.itemMeta = this.itemStack.getItemMeta();
	}


	/**
	 * Creates a Separator, a Stained Glass Pane of a certain color
	 * that does nothing, just for decoration.
	 *
	 * @param	glass		Stained Glass Pane color
	 * @param	guiManager	Gui manager
	 * @return	Created MadItem
	 */
	public static MadItem separator(Material glass, MadGuiManager guiManager)
	{
		Material mat = glass;

		if (!glass.name().endsWith("STAINED_GLASS_PANE")) {
			mat = Material.BLACK_STAINED_GLASS_PANE;
		}

		return new MadItem(mat, 1)
			.setName(Component.text(""))
			.setUntakable(true)
			.build(guiManager);
	}

	/**
	 * Special triggers handling.
	 * <ul>
	 *     <li><code>\n</code>: Line break</li>
	 * </ul>
	 */
	private void formatLore()
	{
		if (!this.itemMeta.hasLore()) {
			return;
		}

		final List<Component> newLore = new ArrayList<Component>();

		for (Component line : this.itemMeta.lore()) {
			if (!(line instanceof TextComponent l)) {
				continue;
			}
			final String lineAsString = l.content();
			String[] splittedLine = lineAsString.split("\\n");

			for (int k = 0; k < splittedLine.length; k++) {
				if (k > 0) {
					splittedLine[k] = ChatColor.getLastColors(splittedLine[k - 1]) + splittedLine[k];
				}
				newLore.add(Component.text(splittedLine[k]));
			}
		}

		this.itemMeta.lore(newLore);
	}

	/**
	 * Builds the item and registers its handlers.
	 *
	 * @param	guiManager	GUI Manager
	 * @return	Itself
	 */
	public MadItem build(MadGuiManager guiManager)
	{
		this.formatLore();
		this.itemStack.setItemMeta(this.itemMeta);
		
		guiManager.getClickManager().addItemHandlers(this);
		guiManager.getInteractionManager().addItemHandlers(this);
		guiManager.getHoldManager().addItemHandlers(this);
		guiManager.getHitManager().addItemHandlers(this);
		guiManager.getDestroyManager().addItemHandlers(this);
		return this;
	}

	/**
	 * Builds the item and registers its handlers, only for
	 * a certain GUI title (mainly the parent gui of the item).
	 *
	 * @param	guiTitle	GUI title
	 * @param	guiManager	GUI Manager
	 * @return	Itself
	 */
	public MadItem build(Component guiTitle, MadGuiManager guiManager)
	{
		if (this.clickHandlers != null) {
			for (ClickHandler handler : this.clickHandlers) {
				handler.addGuiToWhitelist(guiTitle);
			}
		}

		return this.build(guiManager);
	}

	/**
	 * Builds the item and registers its handlers, only for
	 * a certain GUI (mainly the parent gui of the item).
	 *
	 * @param	gui		GUI
	 * @param	guiManager	GUI Manager
	 * @return	Itself
	 */
	public MadItem build(MadGui gui, MadGuiManager guiManager)
	{
		return this.build(gui.getTitle(), guiManager);
	}
	
	/**
	 * Builds the item, without registering its handlers.
	 * 
	 * @return	Itself
	 */
	public MadItem build()
	{
		this.formatLore();
		this.itemStack.setItemMeta(this.itemMeta);
		return this;
	}

	/**
	 * Overrides the set ItemMeta.
	 * Mainly used in {@link MadSkull}.
	 *
	 * @param	itemMeta	Item meta
	 * @return	Itself
	 * @apiNote	Must not be used outside of this library.
	 */
	public MadItem setItemMeta(ItemMeta itemMeta)
	{
		this.itemMeta = itemMeta;
		return this;
	}

	/**
	 * @return	Item's meta
	 * @apiNote	Must not be used outside of this library.
	 */
	public ItemMeta getItemMeta()
	{
		return this.itemMeta;
	}
	
	/**
	 * @return	Built item's ItemStack
	 */
	public ItemStack getItemStack()
	{
		return this.itemStack;
	}
	
	/**
	 * Sets item's type.
	 * 
	 * @param	type	Item type.
	 * @return	Itself
	 */
	public MadItem setType(Material type)
	{
		this.itemStack.setType(type);
		return this;
	}
	
	/**
	 * Sets item's amount.
	 * 
	 * @param	amount	Amount of items
	 * @return	Itself
	 */
	public MadItem setAmount(int amount)
	{
		this.itemStack.setAmount(amount);
		return this;
	}
	
	/**
	 * Sets item's durability.
	 * 
	 * @param	durability	Durability to set
	 * @return	Itself
	 */
	public MadItem setDurability(int durability)
	{
		if (!(this.itemMeta instanceof Damageable)) {
			/* System.err.println is used because HashLogger can't be. */
			System.err.println(
				"MadItem#setDurability: Called with an incompatible ItemMeta (not instance of Damageable).\n" +
				"MadItem used: " + this
			);
			return this;
		}

		((Damageable) this.itemMeta).setDamage(this.itemStack.getType().getMaxDurability() - durability);
		return this;
	}

	/**
	 * Sets item's durability.
	 * If you're manipulating {@link MadItem}s, please use
	 * {@link MadItem#setDurability(int)}.
	 * <p>
	 * This code is duplicated, but, I guess I can't do better.
	 *
	 * @param	item		Item
	 * @param	durability	Durability to set
	 */
	public static void setDurability(ItemStack item, int durability)
	{
		final ItemMeta meta = item.getItemMeta();

		if (!(meta instanceof Damageable)) {
			/* System.err.println is used because HashLogger can't be. */
			System.err.println(
				"MadItem#setDurability: Called with an incompatible ItemMeta (not instance of Damageable).\n" +
				"ItemStack used: " + item
			);
			return;
		}

		((Damageable) meta).setDamage(item.getType().getMaxDurability() - durability);
		item.setItemMeta(meta);
	}
	
	/**
	 * Sets item's data.
	 * TODO: Finish this function.
	 *
	 * @param	data	Item data.
	 * @return	Itself
	 * @deprecated
	 */
	@Deprecated
	public MadItem setData(Byte data)
	{
		return this;
	}

	/**
	 * Sets item's name.
	 *
	 * @param	name	Item name.
	 * @return	Itself
	 */
	public MadItem setName(Component name)
	{
		this.itemMeta.displayName(name);
		return this;
	}

	/**
	 * Sets item's lore.
	 * 
	 * @param	lore	Item lore.
	 * @return	Itself
	 */
	public MadItem setLore(List<Component> lore)
	{
		this.itemMeta.lore(lore);
		return this;
	}
	
	/**
	 * Adds a line to item's lore.
	 * 
	 * @param	line	Lore line.
	 * @return	Itself
	 * @apiNote Handles line breaks ! (<code>>br/<</code>)
	 */
	public MadItem addLore(Component line)
	{
		final List<Component> lore = this.itemMeta.hasLore()
			? this.itemMeta.lore()
			: new ArrayList<Component>();

		lore.add(line);

		this.setLore(lore);
		return this;
	}

	/**
	 * Adds some lines to item's lore.
	 *
	 * @param	content		Content to add.
	 * @return	Itself
	 */
	public MadItem addLore(List<Component> content)
	{
		for (Component line : content) {
			this.addLore(line);
		}
		return this;
	}

	/**
	 * Clears item's lore.
	 *
	 * @return	Itself
	 */
	public MadItem clearLore()
	{
		this.itemMeta.lore(null);
		return this;
	}
	
	/**
	 * Sets item's flags.
	 * 
	 * @param	flags	Item flags.
	 * @return	Itself
	 */
	public MadItem setFlags(List<ItemFlag> flags)
	{
		this.itemMeta.getItemFlags().clear();
		
		flags.forEach(this.itemMeta::addItemFlags);
		return this;
	}
	
	/**
	 * Adds a flag to item's flags.
	 * 
	 * @param	flag	Item flag.
	 * @return	Itself
	 */
	public MadItem addFlag(ItemFlag flag)
	{
		this.itemMeta.addItemFlags(flag);
		return this;
	}

	/**
	 * Clears item flags.
	 *
	 * @return	Itself
	 */
	public MadItem clearFlags()
	{
		for (ItemFlag flag : this.itemMeta.getItemFlags()) {
			this.getItemMeta().removeItemFlags(flag);
		}
		return this;
	}
	
	/**
	 * Makes item unbreakable.
	 * 
	 * @param	unbreakable	Unbreakable
	 * @return	Itself
	 */
	public MadItem setUnbreakable(boolean unbreakable)
	{
		this.itemMeta.setUnbreakable(unbreakable);
		return this;
	}
	
	/**
	 * @return	Is item unbreakable ?
	 */
	public boolean isUnbreakable()
	{
		return this.itemMeta.isUnbreakable();
	}
	
	/**
	 * Adds an enchantment to the item.
	 * 
	 * @param	enchantment	Enchantment type
	 * @param	level		Enchantment level
	 * @return	Itself
	 */
	public MadItem addEnchant(Enchantment enchantment, int level)
	{
		this.itemMeta.addEnchant(enchantment, level, true);
		return this;
	}
	
	/**
	 * Removes an enchantment from the item.
	 * 
	 * @param	enchantment	Enchantment type
	 * @return	Itself
	 */
	public MadItem removeEnchant(Enchantment enchantment)
	{
		this.itemMeta.removeEnchant(enchantment);
		return this;
	}

	/**
	 * Clears item enchantments.
	 *
	 * @return	Itself
	 */
	public MadItem clearEnchantments()
	{
		for (Enchantment enchantment : this.itemMeta.getEnchants().keySet()) {
			this.getItemMeta().removeEnchant(enchantment);
		}
		return this;
	}

	/**
	 * Makes the item untakable, or not.
	 *
	 * @param 	untakable	Untakable
	 * @return	Itself
	 */
	public MadItem setUntakable(boolean untakable)
	{
		if (!untakable) {
			this.clickHandlers.clear();
			return this;
		}

		this.addClickHandler(
			new ClickHandler()
				.addAllClickTypes()
				.setClickAction((player, gui, item, slot) -> {})
		);

		return this;
	}

	/**
	 * Sets leather armor color.
	 *
	 * @param	color				Wanted color
	 * @return	Itself
	 * @throws	ClassCastException	If item is not a leather armor piece
	 */
	public MadItem setLeatherArmorColor(Color color)
		throws ClassCastException
	{
		if (!this.itemStack.getType().name().startsWith("LEATHER_")) {
			throw new ClassCastException("Item must be a leather armor piece.");
		}
		((LeatherArmorMeta) this.itemMeta).setColor(color);
		return this;
	}

	/* Handlers management */

	/**
	 * Adds a click handler to the item.
	 * If a handler with the same ClickType is already registered,
	 * both will run on click.
	 * 
	 * @param	clickHandler	Click handler.
	 * @return	Itself
	 */
	public MadItem addClickHandler(ClickHandler clickHandler)
	{
		if (this.clickHandlers == null) {
			this.clickHandlers = new ArrayList<ClickHandler>();
		}
		this.clickHandlers.add(clickHandler);
		return this;
	}

	/**
	 * Clears item's click handlers.
	 *
	 * @return	Itself
	 */
	public MadItem clearClickHandlers()
	{
		if (this.clickHandlers != null) {
			this.clickHandlers.clear();
		}
		return this;
	}
	
	/**
	 * Adds an interact handler to the item.
	 * If a handler with the same Action is already registered,
	 * both will run on interact.
	 * 
	 * @param	interactHandler	Interact handler.
	 * @return	Itself
	 */
	public MadItem addInteractHandler(InteractHandler interactHandler)
	{
		if (this.interactHandlers == null) {
			this.interactHandlers = new ArrayList<InteractHandler>();
		}
		this.interactHandlers.add(interactHandler);
		return this;
	}

	/**
	 * Clears item's interact handlers.
	 *
	 * @return	Itself
	 */
	public MadItem clearInteractHandlers()
	{
		if (this.interactHandlers != null) {
			this.interactHandlers.clear();
		}
		return this;
	}

	/**
	 * Adds a hold handler to the item.
	 *
	 * @param	holdHandler	Hold handler.
	 * @return	Itself
	 */
	public MadItem addHoldHandler(HoldHandler holdHandler)
	{
		if (this.holdHandlers == null) {
			this.holdHandlers = new ArrayList<HoldHandler>();
		}
		this.holdHandlers.add(holdHandler);
		return this;
	}

	/**
	 * Clears item's hold handlers.
	 *
	 * @return	Itself
	 */
	public MadItem clearHoldHandlers()
	{
		if (this.holdHandlers != null) {
			this.holdHandlers.clear();
		}
		return this;
	}

	/**
	 * Adds a hit handler to the item.
	 *
	 * @param	hitHandler	Hit handler.
	 * @return	Itself
	 */
	public MadItem addHitHandler(HitHandler hitHandler)
	{
		if (this.hitHandlers == null) {
			this.hitHandlers = new ArrayList<HitHandler>();
		}
		this.hitHandlers.add(hitHandler);
		return this;
	}

	/**
	 * Clears item's hit handlers.
	 *
	 * @return	Itself
	 */
	public MadItem clearHitHandlers()
	{
		if (this.hitHandlers != null) {
			this.hitHandlers.clear();
		}
		return this;
	}

	/**
	 * Adds a destroy handler to the item.
	 *
	 * @param	destroyHandler	Destroy handler.
	 * @return	Itself
	 */
	public MadItem addDestroyHandler(DestroyHandler destroyHandler)
	{
		if (this.destroyHandlers == null) {
			this.destroyHandlers = new ArrayList<DestroyHandler>();
		}
		this.destroyHandlers.add(destroyHandler);
		return this;
	}

	/**
	 * Clears item's destroy handlers.
	 *
	 * @return	Itself
	 */
	public MadItem clearDestroyHandlers()
	{
		if (this.destroyHandlers != null) {
			this.destroyHandlers.clear();
		}
		return this;
	}

	/**
	 * Clears item's handlers.
	 *
	 * @return	Itself
	 */
	public MadItem clearHandlers()
	{
		this.clearClickHandlers();
		this.clearInteractHandlers();
		this.clearHoldHandlers();
		this.clearHitHandlers();
		this.clearDestroyHandlers();
		return this;
	}
	
	/**
	 * @return	Item's click handlers.
	 */
	public List<ClickHandler> getClickHandlers()
	{
		return this.clickHandlers;
	}
	
	/**
	 * @return	Item's interact handlers.
	 */
	public List<InteractHandler> getInteractHandlers()
	{
		return this.interactHandlers;
	}

	/**
	 * @return	Item's hold handlers.
	 */
	public List<HoldHandler> getHoldHandlers()
	{
		return this.holdHandlers;
	}

	/**
	 * @return	Item's hit handlers.
	 */
	public List<HitHandler> getHitHandlers()
	{
		return this.hitHandlers;
	}

	/**
	 * @return	Item's destroy handlers.
	 */
	public List<DestroyHandler> getDestroyHandlers()
	{
		return this.destroyHandlers;
	}

}
