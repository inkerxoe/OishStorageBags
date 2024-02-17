package me.inkerxoe.oishplugin.storagebags.internal.manager

import me.inkerxoe.oishplugin.storagebags.utils.ConfigUtil
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.util.concurrent.ConcurrentHashMap

/**
 * OishStorageBags
 * me.inkerxoe.oishplugin.storagebags.internal.manager
 *
 * @author InkerXoe
 * @since 2024/2/17 下午1:45
 */
object ConfigManager {
    val bagMap: ConcurrentHashMap<String, ItemStack> = ConcurrentHashMap<String, ItemStack>()
    val itemMap: ConcurrentHashMap<String, ItemStack> = ConcurrentHashMap<String, ItemStack>()

    @Awake(LifeCycle.ENABLE)
    private fun loadConfigs() {
        val bagConf = ConfigUtil.getAllFiles("bags").filter { it.endsWith(".yml") }
        val itemConf = ConfigUtil.getAllFiles("items").filter { it.endsWith(".yml") }

    }

    fun reload() {
        bagMap.clear()
        itemMap.clear()
        loadConfigs()
    }
}