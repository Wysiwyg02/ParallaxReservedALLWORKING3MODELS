/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naturemorning.parallax.controllers;

import com.naturemorning.parallax.config.StageManager;
import com.naturemorning.parallax.models.Cashier;
import com.naturemorning.parallax.models.Delivery;
import com.naturemorning.parallax.services.impl.CashierService;
import com.naturemorning.parallax.services.impl.DeliveryService;
import com.naturemorning.parallax.views.FxmlView;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
public class CashierPayment implements Initializable {

    @FXML
    private TextField cashierName;

    @FXML
    private TextField cashierAge;

    @FXML
    private TextField amount;

    @FXML
    private TableView<Cashier> paymentTable;

    @FXML
    private TableColumn<Cashier, Long> colID;

    @FXML
    private TableColumn<Cashier, String> colName;

    @FXML
    private TableColumn<Cashier, String> colAge;

    @FXML
    private TableColumn<Cashier, String> colAmount;

    @FXML
    private TableColumn<Cashier, String> colPayment;
    @FXML
    private TableColumn<Cashier, Boolean> colEdit;

    @FXML
    private Button confirm;

    @FXML
    private Button delete;

    @FXML
    private Button reset;
    
    @FXML
    private RadioButton rbtn_delivery;

    @FXML
    private RadioButton rbtn_reservation;

    @FXML
    private Button back;

    @FXML
    private Label paymentId;
    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private CashierService cashierService;
    private ObservableList<Cashier> cashierList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        paymentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();
        loadUserDetails();
    }

    @FXML
    void delete(ActionEvent event) {
        List<Cashier> users = paymentTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected user?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            cashierService.deleteInBatch(users);
        }

        loadUserDetails();
    }

    @FXML
    void payment(ActionEvent event) {
        if (validate("Cashier Name", getCashierName(), "([a-zA-Z]{3,30}\\s*)+")
                && validate("Cashier Age", getCashierAge(), "([0-9]\\s*)+")) {
            if (paymentId.getText() == null || "".equals(paymentId.getText())) {
                if (true) {

                    Cashier user = new Cashier();
                    user.setCashierName(getCashierName());
                    user.setCashierAge(getCashierAge());
                    user.setAmount(getAmount());
                    user.setPaymentType(getPaymentType());
                    Cashier newUser = cashierService.save(user);

                    saveAlert(newUser);
                }
            } else {
                Cashier user = cashierService.find(Long.parseLong(paymentId.getText()));
                user.setCashierName(getCashierName());
                user.setCashierAge(getCashierAge());
                user.setAmount(getAmount());
                user.setPaymentType(getPaymentType());

                Cashier updatedUser = cashierService.update(user);
                updateAlert(updatedUser);
            }

            clearFields();
            loadUserDetails();
        }
    }

    public String getCashierName() {
        return cashierName.getText();
    }

    public String getCashierAge() {
        return cashierAge.getText();
    }

    public String getAmount() {
        return amount.getText();
    }

    public String getPaymentType() {
        return rbtn_delivery.isSelected() ? "Delivery" : "Reservation";
    }

    private void saveAlert(Cashier user) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText(" Cashier " + getCashierName() + " with an age of" + getCashierAge() + " has been created and \n" + " id is " + user.getId() + ".");
        alert.showAndWait();
        loadUserDetails();
    }

    private void clearFields() {
        cashierName.clear();
        cashierAge.clear();
        amount.clear();
    }

    private void updateAlert(Cashier user) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The user " + user.getCashierName() + " " + user.getCashierAge() + " has been updated.");
        alert.showAndWait();
    }

    @FXML
    void reset(ActionEvent event) {
        cashierName.clear();
        cashierAge.clear();
        amount.clear();
    }

    private boolean validate(String field, String value, String pattern) {
        if (!value.isEmpty()) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(value);
            if (m.find() && m.group().equals(value)) {
                return true;
            } else {
                validationAlert(field, false);
                return false;
            }
        } else {
            validationAlert(field, true);
            return false;
        }
    }

    private void validationAlert(String field, boolean empty) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        if (field.equals("Role")) {
            alert.setContentText("Please Select " + field);
        } else {
            if (empty) {
                alert.setContentText("Please Enter " + field);
            } else {
                alert.setContentText("Please Enter Valid " + field);
            }
        }
        alert.showAndWait();
    }

    private boolean emptyValidation(String field, boolean empty) {
        if (!empty) {
            return true;
        } else {
            validationAlert(field, true);
            return false;
        }
    }

    private void setColumnProperties() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("cashierName"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("cashierAge"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colPayment.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        colEdit.setCellFactory(cellFactory);
    }

    Callback<TableColumn<Cashier, Boolean>, TableCell<Cashier, Boolean>> cellFactory
            = new Callback<TableColumn<Cashier, Boolean>, TableCell<Cashier, Boolean>>() {
        @Override
        public TableCell<Cashier, Boolean> call(final TableColumn<Cashier, Boolean> param) {
            final TableCell<Cashier, Boolean> cell = new TableCell<Cashier, Boolean>() {
                Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
                final Button btnEdit = new Button();

                @Override
                public void updateItem(Boolean check, boolean empty) {
                    super.updateItem(check, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btnEdit.setOnAction(e -> {
                            Cashier user = getTableView().getItems().get(getIndex());
                            updateUser(user);
                        });

                        btnEdit.setStyle("-fx-background-color: transparent;");
                        ImageView iv = new ImageView();
                        iv.setImage(imgEdit);
                        iv.setPreserveRatio(true);
                        iv.setSmooth(true);
                        iv.setCache(true);
                        btnEdit.setGraphic(iv);

                        setGraphic(btnEdit);
                        setAlignment(Pos.CENTER);
                        setText(null);
                    }
                }

                private void updateUser(Cashier user) {
                    paymentId.setText(Long.toString(user.getId()));
                    cashierName.setText(user.getCashierName());
                    cashierAge.setText(user.getCashierAge());
                    amount.setText(user.getAmount());
                    if (user.getPaymentType().equals("Delivery")) {
                        rbtn_delivery.setSelected(true);
                    } else {
                        rbtn_reservation.setSelected(true);
                    }

                }
            };
            return cell;
        }
    };

    @FXML
    void back(ActionEvent event) {
        stageManager.switchScene(FxmlView.CASHIER);

    }

    private void loadUserDetails() {
        cashierList.clear();
        cashierList.addAll(cashierService.findAll());
        paymentTable.setItems(cashierList);
    }
}
