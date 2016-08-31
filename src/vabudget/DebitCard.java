/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

import config.MySQLConfig;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Adrian Simionescu
 */
public class DebitCard implements Card, java.io.Serializable {

    public static MySQLConfig model = null;
    
    final private int owner_id;
    final private int id;
    final private String label;
    private final BigDecimal amount;
    final private Set<Integer> persons = new HashSet<>();
    
    public DebitCard(int cardId, int ownerId, String label, BigDecimal amount) {
        id = cardId;
        owner_id = ownerId;
        this.label = label;
        this.amount = amount;
        amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public int getOwnerId() {
        return owner_id;
    }
    
    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    
    @Override
    public boolean equals(Object arg0) {
        return this.label.equals(((DebitCard) arg0).label);
    }
    
    @Override
    public int hashCode() {
        return this.label.hashCode();
    }
}
