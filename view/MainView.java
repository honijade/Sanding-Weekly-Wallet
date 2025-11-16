package view;

import controller.WalletController;
import dao.SQLiteManager;
import model.Category;
import model.Transaction;
import model.WeeklyBudget;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.application.Application;

import java.time.LocalDate;

public class MainView extends Application {

    private WalletController controller;
    private Label lblMoneyLeft;
    private Label lblDailyBudget;
    private TextArea historyBox;

    @Override
    public void start(Stage stage) {

        // Start with 1500 pesos
        WeeklyBudget budget = new WeeklyBudget(1500, LocalDate.now());
        SQLiteManager db = new SQLiteManager();
        controller = new WalletController(budget, db);

        // === MONEY LEFT DISPLAY ===
        lblMoneyLeft = new Label("Money Left: ₱" + budget.getRemainingBudget());
        lblMoneyLeft.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // === DAILY BUDGET DISPLAY ===
        lblDailyBudget = new Label("Daily Allowance: ₱" + budget.getSafeDailySpend());
        lblDailyBudget.setStyle("-fx-font-size: 14px;");

        VBox topInfo = new VBox(2, lblMoneyLeft, lblDailyBudget);
        topInfo.setAlignment(Pos.TOP_RIGHT);
        topInfo.setPadding(new Insets(10, 10, 0, 10));

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
                updateUI();

                txtAmount.clear();
                txtOther.clear();

            } catch (Exception ex) {
                historyBox.setText("Invalid input!");
            }
        });

        VBox centerBox = new VBox(10,
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

        Scene scene = new Scene(root, 360, 500);

        stage.setTitle("Weekly Wallet");
        stage.setScene(scene);
        stage.show();

        updateUI();
    }

    private void updateUI() {
        WeeklyBudget wb = controller.getWeeklyBudget();

        lblMoneyLeft.setText("Money Left: ₱" + wb.getRemainingBudget());
        lblDailyBudget.setText("Daily Allowance: ₱" + wb.getSafeDailySpend());

        StringBuilder sb = new StringBuilder();
        for (Transaction t : wb.getTransactions()) {
            sb.append(String.format("-%-6.2f | %s%n",
                    t.getAmount(),
                    t.getCategory().getName()
            ));
        }
        historyBox.setText(sb.toString());
    }

    public static void main(String[] args) {
        launch();
    }
}
