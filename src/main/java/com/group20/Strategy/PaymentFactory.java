package com.group20.Strategy;

import com.group20.PaymentStrategy.CreditCard;
import com.group20.PaymentStrategy.DebitCard;
import com.group20.PaymentStrategy.PayPal;

public class PaymentFactory {

    public static Payment getPaymentStrategy(String paymentType) {
        if ("CreditCard".equalsIgnoreCase(paymentType)) {
            return new CreditCard();
        } else if ("DebitCard".equalsIgnoreCase(paymentType)) {
            return new DebitCard();
        } else if ("PayPal".equalsIgnoreCase(paymentType)) {
            return new PayPal();
        } else {
            throw new IllegalArgumentException("Invalid payment type: " + paymentType);
        }
    }
}
