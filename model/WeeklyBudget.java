package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeeklyBudget implements Reportable {

    private double totalBudget;
    private LocalDate weekStart;
    private List<Transaction> transactions = new ArrayList<>();

    public WeeklyBudget(double totalBudget, LocalDate weekStart) {
        this.totalBudget = totalBudget;
        this.weekStart = weekStart;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public double getTotalSpent() {
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getRemainingBudget() {
        return totalBudget - getTotalSpent();
    }

    public double getSafeDailySpend() {
        int daysLeft = 7 - LocalDate.now().getDayOfWeek().getValue();
        return daysLeft > 0 ? getRemainingBudget() / daysLeft : 0;
    }

    public List<Transaction> getTransactions() {
    return transactions;
    }


    @Override
    public String generateReport() {
        return "=== Weekly Budget Report ===\n" +
               "Total Budget: " + totalBudget + "\n" +
               "Total Spent: " + getTotalSpent() + "\n" +
               "Remaining: " + getRemainingBudget() + "\n" +
               "Safe Daily Spend: " + getSafeDailySpend();
    }
}
