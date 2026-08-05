package fr.shuvly.paper.madgui.handler.hit;

import fr.shuvly.paper.madgui.manager.MadGuiAbstractManager;
import fr.shuvly.paper.maditem.MadItem;

public class HitManager
	extends MadGuiAbstractManager<HitHandler>
{

	@Override
	public MadGuiAbstractManager<HitHandler> addItemHandlers(MadItem item)
	{
		return super.addItemHandlers(item, item.getHitHandlers());
	}

}
