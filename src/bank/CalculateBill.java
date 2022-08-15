package bank;

/**
 * Wird verwendet um den tatsöchlichen Wert für das Attribut Amount der Klassen
 * @see Payment
 * @see Transfer auszurechnen

 */
public interface CalculateBill
{
    public double calculate();
}