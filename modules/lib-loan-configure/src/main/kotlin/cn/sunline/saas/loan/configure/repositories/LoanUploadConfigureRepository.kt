package cn.sunline.saas.loan.configure.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.loan.configure.modules.db.LoanUploadConfigure

interface LoanUploadConfigureRepository : BaseRepository<LoanUploadConfigure, Long>{

    fun findByDeleted(deleted:Boolean):List<LoanUploadConfigure>
}