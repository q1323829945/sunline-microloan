package cn.sunline.saas.pdpa.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.pdpa.modules.db.Pdpa

interface PdpaRepository : BaseRepository<Pdpa, Long> {
}