package cn.sunline.saas.channel.statistics.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.channel.statistics.modules.db.CustomerStatistics

interface CustomerStatisticsRepository : BaseRepository<CustomerStatistics, Long> {
}