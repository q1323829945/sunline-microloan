package cn.sunline.saas.base_jpa.services

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.io.Serializable

@Component
abstract class BaseRepoService<T, ID: Serializable>(private val baseRepository: BaseRepository<T, ID>) {


    fun getOne(id: ID): T? {
        return baseRepository.findByIdOrNull(id)
    }

    fun getByIds(ids: Iterable<ID>): Iterable<T> {
        return baseRepository.findAllById(ids)
    }

    fun getPaged(specification: Specification<T>? = null, pageable: Pageable): Page<T> {
        return if (specification == null) {
            baseRepository.findAll(pageable)
        } else {
            baseRepository.findAll(specification, pageable)
        }


    }

    fun save(entity: T): T {
        return baseRepository.save(entity!!)
    }

    fun save(list: Iterable<T>): Iterable<T> {
        return baseRepository.saveAll(list)
    }

}