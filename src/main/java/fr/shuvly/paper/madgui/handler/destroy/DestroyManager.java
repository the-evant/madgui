package fr.shuvly.paper.madgui.handler.destroy;

import fr.shuvly.paper.madgui.manager.MadGuiAbstractManager;
import fr.shuvly.paper.maditem.MadItem;

public class DestroyManager
	extends MadGuiAbstractManager<DestroyHandler>
{

	@Override
	public MadGuiAbstractManager<DestroyHandler> addItemHandlers(MadItem item)
	{
		return super.addItemHandlers(item, item.getDestroyHandlers());
	}

}