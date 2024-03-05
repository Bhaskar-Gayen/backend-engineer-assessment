package com.midas.app.workflows;
import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

public class CreateAccountWorkflowImpl implements CreateAccountWorkflow {
    private final AccountActivity accountActivity = Workflow.newActivityStub(
            AccountActivity.class,
            ActivityOptions.newBuilder().setTaskQueue(QUEUE_NAME).build()
    );

    @Override
    public Account createAccount(Account details) {
        Account account = accountActivity.saveAccount(details);
        account = accountActivity.createPaymentAccount(account);
        return account;
    }

    @Override
    public Account createStripeCustomer(Account details) {
        Account account = accountActivity.createStripeCustomer(details);
        return account;
    }
}
