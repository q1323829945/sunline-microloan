package cn.sunline.saas.base_jpa.repositories

import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.PagingAndSortingRepository
import java.io.Serializable

@NoRepositoryBean
interface BaseRepository<T, ID: Serializable>: JpaSpecificationExecutor<T>, PagingAndSortingRepository<T, ID>
