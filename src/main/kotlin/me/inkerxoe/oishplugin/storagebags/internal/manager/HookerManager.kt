package me.inkerxoe.oishplugin.storagebags.internal.manager

import me.inkerxoe.oishplugin.storagebags.common.script.nashorn.hook.NashornHooker
import me.inkerxoe.oishplugin.storagebags.common.script.nashorn.hook.impl.LegacyNashornHookerImpl
import me.inkerxoe.oishplugin.storagebags.common.script.nashorn.hook.impl.NashornHookerImpl

/**
 * OishStorageBags
 * me.inkerxoe.oishplugin.storagebags.internal.manager
 *
 * @author InkerXoe
 * @since 2024/2/17 上午10:20
 */
/**
 * 插件兼容管理器, 用于尝试与各个软依赖插件取得联系
 */
object HookerManager {
    private fun check(clazz: String): Boolean {
        return try {
            Class.forName(clazz)
            true
        } catch (error: Throwable) {
            false
        }
    }

    val nashornHooker: NashornHooker =
        when {
            // jdk自带nashorn
            check("jdk.nashorn.api.scripting.NashornScriptEngineFactory") -> LegacyNashornHookerImpl()
            // 主动下载nashorn
            else -> NashornHookerImpl()
        }
}

