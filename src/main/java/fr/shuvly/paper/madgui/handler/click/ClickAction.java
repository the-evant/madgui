package fr.shuvly.paper.madgui.handler.click;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.shuvly.paper.madgui.MadGui;

public interface ClickAction
{
	
	/**
	 * Function called when item is clicked.
	 * 
	 * @param	player	Player who clicked
	 * @param	gui		Clicked GUI
	 * @param	item	Clicked item
	 * @param	slot	Clicked slot
	 */
	void execute(
		Player player,
		MadGui gui,
		ItemStack item,
		int slot
	);

}
