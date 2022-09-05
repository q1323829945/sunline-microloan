package cn.sunline.saas.loan.model.enum

enum class PositionSubType(val positionTypes: List<PositionType>){
    OWNER(mutableListOf(PositionType.SELF)),
    NON_OFFICER(mutableListOf(PositionType.SELF)),
    ELECTED(mutableListOf(PositionType.GOVERNMENT)),
    EMPLOYEE(mutableListOf(PositionType.GOVERNMENT)),
    CONTRACTUAL(mutableListOf(PositionType.SELF,PositionType.GOVERNMENT)),
}
