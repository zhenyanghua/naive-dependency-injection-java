package io.hua.test;

import io.hua.test.interfaces.DepositDao;

public class DepositDaoImpl implements DepositDao {
    public DepositDaoImpl() {
        System.out.println("This creates an instance of the DepositDao class.");
    }

    public void createDeposit(float deposit) {
        System.out.println(String.format("This creates a deposit of $%f in the repository.", deposit));
    }
}
