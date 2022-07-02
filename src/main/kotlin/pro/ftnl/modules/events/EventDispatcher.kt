package pro.ftnl.modules.events

import dev.minn.jda.ktx.events.CoroutineEventListener
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import pro.ftnl.modules.commands.CommandManager

class EventDispatcher: CoroutineEventListener {

    val manager = CommandManager()

    override suspend fun onEvent(event: GenericEvent) {
        when (event) {
            is ReadyEvent -> {
                manager.onBotReady(event)
            }
            is SlashCommandInteractionEvent -> { manager.handleSlashCmd(event) }
            is ButtonInteractionEvent -> { manager.handleButtonCmd(event) }
            is ModalInteractionEvent -> { manager.handleModalCmd(event) }
            is UserContextInteractionEvent -> { manager.handleUserContextCmd(event) }
            is MessageContextInteractionEvent -> { manager.handleMessageContextCmd(event) }
        }
    }
}