package cn.sunline.saas.workflow.step.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.workflow.step.modules.db.EventStepData

interface EventStepDataRepository: BaseRepository<EventStepData, Long>