package dao;

import model.Transaction;
import java.util.ArrayList;
import java.util.List;

public class SQLiteManager implements DataPersister {

    @Override
    public void saveTransaction(Transaction t) {
        System.out.println("Saving transaction to database... (placeholder)");
    }

    @Override
    public List<Transaction> loadAllTransactions() {
        return new ArrayList<>();
    }
}
