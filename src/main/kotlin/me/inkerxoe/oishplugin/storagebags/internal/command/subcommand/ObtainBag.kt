package me.inkerxoe.oishplugin.storagebags.internal.command.subcommand

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand

/**
 * OishStorageBags
 * me.inkerxoe.oishplugin.storagebags.internal.command.subcommand
 *
 * @author InkerXoe
 * @since 2024/2/17 下午12:48
 */
object ObtainBag {
    // bag giveBag [玩家ID] [储存袋ID] (数量) > 根据ID给予储存袋
    val giveBag = subCommand {  }

    // bag getBag [储存袋ID] (数量) > 根据ID获取储存袋
    val getBag = subCommand {  }

    private fun giveCommand(
        sender: CommandSender,
        player: Player?,
        id: String,
        amount: Int?
    ) {
        player?.let {
            // 获取数量
            amount?.let {
                // 给物品

            }
        }
    }
}