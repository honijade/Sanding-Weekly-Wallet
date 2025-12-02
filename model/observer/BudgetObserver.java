package model.observer;

import model.WeeklyBudget;

// Observer Pattern for budget changes
public interface BudgetObserver {
    void onBudgetChanged(WeeklyBudget budget);
}