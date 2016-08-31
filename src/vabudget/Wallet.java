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
    public boolean connectWith(DAOFactory model);
    

    /**
     *
     * @param ownerId
     * @param name
     * @return
     */
    public int addCard(int ownerId, String name);

    public int addCard(int ownerId, String name, BigDecimal initAmount);

    public boolean removeCard(int cardId);

    public Card getCard(int cardId);

    public List<Card> getCards(int userId);
    
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
    
    public boolean incomeDistrib(int distribId, int userId, BigDecimal amount, String description, Date datetime);
    
    public boolean expense(int cardId, int userId, BigDecimal amount, String description, Date datetime);

    public List<Transaction> history(int cardId);
    
    /**
     * Share account with another person.
     * @param personId
     */
    public boolean shareWith(int cardId, int personId);

    public boolean removeShare(int cardId, int personId);
    
    public List<Share> getShared(int cardId);
    
    public boolean sharedAccept(int cardId, int personId);
    
    public boolean sharedReject(int cardId, int personId);
    
    /**
     * 
     * @param ratio
     * @return
     */
    public int addDistribution(int ownerId, String label, Map<Integer, Integer> ratio);
    
    public boolean removeDistribution(int distribId);
    
    public Distribution getDistribution(int distribId);
    
    public List<Distribution> getDistributions(int personId);
    
    
    public boolean shareDistributionWith(int distribId, int personId);
    
    public boolean removeShareDistribution(int distribId, int personId);
    
    public boolean sharedDistributionAccept(int distribId, int personId);
    
    public boolean sharedDistributionReject(int distribId, int personId);
    
}
