package fr.shuvly.paper.madgui.manager;

import fr.shuvly.paper.madgui.handler.click.ClickManager;
import fr.shuvly.paper.maditem.MadItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MadGuiAbstractManager
    <T>
{

    private final Map<String, List<T>> handlers;


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
        this.handlers = new HashMap<>();
    }


    /**
     * Adds a handler to the handlers map, with a title assigned.
     *
     * @param   id   ID of the item assigned to the handler
     * @param   handler Handler to add
     */
    private void addItemHandlers(String id, T handler)
    {
        if (id == null) {
            return;
        }

        this.handlers.computeIfAbsent(id, _ -> new ArrayList<>());

        for (T h : this.handlers.get(id)) {
            if (handler.equals(h)) {
                return;
            }
        }

        this.handlers.get(id).add(handler);
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

        final String id = item.getActionId();
        for (T handler : handlers) {
            this.addItemHandlers(id, handler);
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
     * @param   itemId   Item's ID
     */
    public void removeItemHandlers(String itemId)
    {
        this.handlers.remove(itemId);
    }


    /**
     * @return  Handlers
     */
    public Map<String, List<T>> getHandlers()
    {
        return this.handlers;
    }

}
