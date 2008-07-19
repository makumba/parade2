-- MySQL dump 10.11
--
-- Host: localhost    Database: parade
-- ------------------------------------------------------
-- Server version	5.0.51a-3ubuntu5.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `InitialPercolationRule`
--

DROP TABLE IF EXISTS `InitialPercolationRule`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `InitialPercolationRule` (
  `initialpercolationrule` bigint(20) NOT NULL auto_increment,
  `objectType` varchar(255) default NULL,
  `action` varchar(255) default NULL,
  `userType` varchar(255) default NULL,
  `initialLevel` int(11) default NULL,
  `percolationMode` int(11) default NULL,
  `active` bit(1) default NULL,
  `focusProgressionCurve` varchar(255) default NULL,
  `nimbusProgressionCurve` varchar(255) default NULL,
  `interactionType` varchar(255) default NULL,
  PRIMARY KEY  (`initialpercolationrule`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `InitialPercolationRule`
--

LOCK TABLES `InitialPercolationRule` WRITE;
/*!40000 ALTER TABLE `InitialPercolationRule` DISABLE KEYS */;
INSERT INTO `InitialPercolationRule` VALUES (1,'FILE','save','all_but_actor',100,30,'','1-ln(t/10+1)','1-t*t+t','20'),(2,'DIR','view','all_but_actor',50,30,'','1-t/2','1-t/2','20');
/*!40000 ALTER TABLE `InitialPercolationRule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `InitialPercolationRule__relationQueries`
--

DROP TABLE IF EXISTS `InitialPercolationRule__relationQueries`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `InitialPercolationRule__relationQueries` (
  `id_initialpercolationrule` bigint(20) NOT NULL,
  `elt` bigint(20) NOT NULL,
  KEY `FK91634F409CE9A215` (`elt`),
  KEY `FK91634F408FE3CD7C` (`id_initialpercolationrule`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `InitialPercolationRule__relationQueries`
--

LOCK TABLES `InitialPercolationRule__relationQueries` WRITE;
/*!40000 ALTER TABLE `InitialPercolationRule__relationQueries` DISABLE KEYS */;
INSERT INTO `InitialPercolationRule__relationQueries` VALUES (1,5),(1,1);
/*!40000 ALTER TABLE `InitialPercolationRule__relationQueries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PercolationRule`
--

DROP TABLE IF EXISTS `PercolationRule`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `PercolationRule` (
  `percolationrule` bigint(20) NOT NULL auto_increment,
  `subject` varchar(255) default NULL,
  `predicate` varchar(255) default NULL,
  `object` varchar(255) default NULL,
  `consumption` int(11) default NULL,
  `description` varchar(255) default NULL,
  `active` bit(1) default NULL,
  PRIMARY KEY  (`percolationrule`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `PercolationRule`
--

LOCK TABLES `PercolationRule` WRITE;
/*!40000 ALTER TABLE `PercolationRule` DISABLE KEYS */;
INSERT INTO `PercolationRule` VALUES (1,'FILE','versionOf','CVSFILE',5,'is a version of',''),(2,'FILE','dependsOn','FILE',20,'depends on the file that was acted upon',''),(3,'USER','save','FILE',0,'User watched file',''),(4,'CVSFILE','dependsOn','CVSFILE',20,'A cvs file depends on another cvs file','\0'),(5,'CVSFILE','checkedOutAs','FILE',5,'is checked out as','');
/*!40000 ALTER TABLE `PercolationRule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PercolationRule__relationQueries`
--

DROP TABLE IF EXISTS `PercolationRule__relationQueries`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `PercolationRule__relationQueries` (
  `id_initialpercolationrule` bigint(20) NOT NULL,
  `elt` bigint(20) NOT NULL,
  KEY `FK432EF01C9CE9A215` (`elt`),
  KEY `FK432EF01C6E36F558` (`id_initialpercolationrule`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `PercolationRule__relationQueries`
--

LOCK TABLES `PercolationRule__relationQueries` WRITE;
/*!40000 ALTER TABLE `PercolationRule__relationQueries` DISABLE KEYS */;
INSERT INTO `PercolationRule__relationQueries` VALUES (2,5),(2,1),(5,4),(4,5),(4,4),(1,5);
/*!40000 ALTER TABLE `PercolationRule__relationQueries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RelationQuery`
--

DROP TABLE IF EXISTS `RelationQuery`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `RelationQuery` (
  `relationquery` bigint(20) NOT NULL auto_increment,
  `query` text NOT NULL,
  `description` text,
  `arguments` varchar(255) default NULL,
  PRIMARY KEY  (`relationquery`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `RelationQuery`
--

LOCK TABLES `RelationQuery` WRITE;
/*!40000 ALTER TABLE `RelationQuery` DISABLE KEYS */;
INSERT INTO `RelationQuery` VALUES (1,'SELECT f.cvsURL as fromURL, \'checkedOutAs\' as type, concat(\'file://\', concat(r.rowname, substring(f.path, length(r.rowpath) + 1 + length(r.webappPath) + case when length(r.webappPath) = 0 then 0 else 1 end, length(f.path)))) as toURL FROM File f JOIN f.row r WHERE concat(\'file://\', concat(r.rowname, substring(f.path, length(r.rowpath) + 1 + length(r.webappPath) + case when length(r.webappPath) = 0 then 0 else 1 end, length(f.path)))) in(:fromURLSet) AND f.cvsURL is not null and concat(concat(\'file://\', concat(r.rowname, substring(f.path, length(r.rowpath) + 1 + length(r.webappPath) + case when length(r.webappPath) = 0 then 0 else 1 end, length(f.path)))), \'True\') not in(:fromURLAndTraversedCVSSet)','cvs:// , checkedOutAs, file:// - all the cvs files that are a version of a repository version (not-null CVS version) and where the percolationPath has no cvs:// prefix','fromURLSet, fromURLAndTraversedCVSSet'),(4,'SELECT concat(\'file://\', concat(r.rowname, substring(f.path, length(r.rowpath) + 1 + length(r.webappPath) + case when length(r.webappPath) = 0 then 0 else 1 end, length(f.path)))) as fromURL, \'versionOf\' as type, f.cvsURL as toURL FROM File f JOIN f.row r WHERE f.cvsURL in(:cvsURLSet) AND r.rowname not in(:rowNameSet)','file://, versionOf, cvs:// - all the checked out files of a cvs file that have a CVS URL set except the ones of the previous row','cvsURLSet, rowNameSet'),(5,'SELECT r.fromURL as fromURL, r.type as type, r.toURL as toURL from org.makumba.devel.relations.Relation r where r.toURL in(:fromURLSet)','fromURL, type, toURL - all the files that depend on this file (through the computed relations)','fromURLSet');
/*!40000 ALTER TABLE `RelationQuery` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2008-07-19 15:35:13
