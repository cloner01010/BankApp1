import bank.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {
    Transfer transfer;
    @BeforeEach
    void init(){
        transfer = new Transfer("08.01.2022", 2000.00,"Test-Überweisung","Max","Ali");
    }
    @Test
    void testKonstruktor(){
        assertEquals("08.01.2022", transfer.getDate());
        assertEquals(2000.0, transfer.getAmount());
        assertEquals("Test-Überweisung",transfer.getDescription());
        assertEquals("Max",transfer.getSender());
        assertEquals("Ali", transfer.getRecipient());
    }

    @Test
    void testToString() {
        String expected = """
            Überweisung:
            Betrag: 2000.0
            Datum: 08.01.2022
            Beschreibung: Test-Überweisung
            Sender: Max
            Empfänger: Ali
            """;
        assertEquals(expected,transfer.toString());

    }

    @Test
    void testEquals() {
        Payment payment = new Payment("08.01.2022", 2000.00,"Test-Einzahlung",0.5,0.5);
        assertFalse(transfer.equals(payment));
    }

    @Test
    void calculate() {
        IncomingTransfer incomingTransfer = new IncomingTransfer("08.01.2022", 2000.00,"Test","Max","Ali");
        OutgoingTransfer outgoingTransfer = new OutgoingTransfer("08.01.2022", 2000.00,"Test","Ali","Max");
        assertEquals(2000.0,incomingTransfer.calculate());
        assertEquals(-2000.0,outgoingTransfer.calculate());
    }

    @Test
    void copyTest(){
        Transfer pCopy = new Transfer(transfer);
        assertTrue(pCopy.equals(transfer));
    }
}