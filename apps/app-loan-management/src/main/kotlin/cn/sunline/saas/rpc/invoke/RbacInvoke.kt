package cn.sunline.saas.rpc.invoke

import cn.sunline.saas.rbac.service.dto.DTOPerson

interface RbacInvoke {
    fun getPerson(id:Long?):DTOPerson?
}