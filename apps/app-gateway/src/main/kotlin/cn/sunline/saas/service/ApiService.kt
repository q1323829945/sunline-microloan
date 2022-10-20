package cn.sunline.saas.service

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.modules.db.Api
import cn.sunline.saas.modules.dto.DTOApi
import cn.sunline.saas.repository.ApiRepository
import cn.sunline.saas.seq.Sequence
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

@Service
class ApiService (private val apiRepository: ApiRepository,
                  private val sequence: Sequence,
                  private val gatewayService: GatewayService
) : BaseRepoService<Api, Long>(apiRepository){

    fun addOne(dtoApi: DTOApi):Api{
        return findByParams(dtoApi.serverId,dtoApi.api,dtoApi.method.toString())?:run {
            val path = if(dtoApi.api.startsWith("/")){
                dtoApi.api
            }else {
                "/${dtoApi.api}"
            }

            val api = save(
                Api(
                    id = sequence.nextId(),
                    serverId = dtoApi.serverId,
                    api = path,
                    method = dtoApi.method.toString(),
                    formatType = dtoApi.formatType
                )
            )

            gatewayService.addApi(api)
            api
        }
    }

    fun updateOne(dtoApi: DTOApi):Api?{
        val api = findByParams(dtoApi.serverId,dtoApi.api,dtoApi.method.toString())?.run {
            this.enabled = dtoApi.enabled
            if(this.enabled){
                gatewayService.addApi(this)
            } else {
                gatewayService.removeApi(this)
            }
            save(this)
        }
        return api
    }

    fun deleteOne(dtoApi: DTOApi){
        findByParams(dtoApi.serverId,dtoApi.api,dtoApi.method.toString())?.run {
            apiRepository.delete(this)
            gatewayService.removeApi(this)
        }
    }


    fun findByParams(instanceId:String,api:String,httpMethod: String):Api?{
        return get{ root,_,criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<String>("instanceId"),instanceId))
            predicates.add(criteriaBuilder.equal(root.get<String>("api"),api))
            predicates.add(criteriaBuilder.equal(root.get<String>("method"),httpMethod))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }

    fun getPagedByInstanceId(instanceId: String):Page<Api>{
        return getPaged({root,_,criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<String>("instanceId"),instanceId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())
    }

}
