
CREATE TABLE `TAccount` (
`id` bigint NOT NULL AUTO_INCREMENT,
`accountID` bigint NOT NULL,
`pid` bigint DEFAULT NULL,
`code` varchar(20) DEFAULT NULL,
`name` varchar(255) DEFAULT NULL,
`accountClass` varchar(1) DEFAULT NULL,
`accountType` varchar(20) DEFAULT NULL,
PRIMARY KEY (`id`),
CONSTRAINT `FK_TAccount_Parent` FOREIGN KEY (`pid`) REFERENCES `TAccount`
(`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3
STATS_SAMPLE_PAGES=256;

CREATE TABLE `TAccountTags` (
`id` bigint NOT NULL AUTO_INCREMENT,
`name` varchar(255) DEFAULT NULL,
`color` varchar(20) DEFAULT NULL,
`type` varchar(50) DEFAULT NULL,
`year` smallint DEFAULT NULL,
`isSystemLabel` bit(1) DEFAULT NULL,
`code` varchar(100) DEFAULT NULL,
`item_order` int DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `TAccountTag` (
`tagID` bigint NOT NULL,
`accountID` bigint NOT NULL,
PRIMARY KEY (`tAccountID`,`tagID`),
CONSTRAINT `FK_TAccountTag_TAccount` FOREIGN KEY (`accountID`)
REFERENCES `TAccount` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
CONSTRAINT `FK_TAccountTag_TAccountTags` FOREIGN KEY (`tagID`)
REFERENCES `TAccountTags` (`id`) ON DELETE RESTRICT ON UPDATE
RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 STATS_SAMPLE_PAGES=128;

CREATE TABLE TEntry (
    id BIGINT NOT NULL AUTO_INCREMENT,
    amount DECIMAL(20, 2)  NOT NULL,
    gstAmount DECIMAL(20, 2)  NOT NULL,
    entryType VARCHAR(30) NOT NULL,  -- To store the type of entry (e.g., BasicBankEntry, Investment, etc.)
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;

CREATE TABLE TBasicBankEntry (
    id BIGINT  NOT NULL PRIMARY KEY,
    field1 VARCHAR(255),
    field2 DECIMAL(20, 2),
    CONSTRAINT `FK_TBasicBankEntry_TEntry` FOREIGN KEY (id) REFERENCES TEntry(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE TInvestmentEntry (
    id BIGINT  NOT NULL  PRIMARY KEY,
    field1 VARCHAR(255),
    field2 DECIMAL(20, 2),
    CONSTRAINT `FK_TInvestmentEntry_TEntry` FOREIGN KEY (id) REFERENCES TEntry(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE TDividendEntry (
    id BIGINT  NOT NULL  PRIMARY KEY,
    field1 VARCHAR(255),
    field2 DECIMAL(20, 2),
    CONSTRAINT `FK_TDividendEntry_TEntry` FOREIGN KEY (id) REFERENCES TEntry(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE TDistributionInterestEntry (
    id BIGINT  NOT NULL  PRIMARY KEY,
    field1 VARCHAR(255),
    field2 DECIMAL(20, 2),
    CONSTRAINT `FK_TDistributionInterestEntry_TEntry` FOREIGN KEY (id) REFERENCES TEntry(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE TContributionEntry (
    id BIGINT  NOT NULL  PRIMARY KEY,
    field1 VARCHAR(255),
    field2 DECIMAL(20, 2),
    CONSTRAINT `FK_TContributionEntry_TEntry` FOREIGN KEY (id) REFERENCES TEntry(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `TEntryTransaction` (
`id` bigint NOT NULL AUTO_INCREMENT,
`acctId` bigint DEFAULT NULL,
`entryId` bigint NOT NULL,
`type` varchar(30) NOT NULL,
`amount` decimal(20,2) NOT NULL,
`transactionDate` date NOT NULL,
`fundId` varchar(32) NOT NULL,
`date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
`last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON
UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (`id`),
CONSTRAINT `FK_TEntryTransaction_Acct` FOREIGN KEY (acctId) REFERENCES TAccount(id),
CONSTRAINT `FK_TEntryTransaction_Entry` FOREIGN KEY (entryId) REFERENCES TEntry(id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;