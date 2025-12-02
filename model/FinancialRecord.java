package model;

import java.util.ArrayList;
import java.util.List;

// Demonstrates polymorphism with FinancialRecord hierarchy
public class FinancialRecordManager {
    private List<FinancialRecord> records = new ArrayList<>();
    
    public void addRecord(FinancialRecord record) {
        records.add(record);
    }
    
    public List<FinancialRecord> getAllRecords() {
        return new ArrayList<>(records);
    }
    
    // Polymorphism: works with any FinancialRecord subtype
    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        for (FinancialRecord record : records) {
            if (record instanceof Transaction) {
                transactions.add((Transaction) record);
            }
        }
        return transactions;
    }
    
    public List<Allowance> getAllowances() {
        List<Allowance> allowances = new ArrayList<>();
        for (FinancialRecord record : records) {
            if (record instanceof Allowance) {
                allowances.add((Allowance) record);
            }
        }
        return allowances;
    }
    
    // Calculate total money received from allowances
    public double getTotalAllowances() {
        return getAllowances().stream()
                .mapToDouble(Allowance::getWeeklyAmount)
                .sum();
    }
    
    // Calculate total money spent on transactions
    public double getTotalSpent() {
        return getTransactions().stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    
    public String generateSummary() {
        return "=== Financial Summary ===\n" +
            "Total Records: " + records.size() + "\n" +
            "Total Allowances: ₱" + getTotalAllowances() + "\n" +
            "Total Spent: ₱" + getTotalSpent() + "\n" +
            "Net: ₱" + (getTotalAllowances() - getTotalSpent());
    }
}