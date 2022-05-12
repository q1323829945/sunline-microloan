package cn.sunline.saas.rbac.invoke.impl

import cn.sunline.saas.rbac.invoke.RbacInvoke
import cn.sunline.saas.rbac.service.dto.DTOPerson
import org.springframework.stereotype.Component

@Component
class RbacInvokeImpl:RbacInvoke {

    override fun getPerson(id: Long): DTOPerson {
        TODO("Not yet implemented")
    }
}