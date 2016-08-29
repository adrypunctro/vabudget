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
    final private BigDecimal amount;
    final private String description;
    final private Date datetime;
    final private TransactionType type;

    Transaction(BigDecimal amount, String description, Date datetime, TransactionType type)
    {
        this.amount = amount;
        this.description = description;
        this.datetime = datetime;
        this.type = type;
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
