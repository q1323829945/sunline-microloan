package cn.sunline.saas.channel.services

import cn.sunline.saas.global.constant.PositionType
import cn.sunline.saas.channel.rbac.modules.Position
import cn.sunline.saas.channel.rbac.modules.User
import cn.sunline.saas.channel.rbac.modules.dto.DTOPositionAdd
import cn.sunline.saas.channel.rbac.services.PositionService
import cn.sunline.saas.channel.rbac.services.UserService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PositionServiceTest {
    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var positionService: PositionService

    lateinit var adminUser: User
    lateinit var user1: User
    lateinit var user2: User

    @BeforeAll
    fun init(){
        val adminUser = userService.register(User(username = "admin", password = "admin", email = "admin@sunline.cn"))
        this.adminUser = adminUser
        val user1 = userService.register(User(username = "user1", password = "user1", email = "admin@sunline.cn"))
        this.user1 = user1
        val user2 = userService.register(User(username = "user2", password = "user2", email = "admin@sunline.cn"))
        this.user2 = user2

        positionService.defaultPositions.forEach {
            val position = positionService.addOne(DTOPositionAdd(name = it))
            Assertions.assertThat(position).isNotNull
            Assertions.assertThat(position.id).isEqualTo(it)
            Assertions.assertThat(position.name).isEqualTo(it)
        }

        val position = positionService.addOne(DTOPositionAdd("test"))
        Assertions.assertThat(position).isNotNull
        Assertions.assertThat(position.id).isNotEqualTo("test")
        Assertions.assertThat(position.name).isEqualTo("test")
    }

    @Test
    fun `update position`(){
        val oldOne = positionService.getOne(PositionType.ADMIN.position)
        Assertions.assertThat(oldOne).isNotNull
        val users = mutableListOf(adminUser,user1,user2)
        val newOne = Position(PositionType.ADMIN.position,PositionType.ADMIN.position,null,users)
        val position = positionService.updateOne(oldOne!!,newOne)
        Assertions.assertThat(position.users?.size).isEqualTo(3)
    }

    @Test
    fun `get position`(){
        val position = positionService.getDetails(PositionType.SUPPLEMENT.position)
        Assertions.assertThat(position).isNotNull
        Assertions.assertThat(position.id).isEqualTo(PositionType.SUPPLEMENT.position)
        Assertions.assertThat(position.name).isEqualTo(PositionType.SUPPLEMENT.position)
    }

    @Test
    fun `get paged`(){
        val paged = positionService.paged(null, Pageable.unpaged())
        Assertions.assertThat(paged.size).isEqualTo(3)
    }
}