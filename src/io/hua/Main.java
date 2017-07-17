package io.hua;

import io.hua.test.DepositService;

import java.lang.reflect.InvocationTargetException;

public class Main {

    private static final String PKG_DEPOSIT_DAO = "io.hua.test.DepositDao";
    private static final String PKG_DEPOSIT_SERVICE = "io.hua.test.DepositService";

    public static void main(String[] args) {

        ClassDetail[] classDetails = new ClassDetail[] {
            new ClassDetail(PKG_DEPOSIT_DAO),
            new ClassDetail(PKG_DEPOSIT_SERVICE, new String[] {PKG_DEPOSIT_DAO})
        };

        try {
            Container context = Container.getContext(classDetails);

            DepositService depositService_1 = context.getInstance(DepositService.class);
            depositService_1.createDeposit(100);

            DepositService depositService_2 = context.getInstance(DepositService.class);
            depositService_2.createDeposit(60);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
