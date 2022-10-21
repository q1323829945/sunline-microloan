package cn.sunline.saas.service

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.modules.db.Instance
import cn.sunline.saas.modules.db.Server
import cn.sunline.saas.modules.dto.DTOServer
import cn.sunline.saas.repository.ServerRepository
import cn.sunline.saas.seq.Sequence
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

@Service
class ServerService(private val serverRepository: ServerRepository,
                    private val sequence: Sequence,
) : BaseRepoService<Server, Long>(serverRepository) {
    private val serverMap = mutableMapOf<Long,Server>()

    fun register(instance: Instance,dtoServer: DTOServer):Server{
        return findByInstanceAndServer(instance.id,dtoServer.server)?.run {
            updateServer(dtoServer,this)
        }?:run{
            addServer(instance,dtoServer)
        }
    }

    private fun addServer(instance: Instance,dtoServer: DTOServer):Server{
        val server = save(Server(
            id = sequence.nextId(),
            instanceId = instance.id,
            server = dtoServer.server,
            domain = dtoServer.domain,
            enabled = dtoServer.enabled
        ))

        serverMap[server.id] = server

        return server
    }

    private fun updateServer(dtoServer: DTOServer,server: Server):Server{
        server.domain = dtoServer.domain
        val update = save(server)
        serverMap[update.id] = update
        return update
    }

    fun findByInstanceAndServer(instanceId:String,server:String):Server?{
        return serverMap.values.firstOrNull { it.instanceId == instanceId && it.server == server }?: run{
            get{ root,_,criteriaBuilder ->
                val predicates = mutableListOf<Predicate>()
                predicates.add(criteriaBuilder.equal(root.get<String>("instanceId"),instanceId))
                predicates.add(criteriaBuilder.equal(root.get<String>("server"),server))
                criteriaBuilder.and(*(predicates.toTypedArray()))
            }?.apply { serverMap[this.id] = this }
        }
    }

    fun remove(id:Long):Server?{
        val server = getOne(id)?: return null
        serverRepository.delete(server)
        serverMap.remove(id)
        return server
    }
}