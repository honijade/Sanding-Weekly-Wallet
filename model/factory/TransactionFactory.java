package model.factory;

import model.Category;
import model.Transaction;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

// Factory Pattern for creating transactions
public class TransactionFactory {
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    
    public static Transaction createTransaction(double amount, String categoryName) {
        Category category = new Category(categoryName);
        int id = idGenerator.getAndIncrement();
        return new Transaction(id, LocalDate.now(), amount, category);
    }
    
    public static Transaction createTransaction(double amount, Category category) {
        int id = idGenerator.getAndIncrement();
        return new Transaction(id, LocalDate.now(), amount, category);
    }
}