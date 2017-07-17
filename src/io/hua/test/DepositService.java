package io.hua.test;

public class DepositService {
    private DepositDao depositDao;

    public DepositService(DepositDao depositDao) {
        this.depositDao = depositDao;
        System.out.println("This creates an instance of the DepositService class. ");
    }

    public void createDeposit(float deposit) {
        depositDao.createDeposit(deposit);
    }
}
