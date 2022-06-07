package cn.sunline.saas.statistics.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.statistics.modules.db.BusinessDetail

interface BusinessDetailRepository : BaseRepository<BusinessDetail, Long> {
}