
# vabudget lib
It is a java application, an extension of vadry.ro project, that helps you manage your personal finances.

## Usage



### IWallet interface

public boolean **addAccount**(String name);
```java
IWallet wallet = new Wallet();

wallet.addAccount("Economy 1");
```
public IAccount **lastInsertAccount**();
```java
IWallet wallet = new Wallet();
wallet.addAccount("Economy 1");

IAccount privateAccount = wallet.lastInsertAccount();
```

public boolean **removeAccount**(IAccount accountObject);
```java
IWallet wallet = new Wallet();

wallet.removeAccount(privateAccount);
```
public IAccount **getAccount**(int accountId);
```java
IWallet wallet = new Wallet();

IAccount privateAccount = wallet.getAccount(1);
```
public List\<IAccount\> **getAll**();
```java
IWallet wallet = new Wallet();

List<IAccount> accounts = wallet.getAll();
```

### IAccount interface

public String **getLabel**();
```java
IWallet wallet = new Wallet();
wallet.addAccount("Economy 1");
IAccount privateAccount = wallet.lastInsertAccount();

System.out.println("Account label: " + privateAccount.getLabel());// Output: Account label: Economy 1
```
public BigDecimal **getAmount**();
```java
IWallet wallet = new Wallet();
wallet.addAccount("Economy 1");
IAccount privateAccount = wallet.lastInsertAccount();

System.out.println("Current amount: " + privateAccount.getAmount());// Output: Current amount: 0
```
public boolean **income**(BigDecimal amount);
```java
IWallet wallet = new Wallet();
wallet.addAccount("Economy 1");
IAccount privateAccount = wallet.lastInsertAccount();

privateAccount.income(new BigDecimal(110.00));
```
public boolean **income**(BigDecimal amount, String description);
```java
IWallet wallet = new Wallet();
wallet.addAccount("Economy 1");
IAccount privateAccount = wallet.lastInsertAccount();

privateAccount.income(new BigDecimal(120.00), "July salary");
```
public boolean **income**(BigDecimal amount, String description, Date datetime);
```java
IWallet wallet = new Wallet();
wallet.addAccount("Economy 1");
IAccount privateAccount = wallet.lastInsertAccount();

privateAccount.income(new BigDecimal(130.00), "July salary", new Date());
```

public boolean **expense**(BigDecimal amount);
```java
IWallet wallet = new Wallet();
wallet.addAccount("Economy 1");
IAccount privateAccount = wallet.lastInsertAccount();

privateAccount.expense(new BigDecimal(210.00));
```
public boolean **expense**(BigDecimal amount, String description);
```java
IWallet wallet = new Wallet();
wallet.addAccount("Economy 1");
IAccount privateAccount = wallet.lastInsertAccount();

privateAccount.expense(new BigDecimal(220.00), "Transport fuel");
```
public boolean **expense**(BigDecimal amount, String description, Date datetime);
```java
IWallet wallet = new Wallet();
wallet.addAccount("Economy 1");
IAccount privateAccount = wallet.lastInsertAccount();

privateAccount.expense(new BigDecimal(230.00), "Transport fuel", new Date());
```
public List\<Transaction\> **history**();

```java
IWallet wallet = new Wallet();
wallet.addAccount("Economy 1");
IAccount privateAccount = wallet.lastInsertAccount();

List<Transaction> transactions = privateAccount.history();
```


public boolean **shareWith**(int personId);

```java
IWallet wallet = new Wallet();
wallet.addAccount("Economy 1");
IAccount privateAccount = wallet.lastInsertAccount();

privateAccount.shareWith(12);
```


public boolean **removeShare**(int personId);

```java
IWallet wallet = new Wallet();
wallet.addAccount("Economy 1");
IAccount privateAccount = wallet.lastInsertAccount();

privateAccount.removeShare(12);
```


public Set\<Integer\> **getShared**();

```java
IWallet wallet = new Wallet();
wallet.addAccount("Economy 1");
IAccount privateAccount = wallet.lastInsertAccount();

Set<Integer> shared = privateAccount.getShared();
```

### Transaction

public TransactionType **getType**();
```java
IWallet wallet = new Wallet();
wallet.addAccount("Economy 1");
IAccount privateAccount = wallet.lastInsertAccount();
List<Transaction> transactions = privateAccount.history();

for(Transaction t : transactions) {
    if(t.getType(TransactionType.Income)) {
        System.out.println("Transaction income.");
    }
    else {
        System.out.println("Transaction expense.");
    }
}
```

public BigDecimal **getAmount**()
```java
IWallet wallet = new Wallet();
wallet.addAccount("Economy 1");
IAccount privateAccount = wallet.lastInsertAccount();
List<Transaction> transactions = privateAccount.history();

for(Transaction t : transactions) {
    if(t.getType(TransactionType.Income)) {
        System.out.println("+" + t.getAmount());
    }
    else {
        System.out.println("-" + t.getAmount());
    }
}
```

public String **getDescription**()
```java
IWallet wallet = new Wallet();
wallet.addAccount("Economy 1");
IAccount privateAccount = wallet.lastInsertAccount();
List<Transaction> transactions = privateAccount.history();

for(Transaction t : transactions) {
    if(t.getType(TransactionType.Income)) {
        System.out.println("+" + t.getAmount() + " " + t.getDescription());
    }
    else {
        System.out.println("-" + t.getAmount() + " " + t.getDescription());
    }
}
```

public Date **getDate**()
```java
IWallet wallet = new Wallet();
wallet.addAccount("Economy 1");
IAccount privateAccount = wallet.lastInsertAccount();
List<Transaction> transactions = privateAccount.history();

for(Transaction t : transactions) {
    if(t.getType(TransactionType.Income)) {
        System.out.println("+" + t.getAmount() + " " + t.getDescription() + " " + t.getDate().toString());
    }
    else {
        System.out.println("-" + t.getAmount() + " " + t.getDescription() + " " + t.getDate().toString());
    }
}
```
