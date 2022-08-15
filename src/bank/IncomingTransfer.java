package bank;

/**
 * Repräsentiert Auszahlungen
 */

public class IncomingTransfer extends Transfer{
    /**
     * Konstruktor
     * @param date String datum
     * @param amount double Betrag
     * @param description String Beschreibung
     * @param sender String Sender
     * @param recipient String Sender
     */
    public IncomingTransfer(String date,double amount, String description,String sender, String recipient)
    {
        super(date, amount, description, sender, recipient);
    }
    public IncomingTransfer(IncomingTransfer o){
        super(o);
    }
    /**
     * Berechnet den Betrag
     * @return double Betrag
     */
    @Override
    public double calculate() {
        return this.amount;
    }
}
