CREATE TABLE `permission` (
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
                              `remark` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                              `tag` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
                              PRIMARY KEY (`id`),
                              KEY `idx_permission_tag` (`tag`),
                              KEY `idx_permission_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `role` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `created` datetime(6) DEFAULT NULL,
                        `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
                        `remark` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                        `updated` datetime(6) DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        KEY `idx_role_name` (`name`),
                        KEY `idx_role_created` (`created`),
                        KEY `idx_role_updated` (`updated`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `role_permission_mapping` (
                                           `role_id` bigint NOT NULL,
                                           `permission_id` bigint NOT NULL,
                                           KEY `FKpuihlymysj88vc9lp90rsocug` (`permission_id`),
                                           KEY `FKmw1igvkrcpqq77w0o32gnbq2r` (`role_id`),
                                           CONSTRAINT `FKmw1igvkrcpqq77w0o32gnbq2r` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
                                           CONSTRAINT `FKpuihlymysj88vc9lp90rsocug` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `user` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `accountExpired` tinyint(1) NOT NULL DEFAULT '0',
                        `accountLocked` tinyint(1) NOT NULL DEFAULT '0',
                        `created` datetime(6) DEFAULT NULL,
                        `credentialsExpired` tinyint(1) NOT NULL DEFAULT '0',
                        `email` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
                        `enabled` tinyint(1) NOT NULL DEFAULT '1',
                        `password` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
                        `updated` datetime(6) DEFAULT NULL,
                        `username` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
                        `jwtKey` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `username` (`username`),
                        KEY `idx_user_enabled` (`enabled`),
                        KEY `idx_user_account_expired` (`accountExpired`),
                        KEY `idx_user_credentials_expired` (`credentialsExpired`),
                        KEY `idx_user_account_locked` (`accountLocked`),
                        KEY `idx_user_created` (`created`),
                        KEY `idx_user_updated` (`updated`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `user_role_mapping` (
                                     `user_id` bigint NOT NULL,
                                     `role_id` bigint NOT NULL,
                                     KEY `FKivsrdkkmm4e9k19vgaat9upp2` (`role_id`),
                                     KEY `FK9583w0afd8oat2vyjnjbg2sq0` (`user_id`),
                                     CONSTRAINT `FK9583w0afd8oat2vyjnjbg2sq0` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                                     CONSTRAINT `FKivsrdkkmm4e9k19vgaat9upp2` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;