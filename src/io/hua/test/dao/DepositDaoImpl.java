package io.hua.test.dao;

import io.hua.test.domain.Deposit;

import java.text.NumberFormat;

public class DepositDaoImpl implements DepositDao {
    public DepositDaoImpl() {
        System.out.println("This creates an instance of the DepositDao class.");
    }

    public void createDeposit(Deposit deposit) {
        System.out.println(String.format("This creates a deposit of %s in the repository.",
            NumberFormat.getCurrencyInstance().format(deposit.getAmount())));
    }
}
