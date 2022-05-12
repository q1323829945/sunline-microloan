package cn.sunline.saas.rbac.invoke

import cn.sunline.saas.rbac.service.dto.DTOPerson

interface RbacInvoke {
    fun getPerson(id:Long):DTOPerson
}