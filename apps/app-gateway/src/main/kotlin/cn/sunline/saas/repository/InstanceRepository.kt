package cn.sunline.saas.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.modules.db.Instance

interface InstanceRepository: BaseRepository<Instance, String> {
}