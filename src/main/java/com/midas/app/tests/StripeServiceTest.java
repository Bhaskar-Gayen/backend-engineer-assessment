package com.midas.app.tests;

import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.assertEquals;

import com.midas.app.models.Account;
import com.midas.app.repositories.AccountRepository;
import com.midas.app.services.AccountServiceImpl;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import org.junit.jupiter.api.Test;

public class StripeServiceTest {

    @Test
    void testCreateStripeCustomer() throws StripeException {
        // Mock account
        Account account = new Account();
        account.setEmail("test@example.com");
        account.setFirstName("John");
        account.setLastName("Doe");

        // Mock CustomerCreateParams
        CustomerCreateParams.Builder createParamsBuilder = mock(CustomerCreateParams.Builder.class);
        CustomerCreateParams createParams = mock(CustomerCreateParams.class);
        when(createParamsBuilder.setEmail("test@example.com")).thenReturn(createParamsBuilder);
        when(createParamsBuilder.setMetadata(any())).thenReturn(createParamsBuilder);
        when(createParamsBuilder.build()).thenReturn(createParams);

        // Mock Customer
        Customer customer = mock(Customer.class);
        when(Customer.create(createParams)).thenReturn(customer);

        // Create StripeService and call createStripeCustomer
        StripeService stripeService = new StripeService();
        stripeService.createStripeCustomer(account);

        // Verify that Customer.create was called with the correct parameters
        verify(Customer.class, times(1)).create(createParams);
    }

    @Test
    void testUpdateAccount() {
        // Mock account
        Account account = new Account();
        account.setId(1L);
        account.setEmail("test@example.com");
        account.setFirstName("John");
        account.setLastName("Doe");

        // Mock AccountRepository
        AccountRepository accountRepository = mock(AccountRepository.class);
        when(accountRepository.save(any())).thenReturn(account);

        // Create AccountServiceImpl and call updateAccount
        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository);
        Account updatedAccount = accountService.updateAccount(String.valueOf(account.getId()), account);

        // Verify that the account was updated correctly
        assertEquals(account.getId(), updatedAccount.getId());
        assertEquals(account.getEmail(), updatedAccount.getEmail());
        assertEquals(account.getFirstName(), updatedAccount.getFirstName());
        assertEquals(account.getLastName(), updatedAccount.getLastName());
    }
}
