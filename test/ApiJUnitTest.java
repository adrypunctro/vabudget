/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import vabudget.ModelDatabase;
import vabudget.Wallet;
import vabudget.ModelREST;
import vabudget.WalletPocket;
import vabudget.Transaction;
import vabudget.Card;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Adrian Simionescu
 */
public class ApiJUnitTest {
    
    public ApiJUnitTest() {
        System.out.println("Inside testPrintMessage()");
        
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testOperations() {
        // Config
        int ownerId = 1;
        int personId = 2;
        String privateCardLabel = "Private Debit Card";
        String sharedCardLabel = "Shared Debit Card";
        // ------
        
        Wallet wallet = new WalletPocket();
        Map<String, String> config = new HashMap<>();
        wallet.connectWith(
                new ModelDatabase("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/VABUDGET", "vadry", "12wq!@WQ", "vab_")
        );
        
        // Create two accounts
        int privateCardId = wallet.addCard(ownerId, privateCardLabel);
        assertTrue("The account could not be created.", (privateCardId>0));
        
        Card privateCard = wallet.getCard(privateCardId);

        assertEquals("Wrong label.", privateCardLabel, privateCard.getLabel());
        
        int sharedCardtId = wallet.addCard(ownerId, sharedCardLabel);
        assertTrue("The account could not be created.", (sharedCardtId>0));
        
        Card sharedCard = wallet.getCard(sharedCardtId);
        
        assertEquals("Wrong label.", sharedCardLabel, sharedCard.getLabel());
        
        // Share account2 with person2
        boolean sharedok = wallet.shareWith(sharedCardtId, personId);
        assertTrue("The account could not be shared.", sharedok);
        
        // Show shared persons
        Map<Integer, String> shareds = wallet.getShared(sharedCardtId);
        
        
        assertEquals("Account does not have the correct amount.",
                new BigDecimal("0.00"), privateCard.getAmount());
        
        // Save an income
        wallet.income(privateCardId, ownerId, new BigDecimal("110.00"), "", new Date());
        privateCard = wallet.getCard(privateCardId);// Update!
        assertEquals("Account does not have the correct amount.",
                new BigDecimal("110.00"), privateCard.getAmount());
        
        wallet.income(privateCardId, ownerId, new BigDecimal("120.00"), "Some income.", new Date());
        privateCard = wallet.getCard(privateCardId);// Update!
        assertEquals("Account does not have the correct amount.",
                new BigDecimal("230.00"), privateCard.getAmount());
        
        wallet.income(privateCardId, ownerId, new BigDecimal("130.00"), "A high income.", new Date());
        privateCard = wallet.getCard(privateCardId);// Update!
        assertEquals("Account does not have the correct amount.",
                new BigDecimal("360.00"), privateCard.getAmount());
        
        // Save an expense
        wallet.expense(privateCardId, ownerId, new BigDecimal("210.00"), "", new Date());
        privateCard = wallet.getCard(privateCardId);// Update!
        assertEquals("Account does not have the correct amount.",
                new BigDecimal("150.00"), privateCard.getAmount());
        wallet.expense(privateCardId, ownerId, new BigDecimal("220.00"), "Some expense.", new Date());
        privateCard = wallet.getCard(privateCardId);// Update!
        assertEquals("Account does not have the correct amount.",
                new BigDecimal("-70.00"), privateCard.getAmount());
        wallet.expense(privateCardId, ownerId, new BigDecimal("230.00"), "A high expense.", new Date());
        privateCard = wallet.getCard(privateCardId);// Update!
        assertEquals("Account does not have the correct amount.",
                new BigDecimal("-300.00"), privateCard.getAmount());
        
        assertEquals("Account does not have the correct amount.",
                new BigDecimal("0.00"), sharedCard.getAmount());
        
        // Save an income (shared account)
        wallet.income(sharedCardtId, ownerId, new BigDecimal("310.10"), "", new Date());
        sharedCard = wallet.getCard(sharedCardtId);// Update!
        assertEquals("Account does not have the correct amount.",
                new BigDecimal("310.10"), sharedCard.getAmount());
        wallet.income(sharedCardtId, personId, new BigDecimal("320.05"), "Some income - shared.", new Date());
        sharedCard = wallet.getCard(sharedCardtId);// Update!
        assertEquals("Account does not have the correct amount.",
                new BigDecimal("630.15"), sharedCard.getAmount());
        wallet.income(sharedCardtId, personId, new BigDecimal("330.00"), "A high income - shared.", new Date());
        sharedCard = wallet.getCard(sharedCardtId);// Update!
        assertEquals("Account does not have the correct amount.",
                new BigDecimal("960.15"), sharedCard.getAmount());
        
        // Save an expense (shared account)
        wallet.expense(sharedCardtId, ownerId, new BigDecimal("410.00"), "", new Date());
        sharedCard = wallet.getCard(sharedCardtId);// Update!
        assertEquals("Account does not have the correct amount.",
                new BigDecimal("550.15"), sharedCard.getAmount());
        wallet.expense(sharedCardtId, personId, new BigDecimal("420.01"), "Some expense - shared.", new Date());
        sharedCard = wallet.getCard(sharedCardtId);// Update!
        assertEquals("Account does not have the correct amount.",
                new BigDecimal("130.14"), sharedCard.getAmount());
        wallet.expense(sharedCardtId, ownerId, new BigDecimal("430.04"), "A high expense - shared.", new Date());
        sharedCard = wallet.getCard(sharedCardtId);// Update!
        assertEquals("Account does not have the correct amount.",
                new BigDecimal("-299.90"), sharedCard.getAmount());
        
        // Show history
        List<Transaction> privateTransactions = wallet.history(privateCardId);
        
        assertNotNull("Error at get history transactions.", privateTransactions);
        
        assertEquals("The number of transactions is incorrect.",
                6, privateTransactions.size());
        
        
        List<Transaction> sharedTransactions = wallet.history(privateCardId);
        assertEquals("The number of transactions is incorrect.",
                6, sharedTransactions.size());
        
        
        // Remove same person for shared
        wallet.removeShare(sharedCardtId, personId);
        
        // Show shared persons
        wallet.getShared(sharedCardtId);
        
        // Remove all accounts        
        wallet.removeCard(privateCard.getId());
        wallet.removeCard(sharedCard.getId());
    }

}
