package com.midas.app.services;

import com.midas.app.models.Account;
import com.midas.app.repositories.AccountRepository;
import com.midas.app.workflows.CreateAccountWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.workflow.Workflow;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
  private final Logger logger = Workflow.getLogger(AccountServiceImpl.class);

  private final WorkflowClient workflowClient;

  private final AccountRepository accountRepository;

  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createAccount(Account details) {
    var options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(CreateAccountWorkflow.QUEUE_NAME)
            .setWorkflowId(details.getEmail())
            .build();

    logger.info("initiating workflow to create account for email: {}", details.getEmail());

    var workflow = workflowClient.newWorkflowStub(CreateAccountWorkflow.class, options);
    Account account = workflow.createAccount(details);
    workflow.createStripeCustomer(details);
    return account;
  }

  /**
   * getAccounts returns a list of accounts.
   *
   * @return List<Account>
   */
  @Override
  public List<Account> getAccounts() {
    return accountRepository.findAll();
  }

  @Override
  public Account updateAccount(String accountId, Account updatedDetails) {
    // Fetch the account from the repository
    Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found"));

    // Update the account details
    account.setFirstName(updatedDetails.getFirstName());
    account.setLastName(updatedDetails.getLastName());
    account.setEmail(updatedDetails.getEmail());

    // Save the updated account
    return accountRepository.save(account);
  }

  @Override
  public Account getAccountById(String accountId) {
    return accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found"));
  }

}
