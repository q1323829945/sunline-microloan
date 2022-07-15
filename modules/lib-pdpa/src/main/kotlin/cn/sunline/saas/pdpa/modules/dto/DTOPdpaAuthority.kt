package cn.sunline.saas.pdpa.modules.dto

data class DTOPdpaAuthority(
    var isElectronicSignature: Boolean = false,
    var isFaceRecognition : Boolean = false,
    var isFingerprint: Boolean = false,
)