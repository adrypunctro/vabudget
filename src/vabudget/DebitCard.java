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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian Simionescu
 */
public class DebitCard implements Card {

    public static Model model = null;
    
    final private int owner_id;
    final private int id;
    final private String label;
    private BigDecimal amount;
    final private Set<Integer> persons = new HashSet<Integer>();
    
    public DebitCard(Map<String, Object> data) {
        id = (int)data.get(Model.CARDS_CARD_ID);
        owner_id = (int)data.get(Model.CARDS_OWNER_ID);
        label = (String)data.get(Model.CARDS_LABEL);
        amount = new BigDecimal(String.valueOf(data.get(Model.CARDS_AMOUNT)));
        amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }
    
    @Override
    public int getId() {
        return id;
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
