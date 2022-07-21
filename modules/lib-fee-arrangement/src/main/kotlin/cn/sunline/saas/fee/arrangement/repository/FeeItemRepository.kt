package cn.sunline.saas.fee.arrangement.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.fee.arrangement.model.db.FeeArrangement
import cn.sunline.saas.fee.arrangement.model.db.FeeItem

interface FeeItemRepository : BaseRepository<FeeItem, Long>