package bank;


/**
 * Diese Klasse repräsentiert mögliche Transaktionen
 * Die Unterklassen sind die Klasse Transfer {@link Transfer} und die KLasse Payment {@link Payment}*/
public abstract class Transaction implements CalculateBill{

    protected String date,description;
    protected double amount;
    /**Konstruktor, welcher das Datum, den Betrag und die Beschreibung zuordnet
     @param date String enthält das Datum der Transaktion
     @param amount double enthält den Betrag der Transaktion
     @param description String gibt die Beschreibung der Transaktion an
     */
    public Transaction(String date,double amount, String description)
    {
        this.amount=amount;
        this.date=date;
        this.description=description;

    }
    /** Kopie Konstruktor um Objekte der Klasse gleichzusetzen
     * @param transaction Objekt der Klasse Transaction {@link Transaction} */
    public Transaction(Transaction transaction)
    {
        this.amount=transaction.amount;
        this.date=transaction.date;
        this.description=transaction.description;

    }
    /** Gibt die Attribute des Objekts zurück
     @return String gibt die Attribute des Objekts als String zurück
     */
    public String toString()
    {

        return (
                "Betrag: "+ amount +"\n"+
                        "Datum: "+ date +"\n"+
                        "Beschreibung: " + description+"\n"
        );
    }
    /**
     * Überprüft Objekte der Klasse auf Gleichheit aller Attribute
     * @param t {@link Transaction} Objekt der Klasse Transfer
     * @return boolean gibt true zurück, wenn alle Attribute identisch sind und false wenn diese nicht identisch
     */
    public boolean equals(Object t)
    {
        if(!(t instanceof Transaction)){
            return false;
        }
        Transaction trans=(Transaction)t;
        return( this.amount==trans.amount&&
                this.date.equals(trans.date)&&
                this.description.equals(trans.description ));
    }
    /* setter und Getter funktionen für die Klassenattribute*/
    /**
     * Setzt das Attribut amount
     * @param amount Betrag
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    /**
     * gibt den Betrag zurück
     * @return String Betrag*/
    public double getAmount() {
        if(this instanceof Payment){
            return amount;
        }
        else{
            return this.calculate();
        }

    }
    public double readAmount() {
      return amount;

    }

    /**
     * gibt das Datum zurück
     * @return String Datum*/
    public String getDate() {
        return date;
    }
    /**
     * Setzt das Attribut date
     * @param date Daum
     */
    public void setDate(String date) {
        this.date = date;
    }
    /**
     * gibt die Beschreibung zurück
     * @return String Beschreibung*/
    public String getDescription() {
        return description;
    }
    /**
     * Setzt das Attribut description Beschreibung
     * @param description Beschreibung
     */
    public void setDescription(String description) {
        this.description = description;
    }
    public abstract double calculate();




}
