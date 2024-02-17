package me.inkerxoe.oishplugin.storagebags.internal.module

import me.inkerxoe.oishplugin.storagebags.OishStorageBags
import me.inkerxoe.oishplugin.storagebags.utils.ConfigUtil
import me.inkerxoe.oishplugin.storagebags.utils.ConfigUtil.saveResourceNotWarn
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.io.File

/**
 * OishStorageBags
 * me.inkerxoe.oishplugin.storagebags.internal.module
 *
 * @author InkerXoe
 * @since 2024/2/17 下午1:54
 */
object ConfigModule {
    val debug
        get() = OishStorageBags.setting.getBoolean("options.debug", false)
    val pathSeparator
        get() = OishStorageBags.setting.getString("options.path_separator")!!.toCharArray().first()
    val bagIdentifiers
        get() = OishStorageBags.setting.getString("options.bag_identifiers", "OishStorageBag")

    /**
     * 加载默认配置文件
     */
    @Awake(LifeCycle.INIT)
    fun saveResource() {
        if (ConfigUtil.getFileOrNull("items") == null) {
            val file = File(OishStorageBags.plugin.dataFolder, File.separator +"items")
            file.mkdirs()
        }
        if (ConfigUtil.getFileOrNull("bags") == null) {
            OishStorageBags.plugin.saveResourceNotWarn("bags${File.separator}ExampleBag.yml")
        }
    }

    /**
     * 重载配置管理器
     */
    fun reload() {
        OishStorageBags.setting.reload()
        saveResource()
    }
}