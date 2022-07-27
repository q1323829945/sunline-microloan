package cn.sunline.saas.menu.services

import cn.sunline.saas.menu.modules.Menu
import cn.sunline.saas.menu.modules.ResultMenu
import org.springframework.stereotype.Service

@Service
class MenuService {

    private lateinit var menuList:List<Menu>


    init {
        val businessConfig = Menu("businessConfig","","","",1)
        val sysConfig = Menu("sysConfig","","","",2)
        val enterpriseConfig = Menu("enterpriseConfig","","","",3)
        val businessHandling= Menu("businessHandling","","","",4)

        val roleConfig = Menu("roleConfig","sysConfig","","/dashboard/role",5)
        val permissionConfig = Menu("permissionConfig","sysConfig","","/dashboard/permissions",6)
        val userConfig = Menu("userConfig","sysConfig","","/dashboard/user",7)
        val ratePlanConfig = Menu("ratePlanConfig","businessConfig","","/dashboard/ratePlan",8)
        val documentTemplateConfig = Menu("documentTemplateConfig","businessConfig","","/dashboard/documentTemplate",9)
        val loanProductConfig = Menu("loanProductConfig","businessConfig","","/dashboard/loanProduct",10)
        val riskControlRuleConfig = Menu("riskControlRuleConfig","businessConfig","","/dashboard/riskControlRule",11)
        val organisationConfig = Menu("organisationConfig","enterpriseConfig","","/dashboard/organisationConfig",12)
        val employeeConfig = Menu("employeeConfig","enterpriseConfig","","/dashboard/employeeConfig",13)
        val customerConfig = Menu("customerConfig","businessConfig","","/dashboard/customerConfig",14)
        val customerOfferConfig = Menu("customerOfferConfig","businessHandling","","/dashboard/customerOfferConfig",15)
        val loanAgreementManagementConfig = Menu("loanAgreementManagementConfig","businessHandling","","/dashboard/loanAgreementManagementConfig",16)
        val repaymentManagementConfig = Menu("repaymentManagementConfig","businessHandling","","/dashboard/repaymentManagementLoanConfig",17)
        val pdpaConfig = Menu("pdpaConfig","businessConfig","","/dashboard/pdpaConfig",18)
        val pdpaAuthorityConfig = Menu("pdpaAuthorityConfig","businessConfig","","/dashboard/pdpaAuthorizationConfig",19)
        val loanRetrieveManagementConfig = Menu("loanRetrieveManagementConfig","businessHandling","","/dashboard/loanRetrieveManagementConfig",20)


        menuList = listOf(businessConfig,sysConfig,enterpriseConfig,businessHandling
            ,roleConfig
            ,permissionConfig
            ,userConfig
            ,ratePlanConfig
            ,documentTemplateConfig
            ,loanProductConfig
            ,riskControlRuleConfig
            ,organisationConfig
            ,customerConfig
            ,customerOfferConfig
            ,loanAgreementManagementConfig
            ,employeeConfig
            ,repaymentManagementConfig
            ,pdpaConfig
            ,pdpaAuthorityConfig,
            loanRetrieveManagementConfig)

    }


    fun getPermissionMenu(menuName: Set<String>): List<ResultMenu> {
        val baseMenuSet = getPermissionSelectMenu(menuName)

        return menuRecursion("", baseMenuSet)!!
    }

    private fun menuRecursion(parentName: String,menuSet:HashSet<Menu>):List<ResultMenu>?{
        val children:ArrayList<ResultMenu> = arrayListOf()
        for(menu in menuSet){
            if(menu.parentName == parentName){
                children.add(ResultMenu(menu.name,menu.parentName,menu.icon,menu.path,menuRecursion(menu.name,menuSet)))
            }
        }

        if(children.size == 0){
            return null
        }
        return children
    }

    private fun getPermissionSelectMenu(menuName:Set<String>):HashSet<Menu>{
        //得到选中菜单
        val permissionMenu = menuList.filter {
            menuName.contains(it.name)
        }.toHashSet()

        val resultMenu = hashSetOf<Menu>()
        resultMenu.addAll(permissionMenu)
        val tempMenu = hashSetOf<Menu>()
        while (true){
            tempMenu.clear()
            tempMenu.addAll(permissionMenu)
            permissionMenu.clear()
            var flag = true
            for(menu in tempMenu){
                if(menu.parentName != ""){
                    val parent = menuList.filter {
                        it.name == menu.parentName }[0]
                    permissionMenu.add(parent)
                    flag = false
                }
            }

            resultMenu.addAll(permissionMenu)

            if (flag){
                return resultMenu
            }
        }

    }



}
