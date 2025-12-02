package model.strategy;

import model.WeeklyBudget;

// Strategy Pattern for different budget calculation methods
public interface BudgetStrategy {
    double calculateSafeDailySpend(WeeklyBudget budget);
    String getStrategyName();
}

// Conservative strategy - leaves a buffer
public class ConservativeBudgetStrategy implements BudgetStrategy {
    @Override
    public double calculateSafeDailySpend(WeeklyBudget budget) {
        int daysLeft = 7 - java.time.LocalDate.now().getDayOfWeek().getValue();
        if (daysLeft <= 0) return 0;
        // Keep 10% buffer
        return (budget.getRemainingBudget() * 0.9) / daysLeft;
    }
    
    @Override
    public String getStrategyName() {
        return "Conservative (90% spending)";
    }
}

// Aggressive strategy - spend it all
public class AggressiveBudgetStrategy implements BudgetStrategy {
    @Override
    public double calculateSafeDailySpend(WeeklyBudget budget) {
        int daysLeft = 7 - java.time.LocalDate.now().getDayOfWeek().getValue();
        return daysLeft > 0 ? budget.getRemainingBudget() / daysLeft : 0;
    }
    
    @Override
    public String getStrategyName() {
        return "Aggressive (100% spending)";
    }
}

// Balanced strategy - moderate buffer
public class BalancedBudgetStrategy implements BudgetStrategy {
    @Override
    public double calculateSafeDailySpend(WeeklyBudget budget) {
        int daysLeft = 7 - java.time.LocalDate.now().getDayOfWeek().getValue();
        if (daysLeft <= 0) return 0;
        // Keep 5% buffer
        return (budget.getRemainingBudget() * 0.95) / daysLeft;
    }
    
    @Override
    public String getStrategyName() {
        return "Balanced (95% spending)";
    }
}