package cn.sunline.saas.rpc.invoke.impl

import cn.sunline.saas.party.person.service.PersonService
import cn.sunline.saas.rbac.service.dto.DTOPerson
import cn.sunline.saas.rpc.invoke.RbacInvoke
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RbacInvokeImpl: RbacInvoke {

    private val applId = "app-loan-management"

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var personService: PersonService

    override fun getPerson(id: Long?): DTOPerson? {

//        id?.run {
//            val person = RPCService.get<DTOResponseSuccess<DTOPerson>>(
//                serviceName = applId,
//                methodName = "Person/$id",
//                queryParams = mapOf(),
//                headerParams = mapOf(
//                    Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant().toString(),
//                    Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
//                ),
//                tenant = ContextUtil.getTenant().toString(),
//            )
//
//            return if(person == null){
//                null
//            } else {
//                objectMapper.convertValue(person.data!!)
//            }
//        }
//
//        return null

        //TODO: 暂时先直接调用，到时候通过dapr调用
        val peron = personService.getOne(id)

        return peron?.run { objectMapper.convertValue(this) }
    }
}