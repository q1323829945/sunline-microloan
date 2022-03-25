package cn.sunline.saas.pdpa.factory

import cn.sunline.saas.pdpa.modules.PDPAInformation

interface PDPAApi {
    fun getPDPA(): PDPAInformation
}