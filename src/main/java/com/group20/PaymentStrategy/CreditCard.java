package com.group20.PaymentStrategy;

import com.group20.Strategy.Payment;

public class CreditCard implements Payment {

	@Override
	public void pay() {
		System.out.println("Paid with CreditCard");
		
	}
	

}
