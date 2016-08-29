/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Adrian Simionescu
 */
public interface Wallet {
    
    /**
     *
     * @param model
     * @param view
     * @return
     */
    public boolean connectWith(Model model);
    

    /**
     *
     * @param ownerId
     * @param name
     * @return
     */
    public int addCard(int ownerId, String name);

    public int addCard(int ownerId, String name, String balance);

    public boolean removeCard(int cardId);

    public Card getCard(int cardId);

    public List<Card> getCards();
    
    /**
     *
     * @param cardId
     * @param userId
     * @param amount
     * @param description
     * @param datetime
     * @return
     */
    public boolean income(int cardId, int userId, BigDecimal amount, String description, Date datetime);
    
    public boolean expense(int cardId, int userId, BigDecimal amount, String description, Date datetime);

    public List<Transaction> history(int cardId);

    public List<Transaction> history(int cardId, int userId);
    
    public List<Transaction> history(int cardId, Date begin, Date end);
    
    public List<Transaction> history(int cardId, int userId, Date begin, Date end);
    
    /**
     * Share account with another person.
     * @param personId
     */
    public boolean shareWith(int cardId, int personId);

    public boolean removeShare(int cardId, int personId);
    
    public Map<Integer,String> getShared(int cardId);
    
    public boolean sharedAccept(int cardId, int personId);
    
    public boolean sharedReject(int cardId, int personId);
    
    /**
     * 
     * @param ratio
     * @return
     */
    public boolean addIncomeDistribution(Map<Integer, Integer> ratio, String label);
    
    public boolean removeIncomeDistribution(int distribId);
    
    public Map<Integer, Integer> getIncomeDistribution(int distribId);
    
    public Set<Integer> shareIncomeDistribution(int distribId, int personId);
    
    public boolean removeShareIncomeDistribution(int distribId, int personId);
    
    public boolean sharedIncomeDistributionAccept(int distribId, int personId);
    
    public boolean sharedIncomeDistributionReject(int distribId, int personId);
    
}
