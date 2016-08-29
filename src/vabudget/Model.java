/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Adrian Simionescu
 */
public interface Model {
    final public static String
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
            
            DISTRIBUTIONS_RATIO_TABLE = "distributions_ratio",
            DISTRIBUTIONS_RATIO_DISTRIB_ID = "distrib_id",
            DISTRIBUTIONS_RATIO_CARD_ID = "card_id",
            DISTRIBUTIONS_RATIO_PERCENT = "percent",
            
            DISTRIBUTIONS_SHARED_TABLE = "distributions_shared",
            DISTRIBUTIONS_SHARED_DISTRIB_ID = "distrib_id",
            DISTRIBUTIONS_SHARED_PERSON_ID = "person_id",
            DISTRIBUTIONS_SHARED_STATUS = "status";
    
    public boolean connect();
    public boolean create();

    public boolean insert(String table, Map<String, String> sets);
    public int insertWithReturn(String table, Map<String, String> sets);
    public boolean delete(String table, Map<String, String> where);
    public boolean update(String table, Map<String, String> where, Map<String, String> sets, Map<String, String> setsExp) throws Exception;
    public List<HashMap<String,Object>> select(String table, Map<String, String> where) throws Exception;
    public Map<String,Object> selectOne(String table, Map<String, String> where) throws Exception;
}
