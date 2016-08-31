/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adrian Simionescu
 */
public final class MySQLConfig {
    final public static String
        DRIVER = "com.mysql.jdbc.Driver",
        DBURL  = "jdbc:mysql://localhost/VABUDGET",
        USER = "vadry",
        PASS = "12wq!@WQ",
            
        PREFIX = "vab_",

        CARDS_TABLE = "cards",
        CARDS_CARD_ID = "card_id",
        CARDS_OWNER_ID = "owner_id",
        CARDS_LABEL = "label",
        CARDS_AMOUNT = "amount",

        TRANSACTIONS_TABLE = "transactions",
        TRANSACTIONS_TRANSACTION_ID = "transaction_id",
        TRANSACTIONS_CARD_ID = "card_id",
        TRANSACTIONS_USER_ID = "owner_id",
        TRANSACTIONS_DESCRIPTION = "label",
        TRANSACTIONS_AMOUNT = "amount",
        TRANSACTIONS_DATE = "datetime",
        TRANSACTIONS_TYPE = "type",

        SHARED_TABLE = "shared",
        SHARED_CARD_ID = "card_id",
        SHARED_PERSON_ID = "person_id",
        SHARED_STATUS = "status",

        DISTRIBUTIONS_TABLE = "distributions",
        DISTRIBUTIONS_DISTRIB_ID = "distrib_id",
        DISTRIBUTIONS_OWNER_ID = "owner_id",
        DISTRIBUTIONS_LABEL = "label",

        DISTRIBUTIONS_RATIO_TABLE = "distributions_ratio",
        DISTRIBUTIONS_RATIO_DISTRIB_ID = "distrib_id",
        DISTRIBUTIONS_RATIO_CARD_ID = "card_id",
        DISTRIBUTIONS_RATIO_PERCENT = "percent",

        DISTRIBUTIONS_SHARED_TABLE = "distributions_shared",
        DISTRIBUTIONS_SHARED_DISTRIB_ID = "distrib_id",
        DISTRIBUTIONS_SHARED_PERSON_ID = "person_id",
        DISTRIBUTIONS_SHARED_STATUS = "status";
    
    public static List<String> createStmt() {
        List<String> sql = new ArrayList<>();
        sql.add("CREATE TABLE IF NOT EXISTS " + PREFIX + MySQLConfig.CARDS_TABLE +
                   "(" + MySQLConfig.CARDS_CARD_ID + " INTEGER not NULL AUTO_INCREMENT, " +
                   " " + MySQLConfig.CARDS_OWNER_ID + " INTEGER not NULL, " + 
                   " " + MySQLConfig.CARDS_LABEL + " VARCHAR(255), " + 
                   " " + MySQLConfig.CARDS_AMOUNT + " DECIMAL(8,2), " + 
                   " PRIMARY KEY ( " + MySQLConfig.CARDS_CARD_ID + " ))");
            
        sql.add("CREATE TABLE IF NOT EXISTS " + PREFIX + MySQLConfig.TRANSACTIONS_TABLE +
                   "(" + MySQLConfig.TRANSACTIONS_TRANSACTION_ID + " INTEGER not NULL AUTO_INCREMENT, " +
                   " " + MySQLConfig.TRANSACTIONS_CARD_ID + " INTEGER not NULL, " + 
                   " " + MySQLConfig.TRANSACTIONS_USER_ID + " INTEGER not NULL, " + 
                   " " + MySQLConfig.TRANSACTIONS_DESCRIPTION + " VARCHAR(255), " + 
                   " " + MySQLConfig.TRANSACTIONS_TYPE + " VARCHAR(50), " + 
                   " " + MySQLConfig.TRANSACTIONS_DATE + " DATETIME, " + 
                   " " + MySQLConfig.TRANSACTIONS_AMOUNT + " DECIMAL(8,2), " + 
                   " PRIMARY KEY ( " + MySQLConfig.TRANSACTIONS_TRANSACTION_ID + " ))");
            
        sql.add("CREATE TABLE IF NOT EXISTS " + PREFIX + MySQLConfig.SHARED_TABLE +
                   "(" + MySQLConfig.SHARED_CARD_ID + " INTEGER not NULL, " +
                   " " + MySQLConfig.SHARED_PERSON_ID + " INTEGER not NULL, " + 
                   " " + MySQLConfig.SHARED_STATUS + " ENUM('-1','0','1'), " + 
                   " PRIMARY KEY ( " + MySQLConfig.SHARED_CARD_ID + ", " + MySQLConfig.SHARED_PERSON_ID + " ))");
            
        sql.add("CREATE TABLE IF NOT EXISTS " + PREFIX + MySQLConfig.DISTRIBUTIONS_TABLE +
                   "(" + MySQLConfig.DISTRIBUTIONS_DISTRIB_ID + " INTEGER not NULL AUTO_INCREMENT, " +
                   " " + MySQLConfig.DISTRIBUTIONS_OWNER_ID + " INTEGER not NULL, " + 
                   " " + MySQLConfig.DISTRIBUTIONS_LABEL + " VARCHAR(50), " + 
                   " PRIMARY KEY ( " + MySQLConfig.DISTRIBUTIONS_DISTRIB_ID + " ))");
            
        sql.add("CREATE TABLE IF NOT EXISTS " + PREFIX + MySQLConfig.DISTRIBUTIONS_RATIO_TABLE +
                   "(" + MySQLConfig.DISTRIBUTIONS_RATIO_DISTRIB_ID + " INTEGER not NULL, " +
                   " " + MySQLConfig.DISTRIBUTIONS_RATIO_CARD_ID + " INTEGER not NULL, " + 
                   " " + MySQLConfig.DISTRIBUTIONS_RATIO_PERCENT + " INTEGER not NULL, " + 
                   " PRIMARY KEY ( " + MySQLConfig.DISTRIBUTIONS_RATIO_DISTRIB_ID + ", " + MySQLConfig.DISTRIBUTIONS_RATIO_CARD_ID + " ))");
            
        sql.add("CREATE TABLE IF NOT EXISTS " + PREFIX + MySQLConfig.DISTRIBUTIONS_SHARED_TABLE +
                   "(" + MySQLConfig.DISTRIBUTIONS_SHARED_DISTRIB_ID + " INTEGER not NULL, " +
                   " " + MySQLConfig.DISTRIBUTIONS_SHARED_PERSON_ID + " INTEGER not NULL, " + 
                   " " + MySQLConfig.DISTRIBUTIONS_SHARED_STATUS + " ENUM('-1','0','1'), " + 
                   " PRIMARY KEY ( " + MySQLConfig.DISTRIBUTIONS_SHARED_DISTRIB_ID + ", " + MySQLConfig.DISTRIBUTIONS_SHARED_PERSON_ID + " ))");
        
        return sql;
    }
    
    // Prevent create instance
    private MySQLConfig() {
        
    }
}
