CREATE TABLE `offer_has_negative` (
  `TO_ID` bigint(20) NOT NULL,
  `TU_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`TO_ID`,`TU_ID`),
  KEY `FK_gbya7duw9vacmaibupib0dfeb` (`TU_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `offer_has_negative` WRITE;
/*!40000 ALTER TABLE `offer_has_negative` DISABLE KEYS */;
/*!40000 ALTER TABLE `offer_has_negative` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `offer_has_positive` (
  `TO_ID` bigint(20) NOT NULL,
  `TU_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`TO_ID`,`TU_ID`),
  KEY `FK_hdhhm9c8lx8yl7yux3uu0y62h` (`TU_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `offer_has_positive` WRITE;
/*!40000 ALTER TABLE `offer_has_positive` DISABLE KEYS */;
/*!40000 ALTER TABLE `offer_has_positive` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `thatcomment` (
  `TCM_ID` bigint(20) NOT NULL auto_increment,
  `TCM_CREATION_DATE` datetime NOT NULL,
  `TCM_TEXT` longtext NOT NULL,
  `TCM_QUOTED_COMMENT` bigint(20) default NULL,
  `TCM_VERSION_ID` bigint(20) default NULL,
  `TCM_USER` bigint(20) NOT NULL,
  `TCM_OFFER` bigint(20) NOT NULL,
  PRIMARY KEY  (`TCM_ID`),
  KEY `FK_pca10vp0hi163livr2eo72iq4` (`TCM_USER`),
  KEY `FK_nxb8j7ts03mnvfco4h5qdahhn` (`TCM_OFFER`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `thatcomment` WRITE;
/*!40000 ALTER TABLE `thatcomment` DISABLE KEYS */;
/*!40000 ALTER TABLE `thatcomment` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `thatcompany` (
  `TC_ID` bigint(20) NOT NULL auto_increment,
  `TC_AFFILIATE_ID` varchar(255) default NULL,
  `TC_LOGO_URL` varchar(255) default NULL,
  `TC_NAME` varchar(255) NOT NULL,
  `TC_URL` varchar(255) default NULL,
  `TC_WATERMARKS` varchar(255) default NULL,
  `TC_VERSION_ID` bigint(20) default NULL,
  PRIMARY KEY  (`TC_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `thatcompany` WRITE;
/*!40000 ALTER TABLE `thatcompany` DISABLE KEYS */;
/*!40000 ALTER TABLE `thatcompany` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `thatoffer` (
  `TO_ID` bigint(20) NOT NULL auto_increment,
  `TO_FINAL_PRICE` decimal(19,2) NOT NULL,
  `TO_CATEGORY` varchar(255) NOT NULL,
  `TO_CREATED_DATE` datetime default NULL,
  `TO_DESCRIPTION` longtext NOT NULL,
  `TO_EXPIRED` bit(1) NOT NULL,
  `TO_IMAGE` varchar(255) NOT NULL,
  `TO_LINK` longtext NOT NULL,
  `TO_NEGATIVE` bigint(20) default NULL,
  `TO_POSITIVE` bigint(20) default NULL,
  `TO_TITLE` varchar(255) NOT NULL,
  `TO_ORIGINAL_PRICE` decimal(19,2) default NULL,
  `TO_VERSION_ID` bigint(20) default NULL,
  `TO_COMPANY` bigint(20) NOT NULL,
  `TO_USER` bigint(20) NOT NULL,
  PRIMARY KEY  (`TO_ID`),
  KEY `FK_pwxf1riagtphtplh6oe5dq77p` (`TO_COMPANY`),
  KEY `FK_5ss85bp8f40mxwrvxbgmbjjoo` (`TO_USER`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `thatoffer` WRITE;
/*!40000 ALTER TABLE `thatoffer` DISABLE KEYS */;
/*!40000 ALTER TABLE `thatoffer` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `thattemplate` (
  `EMT_ID` bigint(20) NOT NULL auto_increment,
  `EMT_CONTENT` longtext,
  `EMT_NAME` varchar(255) NOT NULL,
  `EMT_SUBJECT` varchar(255) NOT NULL,
  PRIMARY KEY  (`EMT_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `thattemplate` WRITE;
/*!40000 ALTER TABLE `thattemplate` DISABLE KEYS */;
/*!40000 ALTER TABLE `thattemplate` ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `thatuser` (
  `TU_ID` bigint(20) NOT NULL auto_increment,
  `TU_ENABLED` bit(1) NOT NULL,
  `TU_PENDING` bit(1) NOT NULL,
  `TU_AVATAR` varchar(255) default NULL,
  `TU_EMAIL` varchar(255) NOT NULL,
  `TU_NICKNAME` varchar(255) NOT NULL,
  `TU_PWD_LOCKED` varchar(60) NOT NULL,
  `TU_UUID` varchar(255) NOT NULL,
  `TU_VERSION_ID` bigint(20) default NULL,
  PRIMARY KEY  (`TU_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `thatuser` WRITE;
/*!40000 ALTER TABLE `thatuser` DISABLE KEYS */;
/*!40000 ALTER TABLE `thatuser` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `thatuserroles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `thatuserroles` (
  `TUR_ID` bigint(20) NOT NULL auto_increment,
  `TUR_NICKNAME` varchar(255) NOT NULL,
  `TUR_ROLE` varchar(255) NOT NULL,
  `TUR_VERSION_ID` bigint(20) default NULL,
  PRIMARY KEY  (`TUR_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `thatuserroles` WRITE;
/*!40000 ALTER TABLE `thatuserroles` DISABLE KEYS */;
/*!40000 ALTER TABLE `thatuserroles` ENABLE KEYS */;
UNLOCK TABLES;
