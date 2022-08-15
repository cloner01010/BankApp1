package bank;

/**
 Diese Klasse repräsentiert Ein- und Auszahlungen
 Die Oberklasse ist {@link Transaction}
 */
public class Payment extends Transaction implements CalculateBill{
    private double incomingInterest, outgoingInterest;

    /**Konstruktor, welcher das Datum, den Betrag und die Beschreibung zuordnet
     @param date String enthält das Datum der Ein- und Auszahlungen
     @param amount double enthält den Betrag der Ein- und Auszahlungen
     @param description String gibt die Beschreibung der Ein- und Auszahlungen an
     */
    public Payment(String date,double amount, String description)
    {
        super(date, amount, description);
    }
    /** Erweiterter Konstruktor
     @param date String enthält das Datum der Ein- und Auszahlungen
     @param amount double enthält den Betrag der Ein- und Auszahlungen
     @param description String gibt die Beschreibung der Ein- und Auszahlungen an
     @param income double gibt die prozentuale Einzahlungsgebühr an
     @param outgoing double gibt die prozentuale Auszahlungsgebühr an
     */
    public Payment(String date,double amount, String description, double income, double outgoing)
    {
        super(date, amount, description);
        this.incomingInterest=income;
        this.outgoingInterest=outgoing;
        //calculate();
    }
    /** Kopie Konstruktor um Instanzen der KLasse gleichzusetzen
     @param pay {@link Payment} Instanz der Klasse Payment
     */
    public Payment(Payment pay)
    {
        super(pay);
        this.incomingInterest=pay.incomingInterest;
        this.outgoingInterest=pay.outgoingInterest;
    }
    /** Gibt die Attribute des Objekts zurück
     @return String gibt die Attribute des Objekts als String zurück
     */
    @Override
    public String toString()
    {

        return ("Ein-Auszahlung:"+ "\n" +
                super.toString()+
                "Zinsfaktor bei der Einzahlung: "+ incomingInterest +"\n"+
                "Zinsfaktor bei der Auszahlung: " + outgoingInterest +"\n"
        );

    }


    /**
     * Überprüft Objekte der Klasse auf Gleichheit aller Attribute
     * @param pay {@link Payment} Objekt der Klasse Payment
     * @return boolean gibt true zurück wenn alle Attribute identisch sind und false wenn diese nicht identisch
     */
    public boolean equals(Object pay)
    {
        if(!(pay instanceof Payment)){
            return false;
        }
        Payment p=(Payment)pay;
        return(super.equals(pay)&&
                this.incomingInterest==p.incomingInterest&&
                this.outgoingInterest==p.outgoingInterest
        );
    }
    /* setter und Getter funktionen für die Klassenattribute*/

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
     * Setzt das Attribut incomingInterest
     * @param outgoingInterest Auszahlungsgebühr
     */
    public void setOutgoingInterest(double outgoingInterest) {
        this.outgoingInterest = outgoingInterest;
    }

    @Override
    /**
     *Berechnet den tatsächlichen Ein-Auszahlungsbetrag nach Abzug anfallender Ein-Auszahlungsgebühren
     * @return double tatsächlicher Ein-Auszahlungsbetrag
     */
    public double calculate() {
        if(this.amount>0.0)
        {
            this.amount = this.amount * (1.0 - incomingInterest);
        }

        else if(this.amount < 0.0)
        {
            this.amount = this.amount * (1.0 + outgoingInterest);
        }
        return this.amount;
    }
}
