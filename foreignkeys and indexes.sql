-- Foreign Key Constraints

ALTER TABLE `PercolationStep` DROP FOREIGN KEY `FKB547075E29F27E9C` ;

ALTER TABLE `PercolationStep` ADD FOREIGN KEY ( `root` ) REFERENCES `parade`.`PercolationStep` (
`percolationstep`
) ON DELETE CASCADE ON UPDATE CASCADE ;

ALTER TABLE `PercolationStep` DROP FOREIGN KEY `FKB547075EDDD0B8B1` ;

ALTER TABLE `PercolationStep` ADD FOREIGN KEY ( `previous` ) REFERENCES `parade`.`PercolationStep` (
`percolationstep`
) ON DELETE CASCADE ON UPDATE CASCADE ;

-- Indexes for optimalisation of the queries
CREATE INDEX `IDX_OBJECTURL` ON ALE (`objectURL`);
CREATE INDEX `IDX_FOCUS` ON `ALE` (`focus`);
CREATE INDEX `IDX_NIMBUS` ON `ALE` (`nimbus`);

CREATE INDEX `IDX_OBJECTURL` ON `MatchedAetherEvent` (`objectURL`);
CREATE INDEX `IDX_ACTOR` ON `MatchedAetherEvent` (`actor`);

CREATE INDEX `IDX_SUBJECT` ON `PercolationRule` (`subject`);
CREATE INDEX `IDX_PREDICATE` ON `PercolationRule` (`predicate`);
CREATE INDEX `IDX_ACTIVE` ON `PercolationRule` (`active`);

CREATE INDEX `IDX_OBJECTURL` ON `PercolationStep` (`objectURL`);
CREATE INDEX `IDX_FOCUS` ON `PercolationStep` (`focus`);
CREATE INDEX `IDX_NIMBUS` ON `PercolationStep` (`nimbus`);

CREATE INDEX `IDX_ROWNAME` ON `Row` (`rowname`);

CREATE INDEX `IDX_TOURL` ON `_org_makumba_devel_relations_relation` (`toURL`);
CREATE INDEX `IDX_TYPE` ON `_org_makumba_devel_relations_relation` (`type`);