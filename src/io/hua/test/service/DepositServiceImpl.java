package io.hua.test.service;

import io.hua.test.domain.Deposit;
import io.hua.test.dao.DepositDao;

public class DepositServiceImpl implements DepositService {
    private DepositDao depositDao;

    public DepositServiceImpl(DepositDao depositDao) {
        this.depositDao = depositDao;
        System.out.println("This creates an instance of the DepositService class. ");
    }

    public void createDeposit(Deposit deposit) {
        depositDao.createDeposit(deposit);
    }
}
