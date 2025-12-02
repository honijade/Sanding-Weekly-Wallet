package view;

import controller.WalletController;
import dao.SQLiteManager;
import model.Transaction;
import model.WeeklyBudget;
import model.observer.BudgetObserver;
import model.strategy.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.application.Application;

import java.time.LocalDate;

// Implements Observer Pattern
public class MainView extends Application implements BudgetObserver {

    private WalletController controller;
    private Label lblMoneyLeft;
    private Label lblDailyBudget;
    private Label lblWarning;
    private TextArea historyBox;

    @Override
    public void start(Stage stage) {

        // Start with 1500 pesos
        WeeklyBudget budget = new WeeklyBudget(1500, LocalDate.now());
        
        // Register this view as observer
        budget.addObserver(this);
        
        SQLiteManager db = new SQLiteManager();
        controller = new WalletController(budget, db);

        // === MONEY LEFT DISPLAY ===
        lblMoneyLeft = new Label("Money Left: ₱" + budget.getRemainingBudget());
        lblMoneyLeft.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // === DAILY BUDGET DISPLAY ===
        lblDailyBudget = new Label("Daily Allowance: ₱" + budget.getSafeDailySpend());
        lblDailyBudget.setStyle("-fx-font-size: 14px;");

        // === WARNING LABEL ===
        lblWarning = new Label("");
        lblWarning.setStyle("-fx-font-size: 12px; -fx-text-fill: red; -fx-font-weight: bold;");

        VBox topInfo = new VBox(2, lblMoneyLeft, lblDailyBudget, lblWarning);
        topInfo.setAlignment(Pos.TOP_RIGHT);
        topInfo.setPadding(new Insets(10, 10, 0, 10));

        // === STRATEGY SELECTOR ===
        Label lblStrategy = new Label("Budget Strategy:");
        ComboBox<BudgetStrategy> strategyDropdown = new ComboBox<>();
        strategyDropdown.getItems().addAll(
            new ConservativeBudgetStrategy(),
            new BalancedBudgetStrategy(),
            new AggressiveBudgetStrategy()
        );
        
        // Custom display for strategies
        strategyDropdown.setCellFactory(lv -> new ListCell<BudgetStrategy>() {
            @Override
            protected void updateItem(BudgetStrategy item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getStrategyName());
            }
        });
        strategyDropdown.setButtonCell(new ListCell<BudgetStrategy>() {
            @Override
            protected void updateItem(BudgetStrategy item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Select Strategy" : item.getStrategyName());
            }
        });

        strategyDropdown.setOnAction(e -> {
            BudgetStrategy selected = strategyDropdown.getValue();
            if (selected != null) {
                controller.changeBudgetStrategy(selected);
                updateUI();
            }
        });

        // === TRANSACTION INPUTS ===
        Label lblAmount = new Label("Amount:");
        TextField txtAmount = new TextField();

        Label lblCategory = new Label("Category:");
        ComboBox<String> categoryDropdown = new ComboBox<>();
        categoryDropdown.getItems().addAll("Food", "Transport", "Other");

        // Extra text box for "Other"
        TextField txtOther = new TextField();
        txtOther.setPromptText("Specify category...");
        txtOther.setVisible(false);

        // Show/hide the "Other" textbox
        categoryDropdown.setOnAction(e -> {
            if ("Other".equals(categoryDropdown.getValue())) {
                txtOther.setVisible(true);
            } else {
                txtOther.setVisible(false);
            }
        });

        Button btnAdd = new Button("Add Transaction");

        // === HISTORY BOX ===
        historyBox = new TextArea();
        historyBox.setEditable(false);
        historyBox.setPrefHeight(150);
        historyBox.setStyle("-fx-font-family: 'Consolas';");

        // === ADD TRANSACTION LOGIC ===
        btnAdd.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(txtAmount.getText());
                
                // Validation: check if can afford
                if (!controller.canAfford(amount)) {
                    showAlert("Insufficient Budget", 
                        "This transaction would exceed your remaining budget of ₱" + 
                        controller.getWeeklyBudget().getRemainingBudget());
                    return;
                }
                
                String category;

                if ("Other".equals(categoryDropdown.getValue())) {
                    category = txtOther.getText().trim();
                    if (category.isEmpty()) {
                        historyBox.setText("Please specify the category.");
                        return;
                    }
                } else {
                    category = categoryDropdown.getValue();
                }

                controller.addTransaction(amount, category);
                // No need to call updateUI() - observer pattern handles it!

                txtAmount.clear();
                txtOther.clear();

            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter a valid number for amount.");
            }
        });

        VBox centerBox = new VBox(10,
                new Label("Budget Strategy:"),
                strategyDropdown,
                new Separator(),
                new Label("Add an Expense:"),
                lblAmount, txtAmount,
                lblCategory, categoryDropdown,
                txtOther,
                btnAdd,
                new Label("Transaction History:"),
                historyBox
        );
        centerBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(topInfo);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 360, 550);

        stage.setTitle("Weekly Wallet");
        stage.setScene(scene);
        stage.show();

        updateUI();
    }

    // Observer Pattern implementation
    @Override
    public void onBudgetChanged(WeeklyBudget budget) {
        updateUI();
    }

    private void updateUI() {
        WeeklyBudget wb = controller.getWeeklyBudget();

        lblMoneyLeft.setText("Money Left: ₱" + String.format("%.2f", wb.getRemainingBudget()));
        lblDailyBudget.setText("Daily Allowance: ₱" + String.format("%.2f", wb.getSafeDailySpend()));
        
        // Show warning if budget is low
        String warning = controller.getBudgetWarning();
        lblWarning.setText(warning);

        StringBuilder sb = new StringBuilder();
        for (Transaction t : wb.getTransactions()) {
            sb.append(String.format("-₱%-6.2f | %s%n",
                    t.getAmount(),
                    t.getCategory().getName()
            ));
        }
        historyBox.setText(sb.toString());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}