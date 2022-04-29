package cn.sunline.saas.invoice.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.invoice.model.db.Invoice

/**
 * @title: InvoiceRepository
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/25 16:26
 */
interface InvoiceRepository : BaseRepository<Invoice, Long>