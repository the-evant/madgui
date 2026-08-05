package fr.shuvly.paper.madgui.handler.click;

import fr.shuvly.paper.madgui.manager.MadGuiAbstractManager;
import fr.shuvly.paper.maditem.MadItem;

public class ClickManager
	extends MadGuiAbstractManager<ClickHandler>
{

	@Override
	public MadGuiAbstractManager<ClickHandler> addItemHandlers(MadItem item)
	{
		return super.addItemHandlers(item, item.getClickHandlers());
	}

}
