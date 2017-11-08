package io.hua.test.dao;

import io.hua.test.domain.Deposit;

import java.text.NumberFormat;

public class DepositDaoAnotherImpl implements DepositDao {
    public DepositDaoAnotherImpl() {
        System.out.println("This creates an instance of the DepositDao class, but this is another implementation.");
    }

    public void createDeposit(Deposit deposit) {
        System.out.println(String.format("This creates a deposit of %s in the repository, from another implementation.",
            NumberFormat.getCurrencyInstance().format(deposit.getAmount())));
    }
}
