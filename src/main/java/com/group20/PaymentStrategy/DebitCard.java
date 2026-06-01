package com.group20.PaymentStrategy;

import com.group20.Strategy.Payment;

public class DebitCard implements Payment {

	@Override
	public void pay() {
		System.out.println("Paid with DebitCard");
		
	}
	

}
