package model;

import java.time.LocalDate;

public class Transaction extends FinancialRecord {
    private double amount;
    private Category category;

    public Transaction(int id, LocalDate date, double amount, Category category) {
        super(id, date);
        this.amount = amount;
        this.category = category;
    }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
