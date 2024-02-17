package me.inkerxoe.oishplugin.storagebags.common.script.nashorn.script

import java.io.File
import java.io.Reader

/**
 * OishStorageBags
 * me.inkerxoe.oishplugin.storagebags.common.script.nashorn.script
 *
 * @author InkerXoe
 * @since 2024/2/17 上午10:16
 */
class ScriptExpansion : CompiledScript {
    /**
     * 构建JavaScript脚本扩展
     *
     * @property reader js脚本文件
     * @constructor JavaScript脚本扩展
     */
    constructor(reader: Reader) : super(reader)

    /**
     * 构建JavaScript脚本扩展
     *
     * @property file js脚本文件
     * @constructor JavaScript脚本扩展
     */
    constructor(file: File) : super(file)

    /**
     * 构建JavaScript脚本扩展
     *
     * @property script js脚本文本
     * @constructor JavaScript脚本扩展
     */
    constructor(script: String) : super(script)

    override fun loadLib() {
        scriptEngine.eval(
            """
                const Bukkit = Packages.org.bukkit.Bukkit
                const plugin = Packages.me.inkerxoe.oishplugin.storagebags.OishStorageBags.plugin
                const pluginManager = Bukkit.getPluginManager()
                const scheduler = Bukkit.getScheduler()
                
                function sync(task) {
                    if (Bukkit.isPrimaryThread()) {
                        task()
                    } else {
                        scheduler.callSyncMethod(plugin, task)
                    }
                }
                
                function async(task) {
                    scheduler["runTaskAsynchronously(Plugin,Runnable)"](plugin, task)
                }
            """.trimIndent()
        )
    }
}