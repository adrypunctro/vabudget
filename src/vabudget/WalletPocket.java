/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import static vabudget.DebitCard.model;

/**
 *
 * @author Adrian Simionescu
 */
public class WalletPocket implements Wallet {

    private Model model;
    final private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public boolean connectWith(Model model) {
        this.model = model;
        
        DebitCard.model = model;
        
        model.connect();
        model.create();
        
        return true;
    }
    

    @Override
    public int addCard(int ownerId, String label) {
        Map<String, String> vals = new HashMap<>();
        vals.put(Model.CARDS_OWNER_ID, String.valueOf(ownerId));
        vals.put(Model.CARDS_LABEL, label);
        vals.put(Model.CARDS_AMOUNT, "0");
        int cardId = model.insertWithReturn(Model.CARDS_TABLE, vals);
        if (cardId > 0) {
            return cardId;
        }
        return 0;
    }
    
    @Override
    public int addCard(int ownerId, String label, String balance) {
        Map<String, String> vals = new HashMap<>();
        vals.put(Model.CARDS_OWNER_ID, String.valueOf(ownerId));
        vals.put(Model.CARDS_LABEL, label);
        vals.put(Model.CARDS_AMOUNT, balance);
        int cardId = model.insertWithReturn(Model.CARDS_TABLE, vals);
        if (cardId > 0) {
            return cardId;
        }
        return 0;
    }
    
    @Override
    public boolean removeCard(int cardId) {
        Map<String, String> where = new HashMap<>();
        where.put(Model.CARDS_CARD_ID, String.valueOf(cardId));
        return model.delete(Model.CARDS_TABLE, where);
    }

    @Override
    public Card getCard(int cardId) {
        try {
            Map<String, String> where = new HashMap<>();
            where.put(Model.CARDS_CARD_ID, String.valueOf(cardId));
            Map<String, Object> item = model.selectOne(Model.CARDS_TABLE, where);
            return new DebitCard(item);
        } catch (Exception ex) {
            Logger.getLogger(WalletPocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Card> getCards() {
        try {
            List<Card> results = new LinkedList<>();
            List<HashMap<String, Object>> rows = model.select(Model.CARDS_TABLE, new HashMap<>());
            for(HashMap<String, Object> row : rows) {
                int cardId = (int)row.get(Model.CARDS_CARD_ID);
                results.add(getCard(cardId));
            }
            return results;
        } catch (Exception ex) {
            Logger.getLogger(WalletPocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean income(int cardId, int userId, BigDecimal new_amount, String description, Date datetime) {
        try {
            Map<String, String> sets = new HashMap<>();
            sets.put(Model.CARDS_AMOUNT, Model.CARDS_AMOUNT+"+"+new_amount);
            Map<String, String> where = new HashMap<>();
            where.put(Model.CARDS_CARD_ID, String.valueOf(cardId));
            model.update(Model.CARDS_TABLE, where, new HashMap<>(), sets);

            Map<String, String> vals = new HashMap<>();
            vals.put(Model.TRANSACTIONS_CARD_ID, String.valueOf(cardId));
            vals.put(Model.TRANSACTIONS_USER_ID, String.valueOf(userId));
            vals.put(Model.TRANSACTIONS_DESCRIPTION, description);
            vals.put(Model.TRANSACTIONS_AMOUNT, new_amount.toPlainString());
            vals.put(Model.TRANSACTIONS_DATE, dateFormat.format(datetime));
            vals.put(Model.TRANSACTIONS_TYPE, TransactionType.Income.name());
            model.insert(Model.TRANSACTIONS_TABLE, vals);
            
            return true;
        } catch (Exception ex) {
            Logger.getLogger(WalletPocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean expense(int cardId, int userId, BigDecimal new_amount, String description, Date datetime) {
        try {
            Map<String, String> sets = new HashMap<>();
            sets.put(Model.CARDS_AMOUNT, Model.CARDS_AMOUNT+"-"+new_amount);
            Map<String, String> where = new HashMap<>();
            where.put(Model.CARDS_CARD_ID, String.valueOf(cardId));
            model.update(Model.CARDS_TABLE, where, new HashMap<>(), sets);

            Map<String, String> vals = new HashMap<>();
            vals.put(Model.TRANSACTIONS_CARD_ID, String.valueOf(cardId));
            vals.put(Model.TRANSACTIONS_USER_ID, String.valueOf(userId));
            vals.put(Model.TRANSACTIONS_DESCRIPTION, description);
            vals.put(Model.TRANSACTIONS_AMOUNT, new_amount.toPlainString());
            vals.put(Model.TRANSACTIONS_DATE, dateFormat.format(datetime));
            vals.put(Model.TRANSACTIONS_TYPE, TransactionType.Expense.name());
            model.insert(Model.TRANSACTIONS_TABLE, vals);
            
            return true;
        } catch (Exception ex) {
            Logger.getLogger(WalletPocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<Transaction> history(int cardId) {
        try {
            List<Transaction> results = new ArrayList<>();
            Map<String, String> where1 = new HashMap<>();
            where1.put(Model.TRANSACTIONS_CARD_ID, String.valueOf(cardId));
            List<HashMap<String, Object>> rows = model.select(Model.TRANSACTIONS_TABLE, where1);
            for(HashMap<String, Object> row : rows) {
                int transId = (int)row.get(Model.CARDS_CARD_ID);
                Map<String, String> where = new HashMap<>();
                where.put(Model.TRANSACTIONS_TRANSACTION_ID, String.valueOf(transId));
                Map<String, Object> item = model.selectOne(Model.TRANSACTIONS_TABLE, where);
                try {
                    results.add(new Transaction(
                            new BigDecimal(String.valueOf(item.get(Model.TRANSACTIONS_AMOUNT))),
                            (String)item.get(Model.TRANSACTIONS_DESCRIPTION),
                            dateFormat.parse(item.get(Model.TRANSACTIONS_DATE).toString()),
                            TransactionType.valueOf((String)item.get(Model.TRANSACTIONS_TYPE))
                        )
                    );
                } catch (ParseException ex) {
                    Logger.getLogger(DebitCard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return results;
        } catch (Exception ex) {
            Logger.getLogger(WalletPocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public List<Transaction> history(int cardId, int userId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Transaction> history(int cardId, Date begin, Date end) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Transaction> history(int cardId, int userId, Date begin, Date end) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean shareWith(int cardId, int personId) {
        Map<Integer,String> shared = getShared(cardId);
        if (shared.keySet().contains(personId)) {
            return false;
        }
        
        Map<String, String> vals = new HashMap<>();
        vals.put(Model.SHARED_CARD_ID, String.valueOf(cardId));
        vals.put(Model.SHARED_PERSON_ID, String.valueOf(personId));
        vals.put(Model.SHARED_STATUS, "0");
        
        return model.insert(Model.SHARED_TABLE, vals);
    }

    @Override
    public boolean removeShare(int cardId, int personId) {
        Map<String, String> where = new HashMap<>();
        where.put(Model.SHARED_CARD_ID, String.valueOf(cardId));
        where.put(Model.SHARED_PERSON_ID, String.valueOf(personId));
        return model.delete(Model.SHARED_TABLE, where);
    }

    @Override
    public Map<Integer,String> getShared(int cardId) {
        try {
            Map<Integer,String> results = new HashMap<>();
            Map<String, String> where = new HashMap<>();
            where.put(Model.SHARED_CARD_ID, String.valueOf(cardId));
            List<HashMap<String, Object>> rows = model.select(Model.SHARED_TABLE, where);
            for(HashMap<String, Object> row : rows) {
                results.put(
                    (int)row.get(Model.SHARED_PERSON_ID),
                    (String)row.get(Model.SHARED_STATUS)
                );
            }
            return results;
        } catch (Exception ex) {
            Logger.getLogger(WalletPocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean sharedAccept(int cardId, int personId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean sharedReject(int cardId, int personId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addIncomeDistribution(Map<Integer, Integer> ratio, String label) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeIncomeDistribution(int distribId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<Integer, Integer> getIncomeDistribution(int distribId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Integer> shareIncomeDistribution(int distribId, int personId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeShareIncomeDistribution(int distribId, int personId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean sharedIncomeDistributionAccept(int distribId, int personId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean sharedIncomeDistributionReject(int distribId, int personId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
