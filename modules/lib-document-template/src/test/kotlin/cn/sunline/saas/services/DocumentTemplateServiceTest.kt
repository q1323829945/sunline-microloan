package cn.sunline.saas.services

import cn.sunline.saas.document.model.DocumentType
import cn.sunline.saas.document.template.modules.FileType
import cn.sunline.saas.global.constant.LanguageType
import cn.sunline.saas.document.template.modules.db.DocumentTemplate
import cn.sunline.saas.document.template.modules.db.DocumentTemplateDirectory
import cn.sunline.saas.document.template.modules.db.LoanUploadConfigure
import cn.sunline.saas.document.template.services.DocumentTemplateDirectoryService
import cn.sunline.saas.document.template.services.DocumentTemplateService
import cn.sunline.saas.document.template.services.LoanUploadConfigureService
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.io.FileInputStream


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DocumentTemplateServiceTest(
    @Autowired val documentTemplateDirectoryService: DocumentTemplateDirectoryService,
    @Autowired val documentTemplateService: DocumentTemplateService,
    @Autowired val loanUploadConfigureService: LoanUploadConfigureService,
    @Autowired val tenantService: TenantService) {

    var directoryId = 0L
    var templateId = 0L
    var loadUploadConfigureId = 0L

    @BeforeAll
    fun `init`() {
        tenantService.save(
            Tenant(
                id = 12344566,
                country = CountryType.CHN,
            )
        )

        ContextUtil.setTenant("12344566")

        val directory = documentTemplateDirectoryService.addOne(
            DocumentTemplateDirectory(
                id = null,
                name = "directory1",
                version = "1",
                deleted = false,
                parent = null,
            )
        )

        directoryId = directory.id!!

        val file = FileInputStream(File("src\\test\\resources\\file\\testdoc.docx"))

        val template = documentTemplateService.addDocumentTemplate(
            DocumentTemplate(
                id = null,
                name = "123",
                documentStoreReference = "$directoryId/testdoc.docx",
                directoryId = directory.id!!,
                fileType = FileType.DOCX,
                languageType = LanguageType.EN_PH,
                documentType = DocumentType.CERTIFICATE
            ),file
        )

        templateId = template.id!!


        val loadUploadConfig = loanUploadConfigureService.addOne(
            LoanUploadConfigure(
                id = 1,
                name = "loadUploadConfigure1",
                required = false,
                deleted = false,
                directoryId = directory.id!!,
            )
        )

        loadUploadConfigureId = loadUploadConfig.id
    }


    @Test
    fun `get directory list`(){
        val directory = documentTemplateDirectoryService.queryAll()

        Assertions.assertThat(directory.size).isEqualTo(1)
    }

    @Test
    fun `update directory`(){
        val directory = documentTemplateDirectoryService.getOne(directoryId)!!
        val updateOne = documentTemplateDirectoryService.update(directory,
            DocumentTemplateDirectory(
                id = null,
                name = "d2",
                version = "1"
            ))

        Assertions.assertThat(updateOne.name).isEqualTo("d2")
    }


    @Test
    fun `update template`(){
        val template = documentTemplateService.getOne(templateId)!!

        val updateOne = documentTemplateService.updateDocumentTemplate(
            template,
            DocumentTemplate(
                id = null,
                name = "testdocx22222",
                documentStoreReference = "$directoryId/testdoc.docx",
                directoryId = directoryId,
                fileType = FileType.DOCX,
                languageType = LanguageType.EN_PH,
                documentType = DocumentType.CERTIFICATE,
            ),
            FileInputStream(File("src\\test\\resources\\file\\testdoc.docx"))
        )
        Assertions.assertThat(updateOne.name).isEqualTo("testdocx22222")
    }
}