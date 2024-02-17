package me.inkerxoe.oishplugin.storagebags.utils

import ulid.ULID

/**
 * OishStorageBags
 * me.inkerxoe.oishplugin.storagebags.utils
 *
 * @author InkerXoe
 * @since 2024/2/17 下午12:22
 */
/**
 * ULID相关工具类
 */
object ULIDUtil {
    /**
     * 生成并返回一个新的ULID字符串
     */
    fun generate(): String {
        return ULID.randomULID()
    }

    /**
     * 验证提供的字符串是否为有效的ULID
     * @param ulidStr 字符串形式的ULID
     * @return 布尔值，指示ULID是否有效
     */
    fun isValid(ulidStr: String): Boolean {
        return try {
            ULID.parseULID(ulidStr)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}