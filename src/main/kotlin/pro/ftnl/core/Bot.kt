package pro.ftnl.core

import dev.minn.jda.ktx.jdabuilder.injectKTX
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag


/**
 * Bot manager class
 * @author OcelusPro
 * @version 1.0
 */
class Bot(private val token: String, private val listeners: List<Any>) {

    /**
     * The shard manager for the bot.
     * @see ShardManager
     * @see DefaultShardManagerBuilder
     * @return The shard manager for the bot.
     */
    fun start(): ShardManager {
        val builder = DefaultShardManagerBuilder.createDefault(token)
        builder.disableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
        builder.setMemberCachePolicy(MemberCachePolicy.ALL)
        builder.disableCache(
            CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOJI, CacheFlag.MEMBER_OVERRIDES,
            CacheFlag.ROLE_TAGS, CacheFlag.VOICE_STATE, CacheFlag.ONLINE_STATUS
        )
        builder.addEventListeners(listeners)
        builder.injectKTX()
        builder.setStatus(OnlineStatus.ONLINE)
        return builder.build()
    }
}