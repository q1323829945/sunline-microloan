package cn.sunline.saas.huaweicloud.apig.constant

data class BodyParams(
    val auth:Auth
)

data class Auth(
    val identity:Identity,
    val scope:Scope? = null // project or domain ;if null get global server token
)

data class Identity(
    val methods:List<String> = listOf("password"),
    val password:Password
)

data class Password(
    val user:User
)

data class User(
    val domain:UserDomain,
    val name:String,
    val password:String
)

data class UserDomain(
    val name:String
)

data class TokenParams(
    val token:String,
    val time:String,
)

data class Scope(
    val domain:ScopeDomain? = null,
    val project:Project? = null
)
data class ScopeDomain(
    val id:String? = null,
    val name:String? = null
)
data class Project(
    val id:String? = null,
    val name:String? = null
)
