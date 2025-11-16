package controller;

import dao.DataPersister;
import java.time.LocalDate;
import model.Category;
import model.Transaction;
import model.WeeklyBudget;

public class WalletController {

    private WeeklyBudget weeklyBudget;
    private DataPersister persister;

    public WalletController(WeeklyBudget weeklyBudget, DataPersister persister) {
        this.weeklyBudget = weeklyBudget;
        this.persister = persister;
    }

    public void addTransaction(double amount, String categoryName) {
        Category category = new Category(categoryName);
        Transaction t = new Transaction(0, LocalDate.now(), amount, category);

        weeklyBudget.addTransaction(t);
        persister.saveTransaction(t);
    }

    public String generateReport() {
        return weeklyBudget.generateReport();
    }

    public WeeklyBudget getWeeklyBudget() {
        return weeklyBudget;
    }
}