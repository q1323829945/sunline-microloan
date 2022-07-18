package cn.sunline.saas.pdpa.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.pdpa.exception.PdpaAuthorityNotFoundException
import cn.sunline.saas.pdpa.modules.db.PdpaAuthority
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaAuthority
import cn.sunline.saas.pdpa.repositories.PdpaAuthorityRepository
import cn.sunline.saas.seq.Sequence
import org.springframework.stereotype.Service

@Service
class PdpaAuthorityService(
    private val pdpaAuthorityRepository: PdpaAuthorityRepository,
    private val sequence: Sequence
): BaseMultiTenantRepoService<PdpaAuthority, Long>(pdpaAuthorityRepository) {

    fun register(){
        val pdpaAuthority = getOneWithTenant(null)
        pdpaAuthority?: run {
            save(PdpaAuthority(sequence.nextId()))
        }
    }

    fun updateOne(dtoPdpaAuthority: DTOPdpaAuthority):PdpaAuthority{
        val pdpaAuthority = getOne()
        pdpaAuthority.isElectronicSignature = dtoPdpaAuthority.isElectronicSignature
        pdpaAuthority.isFingerprint = dtoPdpaAuthority.isFingerprint
        pdpaAuthority.isFaceRecognition = dtoPdpaAuthority.isFaceRecognition
        return save(pdpaAuthority)
    }

    fun getOne(): PdpaAuthority {
        return getOneWithTenant(null)?: throw PdpaAuthorityNotFoundException("Invalid Authority")
    }
}