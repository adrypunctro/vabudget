/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Adrian Simionescu
 */
enum TransactionType {
    Income, Expense
}

public class Transaction {
    final private int id;
    final private int cardId;
    final private int userId;
    final private BigDecimal amount;
    final private String description;
    final private Date datetime;
    final private TransactionType type;

    Transaction(int transId, int cardId, int userId, String description, BigDecimal amount, Date datetime, TransactionType type)
    {
        this.id = transId;
        this.cardId = cardId;
        this.userId = userId;
        this.amount = amount;
        this.description = description;
        this.datetime = datetime;
        this.type = type;
    }

    public int getId() {
        return id;
    }
    
    public int getCardId() {
        return cardId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public TransactionType getType()
    {
        return type;
    }
    
    public BigDecimal getAmount()
    {
        return amount;
    }
    
    public Date getDate()
    {
        return datetime;
    }
    
    public String getDescription()
    {
        return description;
    }
}
