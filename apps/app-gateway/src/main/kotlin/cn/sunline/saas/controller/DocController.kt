package cn.sunline.saas.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import javax.websocket.server.PathParam

@Controller
@RequestMapping("doc")
class DocController {
    enum class DocType{
        Microloan
    }

    @GetMapping
    fun microloanDoc(@PathParam("type") type:DocType): ModelAndView {
        return when(type){
            DocType.Microloan -> ModelAndView("microloan")
        }
    }
}