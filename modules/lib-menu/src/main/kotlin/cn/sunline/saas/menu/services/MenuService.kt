package cn.sunline.saas.menu.services

import cn.sunline.saas.menu.modules.Menu
import cn.sunline.saas.menu.modules.ResultMenu
import org.springframework.stereotype.Service

@Service
class MenuService {

    private lateinit var menuList:List<Menu>


    init {
        val businessConfig = Menu("businessConfig","","","")
        val sysConfig = Menu("sysConfig","","","")
        val enterpriseConfig = Menu("enterpriseConfig","","","")
        val businessHandling= Menu("businessHandling","","","")

        val roleConfig = Menu("roleConfig","sysConfig","","/dashboard/role")
        val permissionConfig = Menu("permissionConfig","sysConfig","","/dashboard/permissions")
        val userConfig = Menu("userConfig","sysConfig","","/dashboard/user")
        val ratePlanConfig = Menu("ratePlanConfig","businessConfig","","/dashboard/ratePlan")
        val documentTemplateConfig = Menu("documentTemplateConfig","businessConfig","","/dashboard/documentTemplate")
        val loanProductConfig = Menu("loanProductConfig","businessConfig","","/dashboard/loanProduct")
        val riskControlRuleConfig = Menu("riskControlRuleConfig","businessConfig","","/dashboard/riskControlRule")
        val organisationConfig = Menu("organisationConfig","enterpriseConfig","","/dashboard/organisationConfig")
        val employeeConfig = Menu("employeeConfig","enterpriseConfig","","/dashboard/employeeConfig")
        val customerConfig = Menu("customerConfig","businessConfig","","/dashboard/customerConfig")
        val customerOfferConfig = Menu("customerOfferConfig","businessHandling","","/dashboard/customerOfferConfig")
        val loanAgreementManagementConfig= Menu("loanAgreementManagementConfig","businessHandling","","/dashboard/loanAgreementManagementConfig")
        val repaymentManagementConfig= Menu("repaymentManagementConfig","businessHandling","","/dashboard/repaymentManagementLoanConfig")



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
            ,repaymentManagementConfig)

    }


    fun getPermissionMenu(menuName: Set<String>): List<ResultMenu> {
        val baseMenuSet = getPermissionSelectMenu(menuName)

        return menuRecursion("", baseMenuSet)!!
    }

    fun menuRecursion(parentName: String,menuSet:HashSet<Menu>):List<ResultMenu>?{
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

    fun getPermissionSelectMenu(menuName:Set<String>):HashSet<Menu>{
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
