import bank.*;

import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAlreadyExistException;
import bank.exceptions.TransactionDoesNotExistException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrivateBankTest {
    PrivateBank account ;
    List<Transaction> testTransactions;
    Payment auszahlung;
    OutgoingTransfer transfer;
    Payment einzahlung;
    IncomingTransfer incomingTransfer;
    @BeforeEach
    void init() throws IOException, AccountAlreadyExistsException, TransactionAlreadyExistException, AccountDoesNotExistException {
        account= new PrivateBank("Test",0.5,0.5);
        auszahlung = new Payment("08.01.2022", -2000.00,"Test-Auszahlung",0.25,0.25);
        einzahlung= new Payment("08.01.2022", 2000.00,"Test-Einzahlung",0.5,0.5);
        transfer = new OutgoingTransfer("08.01.2022", 2000.00,"Test-Überweisung","Max","Ali");
        incomingTransfer = new IncomingTransfer("08.01.2022", 2000.00,"Test-Überweisung","Max","Test");
        account.createAccount("Test");
        account.addTransaction("Test",auszahlung);
        account.addTransaction("Test",einzahlung);
        account.addTransaction("Test",transfer);
        account.addTransaction("Test",incomingTransfer);

    }
    @AfterEach
    void undo() throws IOException, AccountDoesNotExistException {
       account.deleteAccount("Test");

    }

    @Test
    void KonstruktorTest(){
        assertEquals( "Test",account.getName());
        assertEquals( 0.5,account.getIncomingInterest());
        assertEquals(0.5, account.getOutgoingInterest());
    }
    @Test
    void testCreateAccount() throws AccountAlreadyExistsException {
        AccountAlreadyExistsException thrown = Assertions.assertThrows(AccountAlreadyExistsException.class
                ,()->{account.createAccount("Max");});
        Assertions.assertEquals("Account existiert bereits!", thrown.getMessage());

        assertTrue(account.accountsToTransactions.containsKey("Test"));

    }


    @Test
    void addTransaction() throws TransactionAlreadyExistException, AccountDoesNotExistException, AccountAlreadyExistsException, IOException, TransactionDoesNotExistException {


        TransactionAlreadyExistException thrown = Assertions.assertThrows(TransactionAlreadyExistException.class,
                ()->{account.addTransaction("Test",transfer);});
        Assertions.assertEquals("Transaction existiert bereits!", thrown.getMessage());


        AccountDoesNotExistException exception = Assertions.assertThrows(AccountDoesNotExistException.class,
                ()->{account.addTransaction("existiert nicht",transfer);});
        Assertions.assertEquals("Account existiert nicht!", exception.getMessage());

        assertTrue(account.containsTransaction("Test",incomingTransfer));

    }


    @Test
    void removeTransaction() throws TransactionDoesNotExistException, AccountAlreadyExistsException, IOException, TransactionAlreadyExistException, AccountDoesNotExistException {

        TransactionDoesNotExistException thrown = Assertions.assertThrows(TransactionDoesNotExistException.class,
                ()->{account.removeTransaction("Max",incomingTransfer);});
        Assertions.assertEquals("Transaction existiert nicht!", thrown.getMessage());

        account.removeTransaction("Test",transfer);
        assertFalse(account.containsTransaction("Test",transfer));
        account.removeTransaction("Test",einzahlung);
        assertFalse(account.containsTransaction("Test",einzahlung));
        account.removeTransaction("Test",incomingTransfer);
        assertFalse(account.containsTransaction("Test",incomingTransfer));
    }

    @Test
    void containsTransaction() throws  IOException {

        assertTrue( account.containsTransaction("Test",incomingTransfer));
        assertTrue(account.containsTransaction("Test",auszahlung));
    }

    @Test
    void getAccountBalance() throws IOException {

        assertEquals(-2000.0,account.getAccountBalance("Test"));
    }

    @Test
    void getTransactions() throws  IOException {
        assertEquals(account.getTransactions("Test").size(),
                account.accountsToTransactions.get("Test").size());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true,false})
    void getTransactionsSorted(boolean args) throws  IOException {
        account.getTransactionsSorted("Test",args);
        if(args){
            assertEquals(-3000,
                    account.accountsToTransactions.get("Test").get(0).readAmount());
        }
        else{
            assertEquals(2000,account.accountsToTransactions.get("Test").get(0).readAmount());
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true,false})
    void getTransactionsByType(boolean args) throws  IOException {
        List<Transaction> transactionsList = account.getTransactionsByType("Test",args);
        if(args){
            for(Transaction t: transactionsList){
                assertTrue(t.readAmount()>0);
            }
        }
        else{
            for(Transaction t: transactionsList){
                assertTrue(t.readAmount() <= 0);
            }
        }
    }

    @Test
    void writeAccount() throws  IOException {
        account.writeAccount("UserNotExist");
        account.readAccount();
        assertNull(account.accountsToTransactions.get("UserNotExist"));
        assertFalse(account.accountsToTransactions.get("Test").isEmpty());

    }

    @Test
    void readAccount() throws  IOException {
        account.readAccount();
        assertNotNull(account.accountsToTransactions.get("Test"));
        assertNotNull(account.accountsToTransactions.get("Max"));
        assertNull(account.accountsToTransactions.get("UserNotExist"));
    }
}