package cn.sunline.saas.document.template.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.document.template.modules.db.LoanUploadConfigure

interface LoanUploadConfigureRepository : BaseRepository<LoanUploadConfigure, Long>{

    fun findByDeleted(deleted:Boolean):List<LoanUploadConfigure>
}