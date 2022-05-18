package cn.sunline.saas.dapr_wrapper.actor

import cn.sunline.saas.dapr_wrapper.actor.model.ActorContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * @title: ActorContextTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/17 14:00
 */
class ActorContextTest {

    @BeforeEach
    fun `emulate command runner`() {
        Test1RegisterActor().registerActor()
        Test2RegisterActor().registerActor()
    }

    @Test
    fun `context map correct`() {
        val context = ActorContext.getActorTypes()
        assertThat(context.size).isEqualTo(2)
    }

    @Test
    fun `do job`() {
        val actor = ActorContext.getActor("Test1RegisteredActor")
        Assertions.assertThrows(Exception::class.java) {
            actor.doJob("first")
        }
    }

}