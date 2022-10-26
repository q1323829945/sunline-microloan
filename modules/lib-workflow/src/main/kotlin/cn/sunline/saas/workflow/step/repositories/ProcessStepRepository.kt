package cn.sunline.saas.workflow.step.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.workflow.defintion.modules.db.ProcessDefinition
import cn.sunline.saas.workflow.step.modules.db.ProcessStep

interface ProcessStepRepository: BaseRepository<ProcessStep, Long>