package cn.sunline.saas.loan.model.enum

enum class EmploymentSubType(val employmentTypes: List<EmploymentType>) {
    PRIVATE(mutableListOf(EmploymentType.EMPLOYED)),
    GOVERNMENT(mutableListOf(EmploymentType.EMPLOYED)),
    BANGKO_SENTRAL_NG_PILIPINAS(mutableListOf(EmploymentType.EMPLOYED)),
    NON_GOVERNMENTAL(mutableListOf(EmploymentType.EMPLOYED)),
    OVERSEAS_FILIPINO_WORKER(mutableListOf(EmploymentType.EMPLOYED)),
    PROFESSIONAL(mutableListOf(EmploymentType.SELF)),
    SOLE_PROPRIETOR(mutableListOf(EmploymentType.OWNERS)),
    PARTNER(mutableListOf(EmploymentType.OWNERS)),
    STOCKHOLDER(mutableListOf(EmploymentType.OWNERS)),
}