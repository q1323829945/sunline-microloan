package cn.sunline.saas.redis.services

import cn.sunline.saas.redis.configs.RedisConfig
import org.redisson.Redisson
import org.redisson.api.RBlockingQueue
import org.redisson.api.RedissonClient
import org.redisson.codec.JsonJacksonCodec
import org.redisson.config.Config
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Component
class RedisClient(private var redisConfig: RedisConfig) {
    lateinit var redissonClient: RedissonClient

    @PostConstruct
    private fun initialize() {
        val redisServer  = mutableListOf<String>()
        redisConfig.server.split(",").toList().forEach {
            redisServer.add("redis://$it")
        }

        val config = Config()
        if (redisConfig.useCluster){
            config.useClusterServers().addNodeAddress(*redisServer.toTypedArray())
            if (!redisConfig.password.isNullOrBlank()) {
                config.useClusterServers().password = redisConfig.password
            }
            println("Redis client has been initialized in single server mode")
        } else if (redisConfig.useSentinel){
            config.useSentinelServers().addSentinelAddress(*redisServer.toTypedArray())
            if (!redisConfig.password.isNullOrBlank()) {
                config.useSentinelServers().password = redisConfig.password
            }
            println("Redis client has been initialized in cluster server mode")
        } else {
            config.useSingleServer().address = redisServer[0]
            if (!redisConfig.password.isNullOrBlank()) {
                config.useSingleServer().password = redisConfig.password
            }
            println("Redis client has been initialized in sentinel server mode")
        }

        redissonClient = Redisson.create(config)
    }

    @PreDestroy
    private fun dealloc() {
        redissonClient.shutdown(100, 100, TimeUnit.MILLISECONDS)
    }

    fun <T> addToList(key: String, value: T): Boolean {
        val listOps = redissonClient.getList<T>(key, JsonJacksonCodec())
        return listOps.add(value)
    }

    fun getListSize(key: String): Int {
        val listOps = redissonClient.getList<Any>(key, JsonJacksonCodec())
        return listOps.size
    }

    fun <T> getListByRange(key: String, from: Int, to: Int): List<T> {
        val listOps = redissonClient.getList<T>(key, JsonJacksonCodec())
        return listOps.range(from, to)?: emptyList()
    }

    fun deleteListByRange(key: String, from: Int, to: Int) {
        val listOps = redissonClient.getList<Any>(key, JsonJacksonCodec())
        listOps.trim(from, to)
    }

    fun deleteList(name: String) {
        redissonClient.getList<Any>(name,JsonJacksonCodec()).delete()
    }

    fun <T> addToMapIfAbsent(hash: String, key: String, value: T): Boolean {
        val hashOps = redissonClient.getMap<String, T>(hash, JsonJacksonCodec())
        return hashOps.putIfAbsent(key, value) != null
    }

    fun <T> addToMap(hash: String, key: String, value: T): Boolean {
        val hashOps = redissonClient.getMap<String, T>(hash, JsonJacksonCodec())
        return hashOps.put(key, value) != null
    }

    fun <T> getMapItem(hash: String, key: String): T? {
        val hashOps = redissonClient.getMap<String, T>(hash, JsonJacksonCodec())
        return hashOps[key]
    }

    fun mapContains(hash: String, key: String): Boolean {
        val hashOps = redissonClient.getMap<String, Any>(hash, JsonJacksonCodec())
        return hashOps.containsKey(key)
    }

    fun deleteMapItemByKey(hash: String, key: String): Any? {
        val hashOps = redissonClient.getMap<String, Any>(hash, JsonJacksonCodec())
        return hashOps.remove(key)
    }


    fun deleteMap(hash : String) {
        redissonClient.getMap<String,Any>(hash, JsonJacksonCodec()).delete()
    }

    fun <T> addToSet(hash: String, item: T): Boolean {
        val setOps = redissonClient.getSet<T>(hash, JsonJacksonCodec())
        return setOps.add(item)
    }

    fun <T> setContains(hash: String, item: T): Boolean {
        val setOps = redissonClient.getSet<T>(hash, JsonJacksonCodec())
        return setOps.contains(item)
    }

    fun <T> deleteSetItem(hash: String, item: T): Boolean {
        val setOps = redissonClient.getSet<T>(hash, JsonJacksonCodec())
        return setOps.remove(item)
    }

    final inline fun <reified T> subscribe(name: String, crossinline consumer: (T)->Unit): Int {
        return this.redissonClient.getTopic(name, JsonJacksonCodec()).addListener(T::class.java) { _, it -> consumer(it) }
    }

    fun unsubscribe(name: String, listenerId: Int) {
        redissonClient.getTopic(name, JsonJacksonCodec()).removeListener(listenerId)
    }

    fun <T> publish(name : String, message: T) {
        val redissonTopic = redissonClient.getTopic(name, JsonJacksonCodec())
        redissonTopic.publish(message)
    }

    fun <T> addToQueue(key: String, value: T): Boolean {
        val queue: RBlockingQueue<T> = redissonClient.getBlockingQueue(key, JsonJacksonCodec())
        return queue.offer(value)
    }

    fun <T> readFromQueue(key: String, limit: Int): List<T> {
        val queue: RBlockingQueue<T> = redissonClient.getBlockingQueue(key, JsonJacksonCodec())
        return queue.poll(limit)?: emptyList()
    }

    fun getQueueSize(key: String): Int {
        return redissonClient.getBlockingQueue<Any>(key, JsonJacksonCodec()).size
    }
}