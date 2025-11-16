package dao;

import model.Transaction;
import java.util.List;

public interface DataPersister {

    void saveTransaction(Transaction t);

    List<Transaction> loadAllTransactions();
}
