package net.beeapm.agent.boot;

import net.beeapm.agent.loader.AbstractLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 启动插件加载
 *
 * @author yuan
 */
public class BootPluginLoader extends AbstractLoader {
    public static List<AbstractBootPlugin> loadPlugins() {
        Map<String, AbstractBootPlugin> pluginMap = load(new String[]{"boot-plugins", "ext-lib"}, "bee-boot.def");
        List<AbstractBootPlugin> pluginList = new ArrayList<AbstractBootPlugin>(16);
        for (Map.Entry<String, AbstractBootPlugin> entry : pluginMap.entrySet()) {
            AbstractBootPlugin plugin = entry.getValue();
            plugin.setName(entry.getKey());
            pluginList.add(plugin);
        }
        return pluginList;
    }

}
