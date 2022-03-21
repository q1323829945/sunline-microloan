CREATE TABLE `document_template_directory` (
  `id` bigint NOT NULL,
  `name`varchar(256) NOT NULL,
  `version`varchar(256) NOT NULL,
  `deleted`tinyint(1) not null,
  `created` datetime(6) DEFAULT NULL,
  `tenant_id` bigint NOT NULL,
  `updated` datetime(6) DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK14b3i1hjojchu040erkdt5m8a` (`parent_id`),
  CONSTRAINT `FK14b3i1hjojchu040erkdt5m8a` FOREIGN KEY (`parent_id`) REFERENCES `document_template_directory` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `document_template` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `directory_id` bigint NOT NULL,
  `document_store_reference` varchar(256) NOT NULL,
  `document_type` varchar(16) NOT NULL,
  `file_type` varchar(256) NOT NULL,
  `language_type` varchar(16) NOT NULL,
  `name` varchar(256) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9iuykkrwaiwph24v8aeu6onu5` (`directory_id`),
  CONSTRAINT `FK9iuykkrwaiwph24v8aeu6onu5` FOREIGN KEY (`directory_id`) REFERENCES `document_template_directory` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



