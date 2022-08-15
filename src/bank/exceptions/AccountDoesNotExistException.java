package bank.exceptions;

public class AccountDoesNotExistException extends Throwable {
    public AccountDoesNotExistException(String ausgabe){
        super(ausgabe);
    }
}
