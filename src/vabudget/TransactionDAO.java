/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vabudget;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Adrian Simionescu
 */
public interface TransactionDAO {
    public boolean insertIncome(int userId, int cardId, String description, BigDecimal amount, Date datetime, String type);
    public boolean insertExpense(int userId, int cardId, String description, BigDecimal amount, Date datetime, String type);
    public boolean insertMultipleIncome(List<Transaction> transactions);
    
    public boolean deleteTransaction(int transId);
    public Transaction findTransaction(int transId);
    public boolean updateTransaction(int transId, String description);
    public List<Transaction> selectTransaction(int cardId);
}
