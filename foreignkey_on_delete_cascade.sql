ALTER TABLE `PercolationStep` DROP FOREIGN KEY `FKB547075E29F27E9C` ;

ALTER TABLE `PercolationStep` ADD FOREIGN KEY ( `root` ) REFERENCES `parade`.`PercolationStep` (
`percolationstep`
) ON DELETE CASCADE ON UPDATE CASCADE ;

ALTER TABLE `PercolationStep` DROP FOREIGN KEY `FKB547075EDDD0B8B1` ;

ALTER TABLE `PercolationStep` ADD FOREIGN KEY ( `previous` ) REFERENCES `parade`.`PercolationStep` (
`percolationstep`
) ON DELETE CASCADE ON UPDATE CASCADE ;