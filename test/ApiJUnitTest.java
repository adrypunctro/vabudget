/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import vabudget.Wallet;
import vabudget.WalletPocket;
import vabudget.Transaction;
import vabudget.Card;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import vabudget.DAOFactory;
import vabudget.Distribution;
import vabudget.Share;

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
        String sharedCardLabel  = "Shared Debit Card";
        // ------
        
        Wallet wallet = new WalletPocket();
        Map<String, String> config = new HashMap<>();
        wallet.connectWith(
            DAOFactory.getDAOFactory(DAOFactory.MYSQL)
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
        List<Share> shareds = wallet.getShared(sharedCardtId);
        
        assertEquals("The number of shared is incorrect.", 1, shareds.size());
        
        Share entry = shareds.iterator().next();
        
        assertSame("The partner is not correct..", personId, entry.getPersonId());
        assertEquals("Default shared is incorrect.", "0", entry.getStatus());

        wallet.sharedAccept(sharedCardtId, personId);
        
        List<Share> sharedsUpdated = wallet.getShared(sharedCardtId);// Update!
        
        assertEquals("The number of shared is incorrect.", 1, sharedsUpdated.size());
        
        Share entryUpdated = sharedsUpdated.iterator().next();
        
        assertSame("The partner is not correct..", personId, entryUpdated.getPersonId());
        assertEquals("Shared status is incorrect.", "1", entryUpdated.getStatus());
        
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
        
        // Income Distribution
        Map<Integer, Integer> ratio = new HashMap<>();
        ratio.put(privateCardId, 40);
        ratio.put(sharedCardtId, 60);
        int distribId = wallet.addDistribution(ownerId, "40-60", ratio);
        assertTrue("The distribution could not be added.", (distribId>0));
        
        /*List<Distribution> incomeDistributions = wallet.getDistributions(ownerId);
        
        Distribution distrib = incomeDistributions.iterator().next();
        
        Map<Integer, Integer> distribRatio = distrib.getRatio();
        
        BigDecimal beforeAmount = privateCard.getAmount();
        BigDecimal afterAmount = beforeAmount.add(new BigDecimal(1000*(distribRatio.get(privateCardId)/100.0f)));
        afterAmount.setScale(2);
        wallet.incomeDistrib(distrib.getId(), ownerId, new BigDecimal("1000.00"), "Big income.", new Date());
        
        privateCard = wallet.getCard(privateCardId);// Update!
        assertEquals("Account does not have the correct amount.",
                afterAmount, privateCard.getAmount());
        
        sharedCard = wallet.getCard(sharedCardtId);// Update!
        assertEquals("Account does not have the correct amount.",
                new BigDecimal("499.90"), sharedCard.getAmount());*/
        
        
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
