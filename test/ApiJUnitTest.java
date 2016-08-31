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
import java.math.BigInteger;
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
    
    private static Wallet wallet;
    
    public ApiJUnitTest() {
        System.out.println("Inside testPrintMessage()");
        
    }
    
    @BeforeClass
    public static void setUpClass() {
        wallet = new WalletPocket();
        wallet.connectWith(
            DAOFactory.getDAOFactory(DAOFactory.MYSQL)
        );
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
    public void card() {
        int ownerId = 1;
        int personId = 2;
        
        // Card with 0
        int cardNoInitId = wallet.addCard(ownerId, "Card 0");
        assertTrue("Creating card failed.", (cardNoInitId>0));
        
        Card cardNoInit = wallet.getCard(cardNoInitId);
        assertNotNull("Get card failed.", cardNoInit);
        
        assertEquals("Wrong label.", "Card 0", cardNoInit.getLabel());
        assertTrue("Wrong amount.", new BigDecimal(BigInteger.ZERO).compareTo(cardNoInit.getAmount()) == 0);
        assertEquals("Wrong ownerId.", ownerId, cardNoInit.getOwnerId());  
        
        // Card with 100 init
        int cardWithInitId = wallet.addCard(ownerId, "Card 100", new BigDecimal(100));
        assertTrue("Creating card failed.", (cardWithInitId>0));
        
        Card cardWithInit = wallet.getCard(cardWithInitId);
        assertNotNull("Get card failed.", cardWithInit);
        
        assertEquals("Wrong label.", "Card 100", cardWithInit.getLabel());
        assertTrue("Wrong amount.", new BigDecimal(100).compareTo(cardWithInit.getAmount()) == 0);
        assertEquals("Wrong ownerId.", ownerId, cardWithInit.getOwnerId());
        
        // Card with 22.20 init
        int cardWithInit22Id = wallet.addCard(ownerId, "Card 22", new BigDecimal("22.20"));
        assertTrue("Creating card failed.", (cardWithInit22Id>0));
        
        Card cardWithInit22 = wallet.getCard(cardWithInit22Id);
        assertNotNull("Get card failed.", cardWithInit22);
        
        assertEquals("Wrong label.", "Card 22", cardWithInit22.getLabel());
        assertTrue("Wrong amount.", new BigDecimal("22.20").compareTo(cardWithInit22.getAmount()) == 0);
        assertEquals("Wrong ownerId.", ownerId, cardWithInit22.getOwnerId());
        
        boolean card1Deleted = wallet.removeCard(cardNoInitId);
        assertTrue("Deleting card failed.", card1Deleted);
        
        boolean card2Deleted = wallet.removeCard(cardWithInitId);
        assertTrue("Deleting card failed.", card2Deleted);
        
        boolean card3Deleted = wallet.removeCard(cardWithInit22Id);
        assertTrue("Deleting card failed.", card3Deleted);
    }
    
    @Test
    public void shared() {
        int ownerId = 1;
        int personId = 2;
        int person2Id = 3;
        
        int privateCardId = wallet.addCard(ownerId, "Private account");
        assertTrue("Creating card failed.", (privateCardId>0));
        
        int sharedCardId = wallet.addCard(ownerId, "Shared account");
        assertTrue("Creating card failed.", (sharedCardId>0));
        
        int shared2CardId = wallet.addCard(ownerId, "Shared2 account");
        assertTrue("Creating card failed.", (shared2CardId>0));
        
        Card privateCard = wallet.getCard(privateCardId);
        assertNotNull("Get card failed.", privateCard);
        
        Card sharedCard = wallet.getCard(sharedCardId);
        assertNotNull("Get card failed.", sharedCard);
        
        Card shared2Card = wallet.getCard(sharedCardId);
        assertNotNull("Get card failed.", shared2Card);
        
        boolean sharedok = wallet.shareWith(sharedCardId, personId);
        assertTrue("Sharing card failed.", sharedok);
        
        boolean shared2ok = wallet.shareWith(shared2CardId, person2Id);
        assertTrue("Sharing card failed.", shared2ok);
        
        List<Share> shareds = wallet.getShared(sharedCardId);
        assertEquals("The number of shared is incorrect.", 1, shareds.size());
        
        List<Share> shareds2 = wallet.getShared(shared2CardId);
        assertEquals("The number of shared is incorrect.", 1, shareds2.size());
        
        Share entry = shareds.iterator().next();
        assertEquals("Incorect share cardId.", sharedCardId, entry.getCardId());
        assertEquals("Incorect share personId.", personId, entry.getPersonId());
        assertEquals("Incorect share default status.", "0", entry.getStatus());
        
        Share entry2 = shareds2.iterator().next();
        assertEquals("Incorect share cardId.", shared2CardId, entry2.getCardId());
        assertEquals("Incorect share personId.", person2Id, entry2.getPersonId());
        assertEquals("Incorect share default status.", "0", entry2.getStatus());

        boolean acceptRet = wallet.sharedAccept(sharedCardId, personId);
        assertTrue("Accepting share failed.", acceptRet);
        
        boolean acceptRet2 = wallet.sharedReject(shared2CardId, person2Id);
        assertTrue("Accepting share failed.", acceptRet2);
        
        List<Share> sharedsUpdated = wallet.getShared(sharedCardId);// Update!
        assertEquals("The number of shared is incorrect.", 1, shareds.size());
        
        List<Share> sharedsUpdated2 = wallet.getShared(shared2CardId);// Update!
        assertEquals("The number of shared is incorrect.", 1, shareds2.size());
        
        Share entryUpdated = sharedsUpdated.iterator().next();
        assertEquals("Incorect share cardId.", sharedCardId, entryUpdated.getCardId());
        assertEquals("Incorect share personId.", personId, entryUpdated.getPersonId());
        assertEquals("Incorect share status.", "1", entryUpdated.getStatus());
        
        Share entryUpdated2 = sharedsUpdated2.iterator().next();
        assertEquals("Incorect share cardId.", shared2CardId, entryUpdated2.getCardId());
        assertEquals("Incorect share personId.", person2Id, entryUpdated2.getPersonId());
        assertEquals("Incorect share status.", "-1", entryUpdated2.getStatus());
        
        boolean shareDeleted = wallet.removeShare(sharedCardId, personId);
        assertTrue("Deleting share failed.", shareDeleted);
        
        boolean shareDeleted2 = wallet.removeShare(shared2CardId, person2Id);
        assertTrue("Deleting share failed.", shareDeleted2);
        
        List<Share> sharedsUpdatedAD = wallet.getShared(sharedCardId);// Update!
        assertEquals("The number of shared is incorrect.", 0, sharedsUpdatedAD.size());
        
        List<Share> sharedsUpdatedAD2 = wallet.getShared(shared2CardId);// Update!
        assertEquals("The number of shared is incorrect.", 0, sharedsUpdatedAD2.size());
        
        boolean card1Deleted = wallet.removeCard(privateCardId);
        assertTrue("Deleting card failed.", card1Deleted);
        
        boolean card2Deleted = wallet.removeCard(sharedCardId);
        assertTrue("Deleting card failed.", card2Deleted);
        
        boolean card3Deleted = wallet.removeCard(shared2CardId);
        assertTrue("Deleting card failed.", card3Deleted);
    }
    
    @Test
    public void transaction() {
        int ownerId = 1;
        int personId = 2;
        
        int privateCard1Id = wallet.addCard(ownerId, "Private1 account");
        assertTrue("Creating card failed.", (privateCard1Id>0));
        
        int privateCard2Id = wallet.addCard(ownerId, "Private2 account");
        assertTrue("Creating card failed.", (privateCard2Id>0));
        
        int sharedCardId = wallet.addCard(ownerId, "Private account");
        assertTrue("Creating card failed.", (sharedCardId>0));
        
        boolean sharedok = wallet.shareWith(sharedCardId, personId);
        assertTrue("Sharing card failed.", sharedok);
        
        boolean acceptRet = wallet.sharedAccept(sharedCardId, personId);
        assertTrue("Accepting share failed.", acceptRet);
        
        Card privateCard1 = wallet.getCard(privateCard1Id);
        assertNotNull("Get card failed.", privateCard1);
        
        Card privateCard2 = wallet.getCard(privateCard2Id);
        assertNotNull("Get card failed.", privateCard2);
        
        Card sharedCard = wallet.getCard(sharedCardId);
        assertNotNull("Get card failed.", sharedCard);
        
        
        boolean income1ok = wallet.income(privateCard1Id, ownerId, new BigDecimal(120), "Some income.", new Date());
        assertTrue("Adding income failed.", income1ok);
        
        privateCard1 = wallet.getCard(privateCard1Id);// Update!
        assertTrue("Wrong amount.", new BigDecimal(120).compareTo(privateCard1.getAmount()) == 0);
        
        boolean income2ok = wallet.income(privateCard1Id, ownerId, new BigDecimal(130), "A high income.", new Date());
        assertTrue("Adding income failed.", income2ok);
        
        privateCard1 = wallet.getCard(privateCard1Id);// Update!
        assertTrue("Wrong amount.", new BigDecimal(250).compareTo(privateCard1.getAmount()) == 0);
        
        boolean expense1ok = wallet.expense(privateCard1Id, ownerId, new BigDecimal(220), "Some expense.", new Date());
        assertTrue("Adding expense failed.", expense1ok);
        
        privateCard1 = wallet.getCard(privateCard1Id);// Update!
        assertTrue("Wrong amount.", new BigDecimal(30).compareTo(privateCard1.getAmount()) == 0);
        
        boolean expense2ok = wallet.expense(privateCard1Id, ownerId, new BigDecimal("233.36"), "A high expense.", new Date());
        assertTrue("Adding expense failed.", expense2ok);
        
        privateCard1 = wallet.getCard(privateCard1Id);// Update!
        assertTrue("Wrong amount.", new BigDecimal("-203.36").compareTo(privateCard1.getAmount()) == 0);
        
        // ...
        
        List<Transaction> privateTransactions = wallet.history(privateCard1Id);
        
        assertNotNull("Error at get history transactions.", privateTransactions);
        
        assertEquals("The number of transactions is incorrect.", 4, privateTransactions.size());
        
        
        // ...
        
        
        
        boolean card1Deleted = wallet.removeCard(privateCard1Id);
        assertTrue("Deleting card failed.", card1Deleted);
        
        boolean card2Deleted = wallet.removeCard(privateCard2Id);
        assertTrue("Deleting card failed.", card2Deleted);
        
        boolean card3Deleted = wallet.removeCard(sharedCardId);
        assertTrue("Deleting card failed.", card3Deleted);
    }
    
    @Test
    public void distribution() {
        int ownerId = 1;
        int personId = 2;
        
        int privateCardId = wallet.addCard(ownerId, "Private account");
        assertTrue("Creating card failed.", (privateCardId>0));
        
        int sharedCardId = wallet.addCard(ownerId, "Shared account");
        assertTrue("Creating card failed.", (sharedCardId>0));
        
        Card privateCard = wallet.getCard(privateCardId);
        assertNotNull("Get card failed.", privateCard);
        
        Card sharedCard = wallet.getCard(sharedCardId);
        assertNotNull("Get card failed.", sharedCard);
        
        assertTrue("Wrong amount.", new BigDecimal(BigInteger.ZERO).compareTo(privateCard.getAmount()) == 0);
        assertTrue("Wrong amount.", new BigDecimal(BigInteger.ZERO).compareTo(sharedCard.getAmount()) == 0);
        
        Map<Integer, Integer> ratio = new HashMap<>();
        ratio.put(privateCardId, 40);
        ratio.put(sharedCardId, 60);
        int distribId = wallet.addDistribution(ownerId, "40-60", ratio);
        assertTrue("Creating distribution failed.", (distribId>0));
        
        boolean distOk = wallet.incomeDistrib(distribId, ownerId, new BigDecimal(1000), "Big income.", new Date());
        assertTrue("Incoming distribution failed.", distOk);
        
        privateCard = wallet.getCard(privateCardId);// Update!
        assertTrue("Wrong amount.", new BigDecimal(400).compareTo(privateCard.getAmount()) == 0);
        
        sharedCard = wallet.getCard(sharedCardId);// Update!
        assertTrue("Wrong amount.", new BigDecimal(600).compareTo(sharedCard.getAmount()) == 0);
        
        boolean distribDeleted = wallet.removeDistribution(distribId);
        assertTrue("Deleting distribution failed.", distribDeleted);
        
        boolean card1Deleted = wallet.removeCard(privateCardId);
        assertTrue("Deleting card failed.", card1Deleted);
        
        boolean card2Deleted = wallet.removeCard(sharedCardId);
        assertTrue("Deleting card failed.", card2Deleted);
    }
    
}
