package com.midas.app.activities;

import com.midas.app.models.Account;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import io.temporal.activity.ActivityMethod;

import java.util.Map;

public class AccountActivityImpl implements AccountActivity {
    @Override
    public Account saveAccount(Account account) {
        // Implement saveAccount logic here
        return account;
    }

    @Override
    public Account createPaymentAccount(Account account) {
        // Implement createPaymentAccount logic here
        return account;
    }

    @Override
    @ActivityMethod
    public Account createStripeCustomer(Account account) {
        try {
            Stripe.apiKey = "sk_test_tR3PYbcVNZZ796tH88S4VQ2u"; // Stripe secret key
            Customer customer = Customer.create(
                    new CustomerCreateParams.Address.Builder().Builder()
                            .setEmail(account.getEmail())
                            .setMetadata(Map.of("first_name", account.getFirstName(), "last_name", account.getLastName()))
                            .build()
            );
            account.setStripeCustomerId(customer.getId());
        } catch (StripeException e) {
            // Handle Stripe API exception
            e.printStackTrace();
        }
        return account;
    }
}
