package cn.sunline.saas.menu.modules


data class Menu (
        val name:String,
        val parentName:String,
        val icon:String,
        val path:String,
        val sort:Int,
)


data class ResultMenu(
        val name:String,
        val parentName:String,
        val icon:String,
        val path:String,
        val children:List<ResultMenu>?,
        val sort:Int,
)
