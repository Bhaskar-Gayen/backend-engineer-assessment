package com.midas.app.controllers;

import com.midas.app.models.Account;
import com.midas.app.models.ProviderType;
import com.midas.app.repositories.AccountRepository;
import com.midas.generated.model.AccountDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserController {

    @PostMapping("/signup")
    public String signUp(@RequestBody AccountDto accountDto) {
        // Process the signup request
        accountDto.setProviderType(ProviderType.STRIPE);
        // Generate and set the providerId
        String providerId = generateProviderId();
        accountDto.setProviderId(providerId);
        // Save the account details
        saveAccount(accountDto);
        return "User signed up successfully";
    }

    private String generateProviderId() {
        // Generate a unique providerId here (e.g., using UUID)
        return UUID.randomUUID().toString();
    }

    private void saveAccount(AccountDto accountDto) {
        // Convert AccountDto to Account model and save to database
        AccountRepository accountRepository = null;
        accountRepository.save(Account.builder()
                .firstName(accountDto.getFirstName())
                .lastName(accountDto.getLastName())
                .email(accountDto.getEmail())
                .providerType(ProviderType.STRIPE)
                .providerId(accountDto.getProviderId())
                .build());
    }

}

