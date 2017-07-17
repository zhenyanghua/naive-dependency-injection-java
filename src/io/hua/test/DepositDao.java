package io.hua.test;

public class DepositDao {
    public DepositDao() {
        System.out.println("This creates an instance of the DepositDao class.");
    }

    public void createDeposit(float deposit) {
        System.out.println(String.format("This creates a deposit of $%f in the repository.", deposit));
    }
}
