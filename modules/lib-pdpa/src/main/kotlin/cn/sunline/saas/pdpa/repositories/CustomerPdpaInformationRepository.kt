package cn.sunline.saas.pdpa.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.pdpa.modules.db.CustomerPdpaInformation

interface CustomerPdpaInformationRepository : BaseRepository<CustomerPdpaInformation, Long> {
}