CREATE TABLE `fee_feature` (
  `id` bigint NOT NULL,
  `fee_amount` decimal(19,2) DEFAULT NULL,
  `fee_deduct_type` varchar(32) NOT NULL,
  `fee_method_type` varchar(32) NOT NULL,
  `fee_ratio` decimal(9,6) DEFAULT NULL,
  `fee_type` varchar(32) NOT NULL,
  `product_id` bigint NOT NULL,
  `tenant_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_fee_feature_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `interest_feature` (
  `id` bigint NOT NULL,
  `interest_type` varchar(32) NOT NULL,
  `product_id` bigint NOT NULL,
  `rate_plan_id` bigint NOT NULL,
  `tenant_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_interest_feature_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `interest_product_feature_modality` (
  `id` bigint NOT NULL,
  `adjustFrequency` varchar(32) NOT NULL,
  `base_year_days` varchar(32) NOT NULL,
  `tenant_id` bigint NOT NULL,
  `frequency` varchar(32) NOT NULL,
  `payment_method` varchar(64) NOT NULL,
  `repaymentDayType` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `loan_product` (
  `id` bigint NOT NULL,
  `description` varchar(512) NOT NULL,
  `identification_code` varchar(16) NOT NULL,
  `loan_product_type` varchar(32) NOT NULL,
  `loan_purpose` varchar(256) NOT NULL,
  `name` varchar(64) NOT NULL,
  `status` varchar(32) NOT NULL,
  `tenant_id` bigint NOT NULL,
  `version` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `repayment_feature` (
  `id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `tenant_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_repayment_feature_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `prepayment_feature_modality` (
  `id` bigint NOT NULL,
  `penalty_ratio` decimal(9,6) NOT NULL,
  `tenant_id` bigint NOT NULL,
  `term` varchar(32) NOT NULL,
  `type` varchar(32) NOT NULL,
  `repayment_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK60h53ff1oqy9m91rutor8xr6d` (`repayment_id`),
  CONSTRAINT `FK60h53ff1oqy9m91rutor8xr6d` FOREIGN KEY (`repayment_id`) REFERENCES `repayment_feature` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `repayment_feature_modality` (
  `id` bigint NOT NULL,
  `frequency` varchar(32) NOT NULL,
  `payment_method` varchar(64) NOT NULL,
  `repaymentDayType` varchar(32) NOT NULL,
  `tenant_id` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `repayment_product_feature` (
  `id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `tenant_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_repayment_product_feature_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `condition` (
  `id` bigint NOT NULL,
  `created` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `marker` varchar(128) NOT NULL,
  `max_value_range` bigint NOT NULL not null,
  `min_value_range` bigint not null not null,
  `type` varchar(128) NOT NULL,
  `reference_id` bigint NOT NULL,
  `updated` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt6qm86xvlechpl1m368dynlbh` (`reference_id`),
  CONSTRAINT `FKt6qm86xvlechpl1m368dynlbh` FOREIGN KEY (`reference_id`) REFERENCES `loan_product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;