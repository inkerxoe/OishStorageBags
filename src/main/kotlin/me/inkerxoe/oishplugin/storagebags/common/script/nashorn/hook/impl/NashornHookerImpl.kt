package me.inkerxoe.oishplugin.storagebags.common.script.nashorn.hook.impl

import me.inkerxoe.oishplugin.storagebags.common.script.nashorn.hook.NashornHooker
import pers.neige.neigeitems.libs.nashorn.api.scripting.NashornScriptEngineFactory
import pers.neige.neigeitems.libs.nashorn.api.scripting.ScriptObjectMirror
import javax.script.Invocable
import javax.script.ScriptEngine

/**
 * OishStorageBags
 * me.inkerxoe.oishplugin.storagebags.common.script.nashorn.hook.impl
 *
 * @author InkerXoe
 * @since 2024/2/17 上午10:13
 */
/**
 * openjdk nashorn挂钩
 *
 * @constructor 启用openjdk nashorn挂钩
 */
class NashornHookerImpl : NashornHooker() {
    override fun getNashornEngine(args: Array<String>, classLoader: ClassLoader): ScriptEngine {
        return NashornScriptEngineFactory().getScriptEngine(args, classLoader)
    }

    override fun invoke(
        compiledScript: me.inkerxoe.oishplugin.storagebags.common.script.nashorn.script.CompiledScript,
        function: String,
        map: Map<String, Any>?,
        vararg args: Any
    ): Any? {
        val newObject: ScriptObjectMirror =
            (compiledScript.scriptEngine as Invocable).invokeFunction("newObject") as ScriptObjectMirror
        map?.forEach { (key, value) -> newObject[key] = value }
        return newObject.callMember(function, *args)
    }

    override fun isFunction(engine: ScriptEngine, func: Any?): Boolean {
        if (func is ScriptObjectMirror && func.isFunction) {
            return true
        }
        return false
    }
}