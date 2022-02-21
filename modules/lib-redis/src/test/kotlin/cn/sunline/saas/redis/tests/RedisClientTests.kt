package cn.sunline.saas.redis.tests

import cn.sunline.saas.redis.configs.TestRedisConfiguration
import cn.sunline.saas.redis.services.RedisClient
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = [TestRedisConfiguration::class])
class RedisClientTests {
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    data class TestObject(var name: String? = null)

    @Autowired
    private lateinit var redisClient: RedisClient

    @Test
    fun testRedisList() {
        val repeat = 5
        val redisKey = "test_key_list"

        repeat(repeat) {
            redisClient.addToList(redisKey, TestObject(name = "name_$it"))
        }

        var size = redisClient.getListSize(redisKey)
        assert(size == repeat) { "List items should have $repeat items but got $size" }

        var list = redisClient.getListByRange<TestObject>(redisKey, 0, repeat)
        assert(list.size == repeat) { "Items fetched by range should have $repeat items but got ${list.size}" }

        redisClient.deleteListByRange(redisKey, 0, 0)
        size = redisClient.getListSize(redisKey)
        assert(size == 1) { "List size should be 1 but got $size" }

        list = redisClient.getListByRange<TestObject>(redisKey, 0, -1)
        assert(list[0].name == "name_0") { "List item at index 0 should have the correct name" }

        redisClient.deleteList(redisKey)
        size = redisClient.getListSize(redisKey)
        assert(size == 0) { "List should be empty" }
    }

    @Test
    fun testRedisMap() {
        val redisHash = "test_hash_map"
        val redisKey = "test_key_map"
        val testObjectOne = TestObject(name = "name_1")
        val testObjectTwo = TestObject(name = "name_2")

        redisClient.addToMap(redisHash, redisKey, testObjectOne)
        var fetchedObject = redisClient.getMapItem<TestObject>(redisHash, redisKey)
        assert(fetchedObject != null) { "Fetched object must not be empty" }
        assert(fetchedObject!!.name == testObjectOne.name) { "Fetched object should have the correct name" }

        redisClient.addToMapIfAbsent(redisHash, redisKey, testObjectTwo)
        fetchedObject = redisClient.getMapItem<TestObject>(redisHash, redisKey)
        assert(fetchedObject!!.name == testObjectOne.name) { "Fetched object should still at the previous value" }

        redisClient.addToMap(redisHash, redisKey, testObjectTwo)
        fetchedObject = redisClient.getMapItem<TestObject>(redisHash, redisKey)
        assert(fetchedObject!!.name == testObjectTwo.name) { "Fetched object should now have the new value" }

        var found = redisClient.mapContains(redisHash, redisKey)
        assert (found) { "Item must exist by looking up by hash and key" }

        redisClient.deleteMapItemByKey(redisHash, redisKey)
        found = redisClient.mapContains(redisHash, redisKey)
        assert (!found) { "Item should no longer exist" }

        redisClient.addToMap(redisHash, redisKey, testObjectOne)
        redisClient.deleteMap(redisHash)
        found = redisClient.mapContains(redisHash, redisKey)
        assert (!found) { "Item should no longer exist" }
    }

    @Test
    fun testRedisSet() {
        val redisKey = "test_key_set"
        val testObjectOne = TestObject(name = "name_1")
        val testObjectTwo = TestObject(name = "name_2")

        redisClient.addToSet(redisKey, testObjectOne)
        var found = redisClient.setContains(redisKey, testObjectOne)
        assert(found) { "Item one should be found" }

        found = redisClient.setContains(redisKey, testObjectTwo)
        assert(!found) { "Item two should not be found" }

        redisClient.addToSet(redisKey, testObjectTwo)
        found = redisClient.setContains(redisKey, testObjectTwo)
        assert(found) { "Item two should now be found" }

        redisClient.deleteSetItem(redisKey, testObjectOne)
        found = redisClient.setContains(redisKey, testObjectOne)
        assert(!found) { "Item one should no longer be found" }
    }

    @Test
    fun testRedisQueue() {
        val redisKey = "test_key_queue"
        val testObjectOne = TestObject(name = "name_1")
        val testObjectTwo = TestObject(name = "name_2")

        redisClient.addToQueue(redisKey, testObjectOne)
        var size = redisClient.getQueueSize(redisKey)
        assert(size == 1) { "Should have 1 item in the queue" }

        redisClient.addToQueue(redisKey, testObjectTwo)
        size = redisClient.getQueueSize(redisKey)
        assert(size == 2) { "Should have 2 item in the queue" }

        val list = redisClient.readFromQueue<TestObject>(redisKey, 10)
        assert(list.size == 2) { "Should have 2 item in the list" }

        val sortedList = list.sortedBy { it.name }
        assert(sortedList[0].name == testObjectOne.name) { "Sorted item 1 should have the correct name" }
        assert(sortedList[1].name == testObjectTwo.name) { "Sorted item 2 should have the correct name" }

        size = redisClient.getQueueSize(redisKey)
        assert(size == 0) { "Queue should be empty" }
    }

    @Test
    fun testRedisPubSub() {
        val redisKey = "test_key_topic"
        val testObjectOne = TestObject(name = "name_1")
        val testObjectTwo = TestObject(name = "name_2")

        var capturedObject: TestObject? = null

        val listener = redisClient.subscribe<TestObject>(redisKey) {
            capturedObject = it
        }

        redisClient.publish(redisKey, testObjectOne)
        Thread.sleep(100)
        assert(capturedObject != null) { "Captured object should not be null" }
        assert(capturedObject!!.name == testObjectOne.name) { "Captured object should have item 1 name" }

        redisClient.publish(redisKey, testObjectTwo)
        Thread.sleep(100)
        assert(capturedObject!!.name == testObjectTwo.name) { "Captured object should have item 2 name" }

        redisClient.unsubscribe(redisKey, listener)
    }
}