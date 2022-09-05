package cn.sunline.saas.channel.product.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.channel.product.model.db.Product

interface ProductRepository: BaseRepository<Product, Long> {
}