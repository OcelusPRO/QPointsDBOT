package pro.ftnl.modules.commands

import net.dv8tion.jda.api.entities.*

enum class ECmdEnvironment {
    PRIVATE_CHANNEL,
    VOICE_CHANNEL,
    THREAD_CHANNEL,
    TEXT_CHANNEL,
    NEWS_CHANNEL;

    companion object {
        fun retrieveEnvironment(channel: Channel): ECmdEnvironment {
            return when(channel) {
                is VoiceChannel -> ECmdEnvironment.VOICE_CHANNEL
                is NewsChannel -> ECmdEnvironment.NEWS_CHANNEL
                is ThreadChannel -> ECmdEnvironment.THREAD_CHANNEL
                is TextChannel -> ECmdEnvironment.TEXT_CHANNEL
                is PrivateChannel -> ECmdEnvironment.PRIVATE_CHANNEL
                else -> throw IllegalArgumentException("Unknown channel type")
            }
        }
    }
}