package io.hua.test.domain;

public class Deposit {
    private float amount;

    public Deposit() {
        System.out.println("This creates an instance of the Deposit class.");
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(final float amount) {
        this.amount = amount;
    }
}
