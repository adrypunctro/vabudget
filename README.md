
# vabudget lib
It is a java application, an extension of vadry.ro project, that helps you manage your personal finances.

## Usage



### Wallet interface

public int **addCard**(int ownerId, String label);
```java
IWallet wallet = new Wallet();

int ownerId = 1;// My Account ID
int privateCardId = wallet.addCard(ownerId, "Economy 1");
```
public int **addCard**(int ownerId, String label, BigDecimal initAmount);
public boolean **removeCard**(int cardId);
public IAccount **getCard**(int cardId);
public List\<Card\> **getCards**();

---

public boolean **income**(int cardId, int userId, BigDecimal amount, String description, Date datetime);
public boolean **incomeDistrib**(int distribId, int userId, BigDecimal amount, String description, Date datetime);
public boolean **expense**(int cardId, int userId, BigDecimal amount, String description, Date datetime);
public List\<Transaction\> **history**(int cardId);

---

public boolean **shareWith**(int cardId, int personId);
public boolean **removeShare**(int cardId, int personId);
public List\<Share\> **getShared**(int cardId);
public boolean **sharedAccept**(int cardId, int personId);
public boolean **sharedReject**(int cardId, int personId);

---

public int addDistribution(int ownerId, String label, Map<Integer, Integer> ratio);
public boolean removeDistribution(int distribId);
public Distribution getDistribution(int distribId);
public List\<Distribution\> getDistributions(int personId);

---

public boolean shareDistributionWith(int distribId, int personId);
public boolean removeShareDistribution(int distribId, int personId);
public boolean sharedDistributionAccept(int distribId, int personId);
public boolean sharedDistributionReject(int distribId, int personId);

