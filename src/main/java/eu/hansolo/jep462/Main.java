package eu.hansolo.jep462;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Supplier;


public class Main {
    record Account(String name) {}
    record AccountDetails(LocalDate dateOfCreation, String userName) {}
    record UserDetails(String firstName, String lastName, LocalDate birthday) {}
    record Response(AccountDetails accountDetails, List<Account> linkedAccounts, UserDetails userDetails) {}

    ExecutorService executorService = Executors.newFixedThreadPool(4);

    public Main() {

        System.out.println("---------- Old Approach ----------");
        try {
            Response response = fetchOld(815);
            System.out.println(response);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println();

        System.out.println("---------- New Approach ----------");
        Response response = fetchNew(815);
        System.out.println(response);

        executorService.shutdown();
    }


    public Response fetchOld(long id) throws ExecutionException, InterruptedException {
        Future<AccountDetails> accountDetailsFuture = executorService.submit(() -> getAccountDetails(id));
        Future<List<Account>>  linkedAccountsFuture = executorService.submit(() -> fetchLinkedAccounts(id));
        Future<UserDetails>    userDetailsFuture    = executorService.submit(() -> fetchUserDetails(id));

        System.out.println("Start threads independently");
        return new Response(accountDetailsFuture.get(), linkedAccountsFuture.get(), userDetailsFuture.get());
    }

    public Response fetchNew(long id) {
        try ( var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Supplier<AccountDetails> accountDetailsFuture = scope.fork(() -> getAccountDetails(id));
            Supplier<List<Account>>  linkedAccountsFuture = scope.fork(() -> fetchLinkedAccounts(id));
            Supplier<UserDetails>    userDetailsFuture    = scope.fork(() -> fetchUserDetails(id));

            System.out.println("Joining all threads...");
            scope.join();                                // Join all subtasks
            scope.throwIfFailed(RuntimeException::new);  //Handle error when any subtask fails

            System.out.println("Response is received from all workers...");
            return new Response(accountDetailsFuture.get(), linkedAccountsFuture.get(), userDetailsFuture.get()); //The subtasks have completed by now so process the result
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public AccountDetails getAccountDetails(long id) throws InterruptedException {
        Thread.sleep(3000L);
        System.out.println("Retrieved account details");
        return new AccountDetails(LocalDate.of(1984, 07, 01), "hansolo");
    }

    public List<Account> fetchLinkedAccounts(long id) throws InterruptedException {
        Thread.sleep(2000L);
        System.out.println("Retrieved linked accounts");
        return List.of(new Account("alliance"), new Account("pilots"));
    }

    public UserDetails fetchUserDetails(long id) throws InterruptedException {
        Thread.sleep(1000L);
        System.out.println("Retrieved UserDetails");
        return new UserDetails("Han", "Solo", LocalDate.of(1969, 12, 03));
    }


    public static void main(String[] args) {
        new Main();
    }
}
