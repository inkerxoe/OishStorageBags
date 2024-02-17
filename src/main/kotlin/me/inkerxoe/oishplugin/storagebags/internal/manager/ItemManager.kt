package me.inkerxoe.oishplugin.storagebags.internal.manager

import me.inkerxoe.oishplugin.storagebags.OishStorageBags.plugin
import me.inkerxoe.oishplugin.storagebags.internal.module.ConfigModule
import me.inkerxoe.oishplugin.storagebags.utils.ItemUtil.toMap
import me.inkerxoe.oishplugin.storagebags.utils.ItemUtil.invalidNBT

import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack

import taboolib.module.nms.getItemTag
import java.io.File
import java.util.*

/**
 * OishStorageBags
 * me.inkerxoe.oishplugin.storagebags.internal.manager
 *
 * @author InkerXoe
 * @since 2024/2/17 下午2:50
 */
object ItemManager {
    /**
     * 保存物品
     *
     * @param itemStack 保存物品
     * @param id 物品ID
     * @return 1 保存成功; 0 ID冲突; 2 你保存了个空气
     */
    fun saveItem(itemStack: ItemStack, id: String): Int {
        // 检测是否为空气
        if (itemStack.type != Material.AIR) {
            // 获取文件路径
            val file = File(plugin.dataFolder, "${File.separator}bags${File.separator}$id.yml")
            if (!file.exists()) {
                file.createNewFile()
            } else {
                return 0
            }
            with(YamlConfiguration()) {
                options().pathSeparator(ConfigModule.pathSeparator)
                load(file)
                // 创建物品节点
                val configSection = createSection(id)
                // 设置物品材质
                configSection.set("material", itemStack.type.toString())
                // 设置子ID/损伤值
                if (itemStack.durability > 0) {
                    configSection.set("damage", itemStack.durability)
                }
                // 如果物品有ItemMeta
                if (itemStack.hasItemMeta()) {
                    // 获取ItemMeta
                    val itemMeta = itemStack.itemMeta
                    // 获取物品NBT
                    val itemNBT = itemStack.getItemTag()
                    // 获取显示信息
                    val display = itemNBT["display"]
                    itemNBT.remove("display")
                    // 设置CustomModolData
                    try {
                        if (itemMeta?.hasCustomModelData() == true) {
                            configSection.set("custommodeldata", itemMeta.customModelData)
                        }
                    } catch (error: NoSuchMethodError) {
                    }
                    // 设置物品名
                    if (itemMeta?.hasDisplayName() == true) {
                        configSection.set("name", itemMeta.displayName)
                    }
                    // 设置Lore
                    if (itemMeta?.hasLore() == true) {
                        configSection.set("lore", itemMeta.lore)
                    }
                    // 设置是否无法破坏
                    if (itemMeta?.isUnbreakable == true) {
                        configSection.set("unbreakable", itemMeta.isUnbreakable)
                    }
                    // 设置物品附魔
                    if (itemMeta?.hasEnchants() == true) {
                        val enchantSection = configSection.createSection("enchantments")
                        for ((enchant, level) in itemMeta.enchants) {
                            enchantSection.set(enchant.name, level)
                        }
                    }
                    // 设置ItemFlags
                    itemMeta?.itemFlags?.let {
                        if (it.isNotEmpty()) {
                            configSection.set("hideflags", it.map { flag -> flag.name })
                        }
                    }
                    // 设置物品颜色
                    display?.asCompound()?.let {
                        it["color"]?.asInt()?.let { color ->
                            configSection.set("color", color.toString(16).uppercase(Locale.getDefault()))
                        }
                    }
                    // 非法Name/Lore检测
                    display?.asCompound()?.also {
                        // Name/Lore格式化
                        val itemClone = itemStack.clone()
                        val cloneMeta = itemClone.itemMeta
                        cloneMeta?.setDisplayName(cloneMeta.displayName)
                        cloneMeta?.lore = cloneMeta?.lore
                        itemClone.itemMeta = cloneMeta
                        // 格式化后的display
                        val cloneDisplay = itemClone.getItemTag()["display"]?.asCompound()
                        // 非法Name
                        it["Name"]?.asString()?.let { name ->
                            if (name != cloneDisplay?.get("Name")?.asString()) {
                                configSection.set("illegalName", name)
                            }
                        }
                        // 非法Lore
                        it["Lore"]?.asList()?.let { lore ->
                            val cloneLore = cloneDisplay?.get("Lore")?.asList()
                            var illegal = false
                            if (cloneLore?.size != lore.size) {
                                illegal = true
                            }
                            for (i in 0 until lore.size) {
                                if (lore[i].asString() != cloneLore?.get(i)?.asString()) {
                                    illegal = true
                                }
                            }
                            if (illegal) {
                                configSection.set("illegalLore", lore.map { it.asString() })
                            }
                        }
                    }
                    // 设置物品NBT
                    if (!itemNBT.isEmpty()) {
                        configSection.set("nbt", itemNBT.toMap(invalidNBT))
                    }
                }
                // 保存文件
                save(file)
                return 1
            }
        }
        return 2
    }
}