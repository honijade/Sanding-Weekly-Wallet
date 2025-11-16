package model;

import java.time.LocalDate;

public class Allowance extends FinancialRecord {
    private double weeklyAmount;

    public Allowance(int id, LocalDate date, double weeklyAmount) {
        super(id, date);
        this.weeklyAmount = weeklyAmount;
    }

    public double getWeeklyAmount() { return weeklyAmount; }
}
