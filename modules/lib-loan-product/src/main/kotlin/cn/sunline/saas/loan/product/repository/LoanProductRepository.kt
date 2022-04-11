package cn.sunline.saas.loan.product.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.loan.product.model.db.LoanProduct
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query

/**
 * @title: LoanProductRepository
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/7 15:19
 */
interface LoanProductRepository:BaseRepository<LoanProduct,Long>{
    fun findByIdentificationCode(identificationCode:String):MutableList<LoanProduct>?


    @Query(value ="select lp2.id," +
                    "lp2.description," +
                    "lp2.identification_code," +
                    "lp2.loan_product_type," +
                    "lp2.loan_purpose," +
                    "lp2.name," +
                    "lp2.status," +
                    "lp2.tenant_id," +
                    "lp2.`version` " +
            "from loan_product lp2 " +
            "inner join " +
                "(select identification_code,max(version) max_version from loan_product lp group by identification_code) lp3 " +
            "on lp2.identification_code = lp3.identification_code and lp2 .version =lp3.max_version " +
            "where " +
                "if(:loanProductType is not null and :loanProductType != '' , loan_product_type =:loanProductType,1=1) " +
            "and " +
                "if(:loanPurpose is not null and :loanPurpose != '' , loan_purpose =:loanPurpose,1=1) " +
            "and " +
                "if(:name is not null and :name != '' , name =:name,1=1)"
            , countQuery = "select count(1) from loan_product lp2 " +
                "inner join " +
                "(select identification_code,max(version) max_version from loan_product lp group by identification_code) lp3 " +
                "on lp2.identification_code = lp3.identification_code and lp2 .version =lp3.max_version " +
                "where " +
                "if(:loanProductType is not null and :loanProductType != '' , loan_product_type =:loanProductType,1=1) " +
                "and " +
                "if(:loanPurpose is not null and :loanPurpose != '' , loan_purpose =:loanPurpose,1=1) " +
                "and " +
                "if(:name is not null and :name != '' , name =:name,1=1)"
            , nativeQuery = true
    )
    fun getLoanProductPaged(name:String?,
                            loanProductType: LoanProductType?,
                            loanPurpose: String?,
                            pageable: Pageable
    ): Page<LoanProduct>
}