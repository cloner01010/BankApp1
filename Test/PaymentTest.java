import static org.junit.jupiter.api.Assertions.*;

import bank.*;
import org.junit.jupiter.api.*;

class PaymentTest {
    Payment einzahlung;
    Payment auszahlung;
    @BeforeEach
    void init(){
        auszahlung = new Payment("08.01.2022", -2000.00,"Test-Auszahlung",0.25,0.25);
        einzahlung= new Payment("08.01.2022", 2000.00,"Test-Einzahlung",0.5,0.5);
    }
    @Test
    void testKonstruktor(){
        assertEquals("08.01.2022", einzahlung.getDate());
        assertEquals(2000.0, einzahlung.getAmount());
        assertEquals("Test-Einzahlung",einzahlung.getDescription());
        assertEquals(0.5,einzahlung.getIncomingInterest());
        assertEquals(0.5, einzahlung.getOutgoingInterest());
    }

    @Test
    void testToString() {
        String expected = """
            Ein-Auszahlung:
            Betrag: 2000.0
            Datum: 08.01.2022
            Beschreibung: Test-Einzahlung
            Zinsfaktor bei der Einzahlung: 0.5
            Zinsfaktor bei der Auszahlung: 0.5
            """;
        assertEquals(expected,einzahlung.toString());

    }

    @Test
    void testEquals() {
        Transaction transaction = new Transfer("08.01.2022",2000.00,"Test-Einzahlung");
        assertFalse(einzahlung.equals(transaction));
        assertFalse(einzahlung.equals(auszahlung));

    }

    @Test
    void calculateTest() {
        double expected_incoming =(1-einzahlung.getIncomingInterest())* einzahlung.getAmount();
        double expected_outgoing = (1+auszahlung.getOutgoingInterest())* auszahlung.getAmount();
        assertEquals(expected_incoming,einzahlung.calculate());
        assertEquals(expected_outgoing,auszahlung.calculate());
    }
    @Test
    void copyTest(){
        testEquals();
        Payment pCopy = new Payment(einzahlung);
        assertTrue(pCopy.equals(einzahlung));
    }

}