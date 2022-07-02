package pro.ftnl.modules.commands

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import org.reflections.Reflections
import pro.ftnl.core.toHumanTime

class CommandManager {
    private val commands = mutableListOf<ICmd>()
    private var hasStart = false

    private val cooldownMap = mutableMapOf<Pair<User, ICmd>, Long>()

    private fun loadCommands() {
        val reflections: List<Class<out ICmd>> =
            Reflections("pro.ftnl.modules.commands.list").getSubTypesOf(ICmd::class.java).filter { !it.isInterface }
        for (instance in reflections) commands.add(instance.getConstructor().newInstance()!!)
        cmdList = commands
    }
    private fun postDataCmd(jda: JDA){
        val toPost = commands.filterIsInstance<IDataCmd>().map { it.data }
        jda.updateCommands().addCommands(toPost).queue()
    }

    fun onBotReady(event: ReadyEvent){
        if (hasStart) return
        if (event.jda.shardInfo.shardId != 0) return

        loadCommands()
        postDataCmd(event.jda)
    }

    private fun checkCooldown(user: User, cmd: ICmd): Long{
        val cooldownKey = Pair(user, cmd)
        return System.currentTimeMillis() - (cooldownMap[cooldownKey] ?: 0)
    }

    fun handleSlashCmd(event: SlashCommandInteractionEvent) {
        val cmd = commands.filterIsInstance<ISlashCmd>().find { it.name == event.name } ?:
            return event.reply("Commande introuvable !").setEphemeral(true).queue()

        val env = ECmdEnvironment.retrieveEnvironment(event.channel)
        if (!cmd.environment.contains(env)) return event.reply("Commande non disponible dans ce type de salon !").setEphemeral(true).queue()
        if (event.isFromGuild){
            val member = event.member ?: return event.reply("Impossible de retrouver le membre exécutant la commande !").setEphemeral(true).queue()
            if (!cmd.permissions.all { member.hasPermission(event.channel as GuildChannel, it) })
                return event.reply("Vous n'avez pas les permissions requises pour exécuter cette commande !").setEphemeral(true).queue()
        }

        val check = checkCooldown(event.user, cmd)
        if (check > 0) return event.reply("Vous devez attendre ${check.toHumanTime() } secondes avant de pouvoir exécuter cette commande !").setEphemeral(true).queue()

        cmd.execute(event)
        cooldownMap[Pair(event.user, cmd)] = System.currentTimeMillis() + cmd.cooldown.second.toMillis(cmd.cooldown.first)
    }
    fun handleButtonCmd(event: ButtonInteractionEvent){
        val cmd = commands.filterIsInstance<IButtonCmd>().find { event.button.id?.startsWith(it.name) == true } ?:
            return event.reply("Commande introuvable !").setEphemeral(true).queue()

        if (event.isFromGuild){
            val member = event.member ?: return event.reply("Impossible de retrouver le membre exécutant la commande !").setEphemeral(true).queue()
            if (!cmd.permissions.all { member.hasPermission(event.channel as GuildChannel, it) })
                return event.reply("Vous n'avez pas les permissions requises pour exécuter cette commande !").setEphemeral(true).queue()
        }
        val check = checkCooldown(event.user, cmd)
        if (check > 0) return event.reply("Vous devez attendre ${check.toHumanTime() } secondes avant de pouvoir exécuter cette commande !").setEphemeral(true).queue()
        cmd.execute(event)
        cooldownMap[Pair(event.user, cmd)] = System.currentTimeMillis() + cmd.cooldown.second.toMillis(cmd.cooldown.first)
    }
    fun handleModalCmd(event: ModalInteractionEvent) {
        val cmd = commands.filterIsInstance<IModalCmd>().find { event.modalId.startsWith(it.name) } ?:
            return event.reply("Commande introuvable !").setEphemeral(true).queue()

        if (event.isFromGuild){
            val member = event.member ?: return event.reply("Impossible de retrouver le membre exécutant la commande !").setEphemeral(true).queue()
            if (!cmd.permissions.all { member.hasPermission(event.channel as GuildChannel, it) })
                return event.reply("Vous n'avez pas les permissions requises pour exécuter cette commande !").setEphemeral(true).queue()
        }
        val check = checkCooldown(event.user, cmd)
        if (check > 0) return event.reply("Vous devez attendre ${check.toHumanTime() } secondes avant de pouvoir exécuter cette commande !").setEphemeral(true).queue()
        cmd.execute(event)
        cooldownMap[Pair(event.user, cmd)] = System.currentTimeMillis() + cmd.cooldown.second.toMillis(cmd.cooldown.first)
    }
    fun handleUserContextCmd(event: UserContextInteractionEvent) {
        val cmd = commands.filterIsInstance<IUserCmd>().find { event.name == it.name } ?:
            return event.reply("Commande introuvable !").setEphemeral(true).queue()

        val channel = event.channel
        if (channel != null){
            val env = ECmdEnvironment.retrieveEnvironment(channel)
            if (!cmd.environment.contains(env)) return event.reply("Commande non disponible dans ce type de salon !").setEphemeral(true).queue()
        }

        if (event.isFromGuild){
            val member = event.member ?: return event.reply("Impossible de retrouver le membre exécutant la commande !").setEphemeral(true).queue()
            if (!cmd.permissions.all { member.hasPermission(event.channel as GuildChannel, it) })
                return event.reply("Vous n'avez pas les permissions requises pour exécuter cette commande !").setEphemeral(true).queue()
        }
        val check = checkCooldown(event.user, cmd)
        if (check > 0) return event.reply("Vous devez attendre ${check.toHumanTime() } secondes avant de pouvoir exécuter cette commande !").setEphemeral(true).queue()

        cmd.execute(event)
        cooldownMap[Pair(event.user, cmd)] = System.currentTimeMillis() + cmd.cooldown.second.toMillis(cmd.cooldown.first)
    }
    fun handleMessageContextCmd(event: MessageContextInteractionEvent){
        val cmd = commands.filterIsInstance<IMessageCmd>().find { event.name == it.name } ?:
            return event.reply("Commande introuvable !").setEphemeral(true).queue()

        val channel = event.channel
        if (channel != null){
            val env = ECmdEnvironment.retrieveEnvironment(channel)
            if (!cmd.environment.contains(env)) return event.reply("Commande non disponible dans ce type de salon !").setEphemeral(true).queue()
        }

        if (event.isFromGuild){
            val member = event.member ?: return event.reply("Impossible de retrouver le membre exécutant la commande !").setEphemeral(true).queue()
            if (!cmd.permissions.all { member.hasPermission(event.channel as GuildChannel, it) })
                return event.reply("Vous n'avez pas les permissions requises pour exécuter cette commande !").setEphemeral(true).queue()
        }
        val check = checkCooldown(event.user, cmd)
        if (check > 0) return event.reply("Vous devez attendre ${check.toHumanTime() } secondes avant de pouvoir exécuter cette commande !").setEphemeral(true).queue()

        cmd.execute(event)
        cooldownMap[Pair(event.user, cmd)] = System.currentTimeMillis() + cmd.cooldown.second.toMillis(cmd.cooldown.first)
    }

    companion object {
        lateinit var cmdList: List<ICmd>
    }
}