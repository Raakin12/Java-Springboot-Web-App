package com.group20.PaymentStrategy;

import com.group20.Strategy.Payment;

public class PayPal implements Payment  {

	@Override
	public void pay() {
		System.out.println("Paid with PayPal");
		
	}

}
