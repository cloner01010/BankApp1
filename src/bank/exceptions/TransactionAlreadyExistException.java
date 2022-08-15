package bank.exceptions;

public class TransactionAlreadyExistException extends Throwable {
    public TransactionAlreadyExistException(String ausgabe){
        super(ausgabe);
    }
}
