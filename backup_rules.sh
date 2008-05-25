#!/bin/bash

mysqldump -u root parade InitialPercolationRule InitialPercolationRule__relationQueries PercolationRule PercolationRule__relationQueries RelationQuery > backup_rules.sql
