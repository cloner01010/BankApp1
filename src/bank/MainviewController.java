package bank;

import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAlreadyExistException;
import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainviewController{

    private PrivateBank privatebank;

    @FXML
    public MenuItem delete;
    @FXML
    public MenuItem pick;
    @FXML
    public Button accounterstellen;
    @FXML
    public ListView<String> listaccounts;

    private void refresh(){
        listaccounts.getItems().clear();
        listaccounts.getItems().addAll(privatebank.getAllAccounts());
    }

    public MainviewController() throws AccountAlreadyExistsException, IOException {
    }


    public void initialize() throws AccountAlreadyExistsException, IOException, TransactionAlreadyExistException, AccountDoesNotExistException {
        privatebank = new PrivateBank("Die Bank", 0.02, 0.02);
        listaccounts.getItems().addAll(privatebank.getAllAccounts());
    }
    /**
     * method to create a new Account in GUI
     *
     * @param actionEvent
     * @throws AccountAlreadyExistsException
     * @throws IOException
     */
    public void accounterstellen(ActionEvent actionEvent)throws AccountAlreadyExistsException, IOException{
        TextInputDialog input = new TextInputDialog();
        String inputaccount = "";
        input.setHeaderText("Account erstellen:");
        input.setTitle("Account-Generator");
        input.showAndWait();
        inputaccount = input.getEditor().getText();
        if(inputaccount == "" || inputaccount == null){
            return;
        }
        System.out.println(inputaccount);
        try{
            privatebank.createAccount(inputaccount);
        }catch (AccountAlreadyExistsException e){
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Account existiert Bereits");
            alert.showAndWait();
        }
        refresh();
    }
    /**
     * method to select a Item from the Accountlist and change into a Detail view of the chosen account
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void pick(ActionEvent event) throws IOException{
        if(listaccounts.getSelectionModel().getSelectedItems() == null){
            return;
        }
        Stage newstage = new Stage();
        newstage = (Stage)listaccounts.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Accountview.fxml"));
            Parent transactions = loader.load();

            AccountviewController accountviewController = loader.getController();
            accountviewController.setInformation(listaccounts.getSelectionModel().getSelectedItem(),privatebank);
            // Scene scene = new Scene(transactions);
            newstage.setScene(new Scene(transactions));
            newstage.show();
            //transactions = FXMLLoader.load(getClass().getResource("Accountview.fxml"));

    }
    /**
     * method to delete Account in GUI
     *
     * @param event
     * @throws AccountDoesNotExistException
     * @throws IOException
     */
    @FXML
    public void delete(ActionEvent event) throws IOException, AccountDoesNotExistException {
        //String wegdamit = "";
        String wegdamit = listaccounts.getSelectionModel().getSelectedItem().toString();
        if(wegdamit == null){
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sind Sie sicher?");
        alert.setContentText("Soll der Account wirklich geloescht werden?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
                System.out.println(wegdamit);
                privatebank.deleteAccount(wegdamit);
                refresh();
        }
    }


}
