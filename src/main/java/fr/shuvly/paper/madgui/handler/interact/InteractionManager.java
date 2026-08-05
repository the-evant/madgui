package fr.shuvly.paper.madgui.handler.interact;

import fr.shuvly.paper.madgui.manager.MadGuiAbstractManager;
import fr.shuvly.paper.maditem.MadItem;

public class InteractionManager
	extends MadGuiAbstractManager<InteractHandler>
{

	@Override
	public MadGuiAbstractManager<InteractHandler> addItemHandlers(MadItem item)
	{
		return super.addItemHandlers(item, item.getInteractHandlers());
	}

}
