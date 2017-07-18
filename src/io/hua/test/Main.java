package io.hua.test;

import io.hua.DI.ApplicationContext;
import io.hua.DI.ClassDetail;
import io.hua.DI.Scope;
import io.hua.test.domain.Deposit;
import io.hua.test.service.DepositService;

public class Main {

    private static final String PKG_DEPOSIT_DAO = "io.hua.test.dao.DepositDaoImpl";
    private static final String PKG_DEPOSIT_SERVICE = "io.hua.test.service.DepositServiceImpl";
    private static final String PKG_DEPOSIT = "io.hua.test.domain.Deposit";

    public static void main(String[] args) {

        // Create an array of ClassDetail object
        ClassDetail[] classDetails = new ClassDetail[] {
            new ClassDetail(PKG_DEPOSIT_DAO),
            new ClassDetail(PKG_DEPOSIT_SERVICE, new String[] {PKG_DEPOSIT_DAO}),
            new ClassDetail(PKG_DEPOSIT, Scope.Prototype)
        };

        try {
            // Get the context singleton through the factory method.
            ApplicationContext context = ApplicationContext.getContext(classDetails);

            // Call getInstance method
            DepositService depositService_1 = context.getInstance(DepositService.class);
            Deposit deposit_1 = context.getInstance(Deposit.class);
            deposit_1.setAmount(100);
            depositService_1.createDeposit(deposit_1);

            // Call getInstance method
            DepositService depositService_2 = context.getInstance(DepositService.class);
            Deposit deposit_2 = context.getInstance(Deposit.class);
            deposit_2.setAmount(60);
            depositService_2.createDeposit(deposit_2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
