package cn.sunline.saas.rbac.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.rbac.modules.Permission

interface PermissionRepository : BaseRepository<Permission, Long>