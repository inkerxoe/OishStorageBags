package me.inkerxoe.oishplugin.storagebags.internal

import me.inkerxoe.oishplugin.storagebags.OishStorageBags.plugin
import me.inkerxoe.oishplugin.storagebags.utils.ToolsUtil
import org.bukkit.Bukkit
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang
import taboolib.module.metrics.Metrics

/**
 * OishStorageBags
 * me.inkerxoe.oishplugin.storagebags.internal
 *
 * @author InkerXoe
 * @since 2024/2/17 上午9:46
 */
object OishStorageBagsLoader {
    @Awake(LifeCycle.LOAD)
    fun load() {
        console().sendMessage("")
        console().sendLang("Plugin-Loading", Bukkit.getServer().version)
        console().sendMessage("")
        Metrics(21018, plugin.description.version, Platform.BUKKIT)
    }

    @Awake(LifeCycle.ENABLE)
    fun enable() {
        ToolsUtil.printLogo()
        console().sendLang("Plugin-Enabled")
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        console().sendLang("Plugin-Disable")
    }
}