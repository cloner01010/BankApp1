package bank;

import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAlreadyExistException;
import bank.exceptions.TransactionDoesNotExistException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.w3c.dom.events.Event;

import java.io.IOException;
import java.util.Scanner;


public class MainApp extends Application{


    public void start (Stage stage) throws IOException {
        Parent anwendung = FXMLLoader.load(getClass().getResource("Mainview.fxml"));
        Scene scene = new Scene(anwendung);
        stage.setTitle("PrivateBank");
        stage.setScene(scene);
        stage.show();

    }

    public static void main (String args) throws IOException, AccountAlreadyExistsException, TransactionAlreadyExistException, AccountDoesNotExistException, TransactionDoesNotExistException {

        launch(args);
    }
}
