CREATE TABLE `games` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `created_on` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `rows_quantity` bigint(11) unsigned NOT NULL,
  `columns_quantity` bigint(11) unsigned NOT NULL,
  `mines_quantity` bigint(11) unsigned NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'PLAYING',
  `user_name` varchar(128) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE `cells` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `created_on` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `game_id` bigint(11) unsigned,
  `row_number` bigint(11) unsigned NOT NULL,
  `col_number` bigint(11) unsigned NOT NULL,
  `cell_content` varchar(10) NOT NULL DEFAULT 'NUMBER',
  `mines_around` bigint(11) DEFAULT NULL,
  `cell_operation` varchar(20) NOT NULL DEFAULT 'NONE',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

