CREATE TABLE `loan_upload_config` (
  `id` bigint NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `name` varchar(256) NOT NULL,
  `directory_id` bigint NOT NULL,
  `required` tinyint(1) NOT NULL,
  `created` datetime(6) DEFAULT NULL,
  `updated` datetime(6) DEFAULT NULL,
  `tenant_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_deleted` (`deleted`),
  CONSTRAINT `FK9iuykkrwaiwph24v8aeu6on77` FOREIGN KEY (`directory_id`) REFERENCES `document_template_directory` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `customer_offer` (
  `id` bigint NOT NULL,
  `customer_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `status` varchar(256) not null,
  `data` text not null,
  `datetime` tinyblob,
  `created` datetime(6) DEFAULT NULL,
  `updated` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `customer_loan_apply` (
  `customer_offer_id` bigint NOT NULL,
  `amount` decimal(21,2),
  `data` text not null,
  `created` datetime(6) DEFAULT NULL,
  `updated` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`customer_offer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `loan_product_upload_configure_mapping` (
     `product_id` bigint NOT NULL,
     `loan_upload_configure_id` bigint NOT NULL,
     KEY `FKpuihlymysj88vc9lp90rsocut` (`product_id`),
     KEY `FKmw1igvkrcpqq77w0o32gnbq2e` (`loan_upload_configure_id`),
     CONSTRAINT `FKmw1igvkrcpqq77w0o32gnbq2e` FOREIGN KEY (`loan_upload_configure_id`) REFERENCES `loan_upload_config` (`id`),
     CONSTRAINT `FKpuihlymysj88vc9lp90rsocut` FOREIGN KEY (`product_id`) REFERENCES `loan_product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
