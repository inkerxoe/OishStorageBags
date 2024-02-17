package me.inkerxoe.oishplugin.storagebags

import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.platform.BukkitPlugin

/**
 * OishStorageBags
 * me.inkerxoe.oishplugin.storagebags
 *
 * @author InkerXoe
 * @since 2024/2/17 上午9:27
 */
object OishStorageBags : Plugin() {
    val plugin by lazy { BukkitPlugin.getInstance() }

    @Config("setting.yml", autoReload = false)
    lateinit var setting: ConfigFile
}