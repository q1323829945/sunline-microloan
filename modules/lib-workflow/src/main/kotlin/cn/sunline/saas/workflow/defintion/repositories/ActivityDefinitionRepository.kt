package cn.sunline.saas.workflow.defintion.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.workflow.defintion.modules.db.ActivityDefinition
import cn.sunline.saas.workflow.defintion.modules.db.ProcessDefinition

interface ActivityDefinitionRepository: BaseRepository<ActivityDefinition, Long>