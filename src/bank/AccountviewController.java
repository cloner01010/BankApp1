package bank;
import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAlreadyExistException;
import bank.exceptions.TransactionDoesNotExistException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class AccountviewController {

    String accountname;
    PrivateBank privatebank;
    @FXML
    ListView<Transaction> transactionList;
    @FXML
    private Label kontoname;
    @FXML
    private Label balance;


    public void refresh() throws IOException {
        kontoname.setText(accountname);
        balance.setText("Kontostand: " + privatebank.getAccountBalance(accountname) );
        transactionList.getItems().clear();
        transactionList.getItems().addAll(privatebank.getTransactions(accountname));
    }

    @FXML public void initialize(){
    }

    @FXML
    public void addTransaction(ActionEvent actionEvent) throws InterruptedException, AccountDoesNotExistException, IOException, TransactionAlreadyExistException, AccountAlreadyExistsException {
        var dialog = new Dialog<>();
        dialog.setTitle("new Transaction");
        dialog.getDialogPane().getScene().getWindow().sizeToScene();

        var addT = new ButtonType("add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addT,ButtonType.CANCEL);

        var choices = new ArrayList<>();
        choices.add("Transfer");
        choices.add("Payment");

        var TType = new ChoiceBox<>();
        TType.getItems().addAll(choices);
        //TType.setValue("Transfer");

        var general = new GridPane();

        general.add(new Label("Transaction Type: "),0,0);
        general.add(TType,1,0);

        var date = new TextField();
        var amount = new TextField();
        var description = new TextField();

        //extra fields needed for Transfer
        var sender = new TextField();
        var reciever = new TextField();

        TType.addEventFilter(ActionEvent.ACTION,event->{
            System.out.println(TType.getSelectionModel().getSelectedItem() + " toggled");
            if(TType.getSelectionModel().getSelectedItem() == "Transfer"){
                var transfer = new GridPane();
                transfer.add(new Label("Transaction Type: "),0,0);
                transfer.add(TType,1,0);

                transfer.add(new Label("date: "),0,1);
                transfer.add(date,1,1);

                transfer.add(new Label("amount: "),0,2);
                transfer.add(amount,1,2);

                transfer.add(new Label("description: "),0,3);
                transfer.add(description,1,3);

                transfer.add(new Label("sender: "),0,4);
                transfer.add(sender,1,4);

                transfer.add(new Label("reciever"),0,5);
                transfer.add(reciever,1,5);

                dialog.getDialogPane().setContent(transfer);
                dialog.getDialogPane().getScene().getWindow().sizeToScene();
            }
            if(TType.getSelectionModel().getSelectedItem()== "Payment"){
                var payment = new GridPane();
                payment.add(new Label("Transaction Type: "),0,0);
                payment.add(TType,1,0);

                payment.add(new Label("date: "),0,1);
                payment.add(date,1,1);

                payment.add(new Label("amount: "),0,2);
                payment.add(amount,1,2);

                payment.add(new Label("description: "),0,3);
                payment.add(description,1,3);

                dialog.getDialogPane().setContent(payment);
                dialog.getDialogPane().getScene().getWindow().sizeToScene();
            }
        });

        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Necessary information missing!");

        // set the grid and show dialog

        dialog.getDialogPane().setContent(general);

        //get input

        var result =  dialog.showAndWait();

        if(result.isPresent() && result.get() == addT){
            if(date.getText().isEmpty()||amount.getText().isEmpty()||description.getText().isEmpty()){
                System.out.println("fehlende Eingabe");
                alert.showAndWait();
                return;
            }
            else{
                System.out.println(date.getText()+amount.getText()+description.getText());
            }

            double damount = Double.parseDouble(amount.getText());

            if(TType.getSelectionModel().getSelectedItem() == "Payment"){
                var nPay = new Payment(date.getText(),damount,description.getText());
                privatebank.addTransaction(accountname,nPay);
            }

            if(TType.getSelectionModel().getSelectedItem() =="Transfer"){
                if(sender.getText().isEmpty()||reciever.getText().isEmpty()){
                    System.out.println("fehlende Eingabe");
                    alert.showAndWait();
                    return;
                }

                var trans = new Transfer(date.getText(),damount,description.getText(),sender.getText(),reciever.getText());
                if(accountname.equals(sender.getText())){
                    var nTrans = new OutgoingTransfer(trans.getDate(),trans.getAmount(),trans.getDescription(),trans.getSender(),trans.getRecipient());
                    privatebank.addTransaction(accountname,nTrans);
                }
                if(accountname.equals(reciever.getText())){
                    var ninTrans = new IncomingTransfer(trans.getDate(),trans.getAmount(),trans.getDescription(),trans.getSender(),trans.getRecipient());
                    privatebank.addTransaction(accountname,ninTrans);
                }
            }
            refresh();
        } else {
            System.out.println(result.get());
        }
    }

    @FXML
    public void backitup(ActionEvent event) throws IOException{

        Stage newstage = new Stage();
        newstage = (Stage)transactionList.getScene().getWindow();
        Parent accounts = null;
        accounts = FXMLLoader.load(getClass().getResource("Mainview.fxml"));
        Scene scene = new Scene(accounts);
        newstage.setScene(scene);
    }
    public void setInformation(String key, PrivateBank fbank) throws IOException {
        accountname = key;
        privatebank = fbank;
        refresh();
    }
    @FXML
    public void sortasc() throws IOException {
        transactionList.getItems().clear();
        transactionList.getItems().addAll(privatebank.getTransactionsSorted(accountname, true));
    }
    @FXML
    public void sortdesc() throws IOException {
        transactionList.getItems().clear();
        transactionList.getItems().addAll(privatebank.getTransactionsSorted(accountname, false));
    }
    @FXML
    public void sortpos() throws IOException {
        transactionList.getItems().clear();
        transactionList.getItems().addAll(privatebank.getTransactionsByType(accountname, true));
    }
    @FXML
    public void sortneg() throws IOException {
        transactionList.getItems().clear();
        transactionList.getItems().addAll(privatebank.getTransactionsByType(accountname, false));
    }
    @FXML
    public void deleteTransaction() throws TransactionDoesNotExistException, IOException, AccountAlreadyExistsException {
        var trans = transactionList.getSelectionModel().getSelectedItem();
        if(trans == null){
            return;
        }

        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setHeaderText("Transaction löschen?");

        ButtonType yesButton = new ButtonType("Ja",  ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("Abbruch", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getButtonTypes().setAll(yesButton,noButton);

        var answer = dialog.showAndWait();

        if(answer.isPresent() && answer.get().getButtonData().isDefaultButton()){
            System.out.println("Lösche Transaktion "+trans.toString());
            privatebank.removeTransaction(accountname,(Transaction) trans);
            refresh();
        }
    }

}
