package pro.ftnl.modules.commands

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.components.Modal
import java.util.concurrent.TimeUnit

interface ICmd {
    val name: String
    val permissions: List<Permission>
    val environment: List<ECmdEnvironment>
        get() = ECmdEnvironment.values().toList()
    val cooldown: Pair<Long, TimeUnit>
}


interface IDataCmd : ICmd {
    val data: CommandData
}
interface IMessageCmd : IDataCmd {
    override val data: CommandData
        get() = Commands.message(name)
    fun execute(event: MessageContextInteractionEvent)
}
interface IUserCmd : IDataCmd {
    override val data: CommandData
        get() = Commands.user(name)
    fun execute(event: UserContextInteractionEvent)
}
interface ISlashCmd : IDataCmd {
    val description: String
    override val data: CommandData
        get() = Commands.slash(name, description)

    fun execute(event: SlashCommandInteractionEvent)
}


interface IButtonCmd: ICmd {
    fun execute(event: ButtonInteractionEvent)
}
interface IModalCmd: ICmd {
    val modal: Modal
    fun execute(event: ModalInteractionEvent)
}
