package bank;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import bank.exceptions.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Diese Klasse repräsentiert ein Bankkonto
 * implements {@link Bank}
 */
public class PrivateBank implements Bank{

    private String name;
    private double incomingInterest, outgoingInterest;
    public Map<String,List<Transaction>> accountsToTransactions = new HashMap<String,List<Transaction>>();

    /**
     * Konstruktor
     * @param n String Name des Kontos
     * @param income Double Einzahlungsgebühr
     * @param outgoing double Auszahlungsgebühr
     */
    public PrivateBank(String n,double income,double outgoing) throws  IOException {
        name=n;
        incomingInterest=income;
        outgoingInterest=outgoing;
        readAccount();

    }
    /** Kopie Konstruktor um Objekte der Klasse gleichzusetzen
     * @param pb Objekt der Klasse Transaction {@link PrivateBank} */
    public PrivateBank(PrivateBank pb)
    {
        this.name=pb.name;
        this.incomingInterest=pb.incomingInterest;
        this.outgoingInterest=pb.outgoingInterest;

    }
    /** Gibt die Attribute des Objekts zurück
     @return String gibt die Attribute des Objekts als String zurück
     */
    @Override
    public String toString() {
        return "Name:: "+ name +"\n"+
                "Zinsfaktor bei der Einzahlung: "+ incomingInterest +"\n"+
                "Zinsfaktor bei der Auszahlung: " + outgoingInterest +"\n";
    }
    /**
     * Überprüft Objekte der Klasse auf Gleichheit aller Attribute
     * @param pb {@link Object} Objekt der Klasse Object
     * @return boolean gibt true zurück wenn alle Attribute identisch sind und false wenn diese nicht identisch
     */
    public boolean equals(Object pb)
    {
        PrivateBank p=(PrivateBank) pb;
        return( this.name.equals(p.name)&&
                this.incomingInterest==p.incomingInterest&&
                this.outgoingInterest==p.outgoingInterest&&
                this.accountsToTransactions.equals(p.accountsToTransactions)
        );
    }

    /**
     * Setzt den Namen
     * @param n String Name
     */
    public void setName(String n){name=n;}

    /**
     * Gibt den Namen zurück
     * @return String Name
     */
    public String getName(){return name;}
    /**
     * gibt die Einzahlungsgebühr zurück
     * @return double Einzahlungsgebühr
     */
    public double getIncomingInterest() {
        return incomingInterest;
    }

    /**
     * Setzt das Attribut incomingInterest
     * @param incomingInterest Einzahlungsgebühr
     */
    public void setIncomingInterest(double incomingInterest) {
        this.incomingInterest = incomingInterest;
    }
    /**
     * gibt die Auszahlungsgebühr zurück
     * @return double Auszahlungsgebühr
     */
    public double getOutgoingInterest() {
        return outgoingInterest;
    }
    /**
     * Setzt das Attribut outInterest
     * @param outgoingInterest Auszahlungsgebühr
     */
    public void setOutgoingInterest(double outgoingInterest) {
        this.outgoingInterest = outgoingInterest;
    }

    /**
     * Adds an account to the bank. If the account already exists, an exception is thrown.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account already exists
     */
    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException, IOException {
        if (this.accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Account existiert bereits!");
        } else {
        List<Transaction> emptyList=new ArrayList<>();

        accountsToTransactions.put(account,emptyList);}
        writeAccount(account);



    }

    /**
     * Adds an account (with all specified transactions) to the bank. If the account already exists,
     * an exception is thrown.
     *
     * @param account      the account to be added
     * @param transactions List of Transactions
     * @throws AccountAlreadyExistsException if the account already exists
     */
    @Override
    public void createAccount(String account, List<Transaction> transactions) throws AccountAlreadyExistsException
    {
        if (this.accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Account existiert bereits!");
        } else {

                 accountsToTransactions.put(account,transactions);}
                    try{writeAccount(account);}
                    catch (IOException e){ e.printStackTrace();}
    }

    /**
     * Adds a transaction to an account. If the specified account does not exist, an exception is
     * thrown. If the transaction already exists, an exception is thrown.
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which is added to the account
     * @throws TransactionAlreadyExistException if the transaction already exists
     */
    @Override
    public void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException, AccountDoesNotExistException, AccountAlreadyExistsException, IOException {
        if (!this.accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Account existiert nicht!");
        }else if(this.containsTransaction(account,transaction)){
            throw new TransactionAlreadyExistException("Transaction existiert bereits!");}
        else{
            if(transaction instanceof Payment)
            {
                ((Payment) transaction).setIncomingInterest(incomingInterest);
                ((Payment) transaction).setOutgoingInterest(outgoingInterest);

            }
            transaction.calculate();
            if(transaction instanceof Transfer){

                String empfaenger = ((Transfer) transaction).getRecipient();

               String sender = ((Transfer) transaction).getSender();
               if(account.equals(sender)){
                   IncomingTransfer in = new IncomingTransfer(transaction.date,transaction.amount,transaction.description,sender,empfaenger);
                   List<Transaction> temp = accountsToTransactions.get(empfaenger);
                   accountsToTransactions.replace(empfaenger,temp);
                   temp.add(in);
                   writeAccount(empfaenger);

               }
               else{
                   OutgoingTransfer out = new OutgoingTransfer(transaction.date, transaction.amount, transaction.description,sender,empfaenger);
                   List<Transaction> temp = accountsToTransactions.get(sender);
                   accountsToTransactions.replace(sender,temp);
                   out.setAmount(out.getAmount());
                   temp.add(out);
                   writeAccount(sender);
               }



            }

                List<Transaction> temp = accountsToTransactions.get(account);
            transaction.setAmount(transaction.getAmount());
                temp.add(transaction);
                accountsToTransactions.replace(account,temp);


        }
        try{writeAccount(account);}
        catch (IOException e){ e.printStackTrace();}

    }

    /**
     * Removes a transaction from an account. If the transaction does not exist, an exception is
     * thrown.
     *
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is added to the account
     * @throws TransactionDoesNotExistException if the transaction cannot be found
     */
    @Override
    public void removeTransaction(String account, Transaction transaction) throws TransactionDoesNotExistException, AccountAlreadyExistsException, IOException {
       if (!this.containsTransaction(account,transaction)) {
            throw new TransactionDoesNotExistException("Transaction existiert nicht!");
        } else {
        List<Transaction> tempListe = accountsToTransactions.get(account);
        tempListe.remove(transaction);
        accountsToTransactions.replace(account,tempListe);
           try{writeAccount(account);}
           catch (IOException e){ e.printStackTrace();}
        }
    }

    /**
     * Checks whether the specified transaction for a given account exists.
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction which is added to the account
     */
    @Override
    public boolean containsTransaction(String account, Transaction transaction) throws IOException {
        readAccount();
        List<Transaction> tempListe = accountsToTransactions.get(account);
       for(Transaction t : tempListe){
           if(t.equals(transaction)){
               return true;
           }

       }


        return false;
    }

    /**
     * Calculates and returns the current account balance.
     *
     * @param account the selected account
     * @return the current account balance
     */
    @Override
    public double getAccountBalance(String account) throws  IOException {
        readAccount();
        double kontoStand=0.0;
        List<Transaction> tempListe = accountsToTransactions.get(account);
        for(Transaction transaction : tempListe)
        {
               /* if(!(transaction instanceof Payment)){
                    kontoStand+=transaction.calculate();
                }
                else{
                    kontoStand+=transaction.getAmount();
                }*/
            kontoStand+=transaction.amount;




        }
        return kontoStand;
    }

    /**
     * Returns a list of transactions for an account.
     *
     * @param account the selected account
     * @return the list of transactions
     */
    @Override
    public List<Transaction> getTransactions(String account) {

        return accountsToTransactions.get(account);
    }

    /**
     * Comparator fürs Sortieren nach Amount
     */
    public static final Comparator<Transaction> Amount_COMPARATOR = Comparator
            .comparing(Transaction::readAmount);

    /**
     * Returns a sorted list (-> calculated amounts) of transactions for a specific account . Sorts the list either in ascending or descending order
     * (or empty).
     *
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted ascending or descending
     * @return the list of transactions
     */
    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) throws  IOException {
        readAccount();
        List<Transaction> tempListe = accountsToTransactions.get(account);
        if(asc){
            tempListe.sort(Amount_COMPARATOR);
        }
        else{
            tempListe.sort(Amount_COMPARATOR);
            Collections.reverse(tempListe);

        }
        return tempListe;
    }

    /**
     * Returns a list of either positive or negative transactions (-> calculated amounts).
     *
     * @param account  the selected account
     * @param positive selects if positive  or negative transactions are listed
     * @return the list of transactions
     */
    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) throws IOException {
        readAccount();
        List<Transaction> tmp = getTransactions(account);
        List<Transaction> temp = new ArrayList<Transaction>();
        for (Transaction transaction : tmp) {
            if (positive) {
                if (transaction.amount > 0)
                    temp.add(transaction);
            }
            else if (transaction.amount <= 0)
                temp.add(transaction);
        }
        return temp;

    }

    @Override
    public void writeAccount(String account) throws IOException {

        List<Transaction> transactions = getTransactions(account);

        FileWriter writer = new FileWriter("C:\\Users\\aliam\\IdeaProjects\\BankApp1\\resources\\Accounts\\"+account +".json");

        GsonBuilder builder = new GsonBuilder();
        if(transactions==null){
            return;
        }
        Gson gson = builder.registerTypeAdapter(transactions.getClass(), new PrivateBankSerializer())
                .setPrettyPrinting()
                .create();
        writer.write(gson.toJson(transactions));




        writer.close();


    }

    @Override
    public void readAccount() throws IOException {
        File file = new File("C:\\Users\\aliam\\IdeaProjects\\BankApp1\\resources\\Accounts");
        File[] files = file.listFiles();
        for (File value : files) {
            FileReader fs = new FileReader(value.getAbsolutePath());
            List<Transaction> transactions = new ArrayList<>();
            GsonBuilder gsonBuilder = new GsonBuilder();

            Gson gson = gsonBuilder.registerTypeAdapter(transactions.getClass(), new PrivateBankDeserializer()).create();
            transactions = gson.fromJson(fs, transactions.getClass());
            String filename = value.getName();
            fs.close();
            String accountName = filename.substring(0, filename.length() - 5);
            if (accountsToTransactions.containsKey(accountName)) {
                accountsToTransactions.replace(accountName, transactions);
            } else {
                accountsToTransactions.put(accountName,transactions);

            }


        }


    }


    @Override
    public ObservableList<String> getAllAccounts() {
        List<String> allAccounts = new ArrayList<String>();
        String Pfad = "C:\\Users\\aliam\\IdeaProjects\\BankApp1\\resources\\Accounts";
        File[] files = new File(Pfad).listFiles();
        for (File file : files){
            if(file.isFile()){
                String addaccount = file.getName();
                addaccount = addaccount.substring(0, addaccount.lastIndexOf('.'));
                allAccounts.add(addaccount);
            }
        }
        ObservableList<String> items = FXCollections.observableArrayList (allAccounts);
        return items;
    }

    @Override
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException {
        if (!this.accountsToTransactions.containsKey(account))
        {
            throw new AccountDoesNotExistException("Account exisitert nicht!");
        }
        else
        {
            if(Files.exists(Path.of("C:\\Users\\aliam\\IdeaProjects\\BankApp1\\resources\\Accounts\\" + account + ".json"))){
                try {
                    Files.delete(Path.of("C:\\Users\\aliam\\IdeaProjects\\BankApp1\\resources\\Accounts\\" + account + ".json"));
                }catch (IOException e){ e.printStackTrace();}
            }
            this.accountsToTransactions.remove(account);

        }
    }
}
