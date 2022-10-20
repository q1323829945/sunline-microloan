package cn.sunline.saas.service

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.modules.db.Instance
import cn.sunline.saas.modules.dto.DTOInstance
import cn.sunline.saas.repository.InstanceRepository
import cn.sunline.saas.tools.RSASecretTools
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate

@Service
class InstanceService (private val instanceRepository: InstanceRepository
) : BaseRepoService<Instance, String>(instanceRepository){
    private val instanceMap = mutableMapOf<String,Instance>()

    fun register(dtoInstance: DTOInstance):Instance{
        return findByTenant(dtoInstance.tenant)?.run {
            updateOne(dtoInstance,this)
        }?:run{
            addOne(dtoInstance)
        }
    }

    private fun addOne(dtoInstance: DTOInstance):Instance{
        var uuid = UUID.randomUUID().toString()
        while (true){
            if(getOne(uuid) == null){
                break
            }
            uuid = UUID.randomUUID().toString()
        }

        val store = RSASecretTools.generatorKey()

        return save(
            Instance(
                uuid,
                dtoInstance.tenant,
                store.accessKey,
                store.secretKey,
                dtoInstance.enable
            )
        )
    }

    private fun updateOne(dtoInstance: DTOInstance,instance: Instance):Instance{
        instance.enabled = dtoInstance.enable
        return save(instance)
    }

    fun getInstance(id:String):Instance?{
        return instanceMap[id]?: run {
            getOne(id)?.apply { instanceMap[this.tenant] = this }
        }
    }

    fun findByTenant(tenant:String):Instance?{
        return get{ root,_,criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<String>("tenant"),tenant))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }

    fun findAll():List<Instance>{
        return getPaged(null, Pageable.unpaged()).content
    }

    fun deleteInstance(id:String){
        instanceRepository.deleteById(id)
        instanceMap.remove(id)
    }
}