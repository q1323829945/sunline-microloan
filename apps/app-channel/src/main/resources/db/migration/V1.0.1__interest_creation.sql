CREATE TABLE `rate_plan` (
  `id` bigint NOT NULL,
  `name` varchar(32) NOT NULL,
  `tenant_id` bigint NOT NULL,
  `rate_plan_type` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `interest_rate` (
  `id` bigint NOT NULL,
  `period` varchar(32) NOT NULL,
  `rate` decimal(9,6) NOT NULL,
  `rate_plan_id` bigint NOT NULL,
  `tenant_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_interest_rate_plan_id` (`rate_plan_id`),
  CONSTRAINT `FKmyicrbd4fitjw4krssw826fhd` FOREIGN KEY (`rate_plan_id`) REFERENCES `rate_plan` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci