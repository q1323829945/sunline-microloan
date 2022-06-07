package cn.sunline.saas.statistics.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.statistics.modules.db.CustomerStatistics

interface CustomerStatisticsRepository : BaseRepository<CustomerStatistics, Long> {
}