CREATE TABLE `users`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `name`       varchar(32)  NOT NULL DEFAULT '',
    `email`      varchar(100) NOT NULL DEFAULT '',
    `role`       varchar(15)  NOT NULL,
    `password`   varchar(70)  NOT NULL DEFAULT '',
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email` (`email`)
);

CREATE TABLE `access_tokens`
(
    `id`         bigint      NOT NULL AUTO_INCREMENT,
    `user_id`    int         NOT NULL,
    `token`      varchar(70) NOT NULL DEFAULT '',
    `expire_at`  timestamp   NOT NULL,
    `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `token` (`token`),
    KEY          `user_id` (`user_id`)
);

