package cn.sunline.saas.statistics.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.statistics.modules.db.ApiDetail
import java.util.Date

interface ApiDetailRepository : BaseRepository<ApiDetail, Long> {
}