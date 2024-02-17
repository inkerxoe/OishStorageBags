package me.inkerxoe.oishplugin.storagebags.internal.command.subcommand

import me.inkerxoe.oishplugin.storagebags.OishStorageBags
import me.inkerxoe.oishplugin.storagebags.internal.manager.ItemManager
import me.inkerxoe.oishplugin.storagebags.internal.module.ConfigModule
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import taboolib.module.lang.sendLang
import taboolib.module.nms.getItemTag
import taboolib.module.nms.setItemTag
import kotlin.contracts.contract

/**
 * OishStorageBags
 * me.inkerxoe.oishplugin.storagebags.internal.command.subcommand
 *
 * @author InkerXoe
 * @since 2024/2/17 下午2:43
 */
object CreateBag {
    val createBag = subCommand {
        // bag createBag [ID]
        dynamic {
            suggestion<Player>(uncheck = true) { _, _ ->
                arrayListOf("id")
            }
            execute<Player> { sender, _, argument ->
                submit(async = true) {
                    when (ItemManager.saveItem(sender.inventory.itemInMainHand, argument)) {
                        // 创建成功
                        0 -> {
                            val item = sender.inventory.itemInMainHand
                            val itemTag = item.getItemTag()
                            itemTag.remove("OishSorageBags")
                            itemTag.put("OishStorageBags", ConfigModule.bagIdentifiers)
                            sender.inventory.setItemInMainHand(item.setItemTag(itemTag))
                            console().sendLang("Create-Bag-successSaveInfo", argument)
                        }
                        // 已存在对应ID的Bag
                        1 -> {
                            console().sendLang("Create-Bag-existedKey", argument)
                        }
                        // 你创建了个空气
                        2 -> {
                            console().sendLang("Create-Bag-airItem")
                        }
                    }
                }
            }
        }
    }
}