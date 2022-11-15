package cn.sunline.saas.template.data.service.impl

import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.service.TemplateDataBaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.reflect.KClass


@Service
class CommonTemplateDataServiceImpl : TemplateDataBaseService() {

    @Autowired
    private lateinit var sequence: Sequence

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    private fun initData(): Map<String, Any> {
        val mapOf = mutableMapOf<String, Any>()
        mapOf["id"] = sequence.nextId()
        return mapOf
    }

    override fun <T : Any> deepCustomAssignment(type: KClass<T>, map: Map<String, Any>?): T {
        return deepBaseAssignment(type, map ?: initData())
    }
}