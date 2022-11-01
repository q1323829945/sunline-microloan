package cn.sunline.saas.workflow.step.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.workflow.step.modules.db.EventStep

interface EventStepRepository: BaseRepository<EventStep, Long>