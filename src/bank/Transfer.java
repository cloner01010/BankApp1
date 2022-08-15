package bank;

/** Diese Klasse repräsentiert Überweisungen
 * Die Oberklasse ist die Klasse Transaction {@link Transaction}
 * */
public class Transfer extends Transaction implements CalculateBill{

    private String sender,recipient;

    /**Konstruktor, welcher das Datum, den Betrag und die Beschreibung zuordnet
     @param date String enthält das Datum der Ein- und Auszahlungen
     @param amount double enthält den Betrag der Ein- und Auszahlungen
     @param description String gibt die Beschreibung der Ein- und Auszahlungen an
     */
    public Transfer(String date,double amount, String description)
    {
        super(date, amount, description);
        sender="Keine Angaben.";
        recipient="Keine Angaben.";
        calculate();
    }
    /** Erweiterter Konstruktor
     *@param date String enthält das Datum der Ein- und Auszahlungen
     *@param amount double enthält den Betrag der Ein- und Auszahlungen
     *@param description String gibt die Beschreibung der Ein- und Auszahlungen an
     * @param sender String beinhaltet den Namen des Absenders
     * @param recipient String beinhaltet den Namen des Empfängers
     * */
    public Transfer(String date,double amount, String description,String sender, String recipient)
    {
        super(date, amount, description);
        this.sender=sender;
        this.recipient=recipient;
        calculate();
    }
    /** Kopie Konstruktor um Objekte der Klasse gleichzusetzen
     * @param Transfer {@link Transfer} Objekt der Klasse Transfer*/
    public Transfer(Transfer Transfer)
    {
        super(Transfer);
        this.sender=Transfer.sender;
        this.recipient=Transfer.recipient;
    }
    /** Gibt die Attribute des Objekts zurück
     @return String gibt die Attribute des Objekts als String zurück
     */
    public String toString()
    {
        return ("Überweisung:"+ "\n"+
                super.toString()+
                "Sender: "+ sender + "\n"+
                "Empfänger: " + recipient + "\n"
        );

    }
    /**
     * Überprüft Objekte der Klasse auf Gleichheit aller Attribute
     * @param transfer {@link Transfer} Objekt der Klasse Transfer
     * @return boolean gibt true zurück, wenn alle Attribute identisch sind und false wenn diese nicht identisch
     */
    public boolean equals(Object transfer)
    {
        if(!(transfer instanceof Transfer)){
            return false;
        }
        Transfer t=(Transfer)transfer;
        return ( super.equals(transfer) &&
                this.sender.equals(t.sender)&&
                this.recipient.equals(t.recipient ));

    }
    /* setter und Getter funktionen für die Klassenattribute*/
    /**
     * gibt den Empfänger zurück
     * @return String Empfänger*/
    public String getRecipient() {
        return recipient;
    }
    /**
     * Setzt das Attribut Recipient
     * @param recipient Empfänger
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    /**
     * gibt den Sender zurück
     * @return String Sender*/
    public String getSender() {
        return sender;
    }
    /**
     * Setzt das Attribut incomingInterest
     * @param sender Sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }
    /**
     *Berechnet den tatsächlichen Betrag nach Abzug anfallender Gebühren (hier=0€)
     * @return double tatsächlicher Betrag
     */
    @Override
    public double calculate() {
        return this.amount;
    }
}
