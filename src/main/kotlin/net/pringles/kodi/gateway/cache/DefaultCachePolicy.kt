package net.pringles.kodi.gateway.cache

import net.pringles.kodi.gateway.cache.modelCaches.*
import net.pringles.kodi.models.Guild
import net.pringles.kodi.models.Member
import net.pringles.kodi.models.Role
import net.pringles.kodi.models.User
import net.pringles.kodi.models.channels.BaseChannel
import java.util.concurrent.ConcurrentHashMap

object DefaultCachePolicy {
    private val userCache = ConcurrentHashMap<Long, User>()
    private val guildCache = ConcurrentHashMap<Long, Guild>()
    private val channelCache = ConcurrentHashMap<Long, BaseChannel>()
    private val roleCache = ConcurrentHashMap<Long, Role>()
    private val memberCache = ConcurrentHashMap<MemberCacheKey, Member>()

    fun user() =
        UserCache().apply {
            view { userCache.values.toList() }
            get { userCache[it] }
            update { userCache[it.id] = it }
            remove { userCache.remove(it) }
        }

    fun guild() =
        GuildCache().apply {
            view { guildCache.values.toList() }
            get { id -> guildCache[id] }
            update { guild -> guildCache[guild.id] = guild }
            remove { id -> guildCache.remove(id) }
        }

    fun channel() =
        ChannelCache().apply {
            view { channelCache.values.toList() }
            get { id -> channelCache[id] }
            update { channel -> channelCache[channel.id] = channel }
            remove { id -> channelCache.remove(id) }
        }

    fun role() =
        RoleCache().apply {
            view { roleCache.values.toList() }
            get { id -> roleCache[id] }
            update { role -> roleCache[role.id] = role }
            remove { id -> roleCache.remove(id) }
        }

    fun member() =
        MemberCache().apply {
            view { memberCache.values.toList() }
            get { memberCache[it] }
            update { memberCache[MemberCacheKey(it.guildId, it.id)] = it }
            remove { memberCache.remove(it) }
        }

    fun <K, V> none() = EmptyCache<K, V>()
}
