package fr.shuvly.paper.madgui.manager;

import fr.shuvly.paper.madgui.handler.destroy.DestroyHandler;
import fr.shuvly.paper.madgui.handler.hit.HitHandler;
import fr.shuvly.paper.madgui.handler.hold.HoldHandler;
import fr.shuvly.paper.madgui.handler.interact.InteractHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MadItemRegistry
{

    private static final Map<String, List<HitHandler>> hitHandlers = new HashMap<>();
    private static final Map<String, List<DestroyHandler>> destroyHandlers = new HashMap<>();
    private static final Map<String, List<HoldHandler>> holdHandlers = new HashMap<>();
    private static final Map<String, List<InteractHandler>> interactHandlers = new HashMap<>();

    public static void registerHitAction(String id, HitHandler handler) { hitHandlers.computeIfAbsent(id, k -> new ArrayList<>()).add(handler); }
    public static List<HitHandler> getHitHandlers(String id) { return hitHandlers.get(id); }

    public static void registerDestroyAction(String id, DestroyHandler handler) { destroyHandlers.computeIfAbsent(id, k -> new ArrayList<>()).add(handler); }
    public static List<DestroyHandler> getDestroyHandlers(String id) { return destroyHandlers.get(id); }

    public static void registerHoldAction(String id, HoldHandler handler) { holdHandlers.computeIfAbsent(id, k -> new ArrayList<>()).add(handler); }
    public static List<HoldHandler> getHoldHandlers(String id) { return holdHandlers.get(id); }

    public static void registerInteractAction(String id, InteractHandler handler) { interactHandlers.computeIfAbsent(id, k -> new ArrayList<>()).add(handler); }
    public static List<InteractHandler> getInteractHandlers(String id) { return interactHandlers.get(id); }

}
