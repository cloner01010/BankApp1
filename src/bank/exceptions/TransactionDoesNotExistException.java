package bank.exceptions;

public class TransactionDoesNotExistException extends Throwable {
    public TransactionDoesNotExistException(String ausgabe){
        super(ausgabe);
    }
}
