package cn.sunline.saas.pdpa.factory

import cn.sunline.saas.pdpa.modules.dto.PDPAInformationView

interface PDPAApi {
    fun getPDPA(): PDPAInformationView
}