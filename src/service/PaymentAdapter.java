package service;

import service.PaymentService;

public class PaymentAdapter implements PaymentService {
    public void processPayment(String account, double amount) {
        System.out.println("Processing payment of $" + amount + " for account: " + account);
    }
}
