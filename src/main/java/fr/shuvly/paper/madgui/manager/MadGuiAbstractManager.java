package fr.shuvly.paper.madgui.manager;

import fr.shuvly.paper.madgui.handler.click.ClickManager;
import fr.shuvly.paper.maditem.MadItem;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MadGuiAbstractManager
    <T>
{

    private final Map<Component, List<T>> handlers;


    /**
     * Creates a new manager of a certain type T.
     * <p>
     * Basically stores the handlers of a certain type.
     * </br>
     * It is used for detections of a certain type on items.
     * </p>
     */
    public MadGuiAbstractManager()
    {
        this.handlers = new HashMap<Component, List<T>>();
    }


    /**
     * Adds a handler to the handlers map, with a title assigned.
     *
     * @param   title   Title of the item assigned to the handler
     * @param   handler Handler to add
     */
    private void addItemHandlers(Component title, T handler)
    {
        this.handlers.computeIfAbsent(title, k -> new ArrayList<T>());

        for (T h : this.handlers.get(title)) {
            if (handler.equals(h)) {
                return;
            }
        }
        this.handlers.get(title).add(handler);
    }

    /**
     * Adds every handler of type T of a given item.
     *
     * @param   item        Item whose handlers will be added
     * @param   handlers    Item handlers
     * @return  Itself
     */
    protected MadGuiAbstractManager<T> addItemHandlers(MadItem item, List<T> handlers)
    {
        if (handlers == null || handlers.isEmpty()) {
            return this;
        }

        for (T handler : handlers) {
            this.addItemHandlers(item.getItemStack().getItemMeta().displayName(), handler);
        }
        return this;
    }

    /**
     * This must call {@link #addItemHandlers(MadItem, List)} with
     * the appropriate handler list.
     *
     * @param   item    Item whose handlers will be added
     * @return  Itself
     * @see     {@link ClickManager#addItemHandlers(MadItem)}
     */
    public abstract MadGuiAbstractManager<T> addItemHandlers(MadItem item);

    /**
     * @param   itemTitle   Item's title
     */
    public void removeItemHandlers(Component itemTitle)
    {
        this.handlers.remove(itemTitle);
    }


    /**
     * @return  Handlers
     */
    public Map<Component, List<T>> getHandlers()
    {
        return this.handlers;
    }

}
