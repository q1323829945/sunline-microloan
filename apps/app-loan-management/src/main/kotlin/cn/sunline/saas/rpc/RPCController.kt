package cn.sunline.saas.rpc

import cn.sunline.saas.dapr_wrapper.actor.ActorReminderService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/RPC")
class RPCController {


    @DeleteMapping("actors/{actorType}/{actorId}/reminders/{name}")
    fun deleteActor(@PathVariable(value = "actorType")actorType: String,
              @PathVariable(value = "actorId")actorId: String,
              @PathVariable(value = "name")name: String){
        ActorReminderService.deleteReminders(actorType,actorId,name)
    }

}