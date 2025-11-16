package model;

import java.time.LocalDate;

public abstract class FinancialRecord {
    protected int id;
    protected LocalDate date;

    public FinancialRecord(int id, LocalDate date) {
        this.id = id;
        this.date = date;
    }

    public int getId() { return id; }
    public LocalDate getDate() { return date; }
}
