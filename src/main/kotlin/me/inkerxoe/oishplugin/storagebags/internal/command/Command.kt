package me.inkerxoe.oishplugin.storagebags.internal.command

import me.inkerxoe.oishplugin.storagebags.internal.command.subcommand.CreateBag
import me.inkerxoe.oishplugin.storagebags.internal.command.subcommand.ObtainBag
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper

/**
 * OishStorageBags
 * me.inkerxoe.oishplugin.storagebags.internal.command
 *
 * @author InkerXoe
 * @since 2024/2/17 下午12:43
 */
@CommandHeader(
    name = "OishStorageBags",
    aliases = ["osb", "bag"],
    description = "OishStorageBags Main Command"
)
object Command {
    @CommandBody
    val main = mainCommand { createHelper() }
    @CommandBody
    val help = subCommand { createHelper() }
    @CommandBody
    val giveBag = ObtainBag.giveBag
    @CommandBody
    val getBag = ObtainBag.getBag
    @CommandBody
    val createBag = CreateBag.createBag
}