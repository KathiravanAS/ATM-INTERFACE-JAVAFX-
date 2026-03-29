package taskof5;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.application.Platform;


import java.io.IOException;
import java.util.HashMap;

public class atm_controller {

    static class User{
        private String name;
        private long accountNumber;
        private int pin;
        private double balance;
        public User(String name, long accountNumber, int pin) {
            this.name = name;
            this.accountNumber = accountNumber;
            this.pin = pin;
            this.balance = 0.0;
        }
        public int getPin(){
            return pin;
        }
        public double getBalance() {
            return balance;
        }
        public void deposit(double amount){
            this.balance += amount;
        }
        public boolean withdraw(double amount) {
            if (amount > balance) return false;
            this.balance -= amount;
            return true;
        }
    }

    static HashMap<Long, User> userDatabase = new HashMap<>();
    static User loggedInUser = null;

    @FXML
    private AnchorPane interior_panel;
    @FXML
    private Label l1,l2,l21,label,balanceLabel;
    @FXML
    private Button register,login,exit,help,Nsubmit,w,d,b;
    @FXML
    private TextField Nname,Naccno,Np1,Np2;
    @FXML
    private TextField loginAccNo, loginPin;
    @FXML
    private TextField withdrawField, depositField;

    @FXML
    void doRegister(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/taskof5/task_2/newUSerRegistration.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/taskof5/task_2/style.css").toExternalForm());
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void NSubmit(ActionEvent event) {
        try {
            String name = Nname.getText();
            String accNoText = Naccno.getText();
            String pinText = Np1.getText();
            String rePinText = Np2.getText();

            if (name.isEmpty() || accNoText.isEmpty() || pinText.isEmpty() || rePinText.isEmpty()) {
                l21.setText("Please fill all fields.");
                return;
            }

            long accNo = Long.parseLong(accNoText);
            int pin = Integer.parseInt(pinText);
            int re_pin = Integer.parseInt(rePinText);

            if (pin != re_pin) {
                l21.setText("Pin doesn't match.");
            }
            else if (userDatabase.containsKey(accNo)) {
                l21.setText("Account already exists.");
                }
            else {
                User newUser = new User(name, accNo, pin);
                userDatabase.put(accNo, newUser);
                l21.setText("Registration successful.");
            }
        } catch (NumberFormatException e) {
            l21.setText("Please enter numbers correctly.");
        }
    }
    @FXML
    void goBack(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/taskof5/task_2/atm_homepage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/taskof5/task_2/style.css").toExternalForm());
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("ATM Home");
        stage.show();
    }
    @FXML
    void doLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/taskof5/task_2/login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/taskof5/task_2/style.css").toExternalForm());
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void exit(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Exit");
        alert.setHeaderText("Warning");
        alert.setContentText("Do u really wanna exit ?");
        alert.showAndWait().ifPresent(response ->{
            if(response == ButtonType.OK){
                Platform.exit();
            }
        });
    }
    @FXML
    void loginSubmit(ActionEvent event) throws IOException {
        try {
            long accNo = Long.parseLong(loginAccNo.getText());
            int pin = Integer.parseInt(loginPin.getText());

            if (userDatabase.containsKey(accNo)) {
                User user = userDatabase.get(accNo);
                if (user.getPin() == pin) {
                    loggedInUser = user;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/taskof5/task_2/dashboard.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(getClass().getResource("/taskof5/task_2/style.css").toExternalForm());
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.setTitle("User Dashboard");
                    stage.show();
                } else {
                    label.setText("Incorrect PIN.");
                }
            } else {
                label.setText("Account not found.");
            }
        } catch (NumberFormatException e) {
            label.setText("Invalid account number or PIN.");
        }
    }
    @FXML
    void Withdraw(ActionEvent event) {
        try {
            double amount = Double.parseDouble(withdrawField.getText());
            if (loggedInUser.withdraw(amount)) {
                balanceLabel.setText("Withdraw successful. New balance: ₹" + loggedInUser.getBalance());
            } else {
                balanceLabel.setText("Insufficient balance.");
            }
        } catch (NumberFormatException e) {
            balanceLabel.setText("Enter a valid amount.");
        }
    }

    @FXML
    void Deposit(ActionEvent event) {
        try {
            double amount = Double.parseDouble(depositField.getText());
            loggedInUser.deposit(amount);
            balanceLabel.setText("Deposit successful. New balance: ₹" + loggedInUser.getBalance());
        } catch (NumberFormatException e) {
            balanceLabel.setText("Enter a valid amount.");
        }
    }

    @FXML
    void checkbalance(ActionEvent event) {
        if (loggedInUser != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Balance Information");
            alert.setHeaderText("Current Balance");
            alert.setContentText("₹" + loggedInUser.getBalance());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Login Required");
            alert.setHeaderText("Please Log In");
            alert.setContentText("You need to log in to check your balance.");
            alert.showAndWait();
        }
    }

    @FXML
    void HELP(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText(null);
        alert.setContentText("Feature not built yet.");
        alert.showAndWait();

    }
}