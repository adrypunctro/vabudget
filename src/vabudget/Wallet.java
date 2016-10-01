/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Adrian Simionescu
 */
public abstract class Wallet
{
    protected DAOFactory model=null;
    
    public abstract boolean connected();
    
    public boolean reconnect()
    {
        //@todo: fix this
        return false;
    }
    
    /**
     *
     * @param ownerId
     * @param name
     * @return
     */
    public int addCard(int ownerId, String label)
    {
        CardDAO cardDAO = model.getCardDAO();
        return cardDAO.insertCard(ownerId, label, new BigDecimal("0.00"));
    }
    
    public int addCard(int ownerId, String label, BigDecimal initAmount)
    {
        CardDAO cardDAO = model.getCardDAO();
        return cardDAO.insertCard(ownerId, label, initAmount);
    }
    
    public boolean removeCard(int cardId)
    {
        CardDAO cardDAO = model.getCardDAO();
        ShareDAO shareDAO = model.getShareDAO();
        for (Share share : shareDAO.selectShared(cardId))
        {
            shareDAO.deleteShare(cardId, share.getPersonId());
        }
        return cardDAO.deleteCard(cardId);
    }

    public Card getCard(int cardId)
    {
        CardDAO cardDAO = model.getCardDAO();
        ShareDAO shareDAO = model.getShareDAO();
        Card card = cardDAO.findCard(cardId);
        for (Share share : shareDAO.selectShared(card.getId()))
        {
            card.addShare(share);
        }
        return card;
    }

    public List<Card> getCards(int userId)
    {
        CardDAO cardDAO = model.getCardDAO();
        ShareDAO shareDAO = model.getShareDAO();
        List<Card> cards = cardDAO.selectCards(userId);
        for (Card card : cards)
        {
            for (Share share : shareDAO.selectShared(card.getId()))
            {
                card.addShare(share);
            }
        }
        return cards;
    }

    public boolean income(int cardId, int userId, BigDecimal amount, String description, Date datetime)
    {
        TransactionDAO transDAO = model.getTransactionDAO();
        return transDAO.insertIncome(userId, cardId, description, amount, datetime, TransactionType.Income.name());
    }

    public boolean incomeDistrib(int distribId, int userId, BigDecimal amount, String description, Date datetime)
    {
        DistributionDAO distribDAO = model.getDistributionDAO();
        TransactionDAO transDAO = model.getTransactionDAO();
        
        List<Transaction> transactions = new ArrayList<>();
        Distribution distrib = distribDAO.findDistribution(distribId);
        for(int cardId : distrib.getRatio().keySet())
        {
            int percent = distrib.getRatio().get(cardId);
            BigDecimal incomeValue = new BigDecimal(amount.floatValue()*(percent/100.0f));
            transactions.add(
                new Transaction(0, cardId, userId, description+ " Distrib: "+distrib.getLabel(), incomeValue, datetime, TransactionType.Income)
            );
        }
        
        return transDAO.insertMultipleIncome(transactions);
    }
    
    public boolean expense(int cardId, int userId, BigDecimal amount, String description, Date datetime)
    {
        TransactionDAO transDAO = model.getTransactionDAO();
        return transDAO.insertExpense(userId, cardId, description, amount, datetime, TransactionType.Expense.name());
    }
    
    public List<Transaction> history(int cardId)
    {
        TransactionDAO transDAO = model.getTransactionDAO();
        return transDAO.selectTransaction(cardId);
    }

    public boolean shareWith(int cardId, int personId)
    {
        ShareDAO shareDAO = model.getShareDAO();
        return shareDAO.insertShare(cardId, personId);
    }

    public boolean removeShare(int cardId, int personId)
    {
        ShareDAO shareDAO = model.getShareDAO();
        return shareDAO.deleteShare(cardId, personId);
    }

    public List<Share> getShared(int cardId)
    {
        ShareDAO shareDAO = model.getShareDAO();
        return shareDAO.selectShared(cardId);
    }

    public boolean sharedAccept(int cardId, int personId)
    {
        ShareDAO shareDAO = model.getShareDAO();
        return shareDAO.acceptShare(cardId, personId);
    }

    public boolean sharedReject(int cardId, int personId)
    {
        ShareDAO shareDAO = model.getShareDAO();
        return shareDAO.rejectShare(cardId, personId);
    }

    public int addDistribution(int ownerId, String label, Map<Integer, Integer> ratio)
    {
        DistributionDAO distribDAO = model.getDistributionDAO();
        return distribDAO.insertDistribution(ownerId, label, ratio);
    }

    public boolean removeDistribution(int distribId)
    {
        DistributionDAO distribDAO = model.getDistributionDAO();
        return distribDAO.deleteDistribution(distribId);
    }

    public Distribution getDistribution(int distribId)
    {
        DistributionDAO distribDAO = model.getDistributionDAO();
        return distribDAO.findDistribution(distribId);
    }

    public boolean shareDistributionWith(int distribId, int personId)
    {
        return false;// TODO: Not yet!
        /*Map<String, String> vals = new HashMap<>();
        vals.put(MySQLConfig.DISTRIBUTIONS_SHARED_DISTRIB_ID, String.valueOf(distribId));
        vals.put(MySQLConfig.DISTRIBUTIONS_SHARED_PERSON_ID, String.valueOf(personId));
        vals.put(MySQLConfig.DISTRIBUTIONS_SHARED_STATUS, "0");
        
        return model.insert(MySQLConfig.DISTRIBUTIONS_SHARED_TABLE, vals);*/
    }

    public boolean removeShareDistribution(int distribId, int personId)
    {
        return false;// TODO: Not yet!
        /*Map<String, String> where = new HashMap<>();
        where.put(MySQLConfig.DISTRIBUTIONS_SHARED_DISTRIB_ID, String.valueOf(distribId));
        where.put(MySQLConfig.DISTRIBUTIONS_SHARED_PERSON_ID, String.valueOf(personId));
        return model.delete(MySQLConfig.DISTRIBUTIONS_SHARED_TABLE, where);*/
    }

    public boolean sharedDistributionAccept(int distribId, int personId)
    {
        return false;// TODO: Not yet!
        /*try {
            Map<String, String> sets = new HashMap<>();
            sets.put(MySQLConfig.DISTRIBUTIONS_SHARED_STATUS, "1");
            
            Map<String, String> where = new HashMap<>();
            where.put(MySQLConfig.DISTRIBUTIONS_SHARED_DISTRIB_ID, String.valueOf(distribId));
            where.put(MySQLConfig.DISTRIBUTIONS_SHARED_PERSON_ID, String.valueOf(personId));
            
            model.update(MySQLConfig.DISTRIBUTIONS_SHARED_TABLE, where, sets);

            return true;
        } catch (Exception ex) {
            Logger.getLogger(WalletPocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;*/
    }

    public boolean sharedDistributionReject(int distribId, int personId)
    {
        return false;// TODO: Not yet!
        /*try {
            Map<String, String> sets = new HashMap<>();
            sets.put(MySQLConfig.DISTRIBUTIONS_SHARED_STATUS, "-1");
            
            Map<String, String> where = new HashMap<>();
            where.put(MySQLConfig.DISTRIBUTIONS_SHARED_DISTRIB_ID, String.valueOf(distribId));
            where.put(MySQLConfig.DISTRIBUTIONS_SHARED_PERSON_ID, String.valueOf(personId));
            
            model.update(MySQLConfig.DISTRIBUTIONS_SHARED_TABLE, where, sets);

            return true;
        } catch (Exception ex) {
            Logger.getLogger(WalletPocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;*/
    }

    public List<Distribution> getDistributions(int personId)
    {
        DistributionDAO distribDAO = model.getDistributionDAO();
        return distribDAO.selectDistributions(personId);
    }
}
