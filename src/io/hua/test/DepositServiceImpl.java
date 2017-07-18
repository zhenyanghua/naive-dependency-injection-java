package io.hua.test;

import io.hua.test.interfaces.DepositDao;
import io.hua.test.interfaces.DepositService;

public class DepositServiceImpl implements DepositService {
    private DepositDao depositDao;

    public DepositServiceImpl(DepositDao depositDao) {
        this.depositDao = depositDao;
        System.out.println("This creates an instance of the DepositService class. ");
    }

    public void createDeposit(float deposit) {
        depositDao.createDeposit(deposit);
    }
}
