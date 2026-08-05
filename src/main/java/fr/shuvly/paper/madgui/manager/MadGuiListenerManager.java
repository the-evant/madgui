package fr.shuvly.paper.madgui.manager;

import fr.shuvly.paper.madgui.listener.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class MadGuiListenerManager
{

    private final Plugin plugin;
    private final PluginManager pluginManager;


    public MadGuiListenerManager(Plugin plugin, PluginManager pluginManager)
    {
        this.plugin = plugin;
        this.pluginManager = pluginManager;
    }


    public void registerListeners()
    {
        pluginManager.registerEvents(new ClickListener(), plugin);
        pluginManager.registerEvents(new CloseListener(), plugin);
        pluginManager.registerEvents(new DestroyListener(), plugin);
        pluginManager.registerEvents(new HitListener(), plugin);
        pluginManager.registerEvents(new HoldListener(), plugin);
        pluginManager.registerEvents(new InteractListener(), plugin);
    }

}
