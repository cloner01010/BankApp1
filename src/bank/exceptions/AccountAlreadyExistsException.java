package bank.exceptions;

public class AccountAlreadyExistsException extends Throwable {
    public AccountAlreadyExistsException(String ausgabe){
        super(ausgabe);
    }
}
