import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Bank {
    private Map<String, Account> accounts;
    private final String csvFileName = "accounts.csv";

    public Bank() {
        this.accounts = readAccountsFromCSV();
    }

    private Map<String, Account> readAccountsFromCSV() {
        Map<String, Account> accounts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String accountNumber = data[0];
                String accountHolder = data[1];
                double balance = Double.parseDouble(data[2]);
                Account account = new Account(accountNumber, accountHolder, balance);
                accounts.put(accountNumber, account);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public void writeAccountsToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName))) {
            for (Account account : accounts.values()) {
                String line = account.getAccountNumber() + "," + account.getAccountHolder() + "," + account.getBalance();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createAccount(String accountNumber, String name, double initialBalance) {
        if (!accounts.containsKey(accountNumber)) {
            Account account = new Account(accountNumber, name, initialBalance);
            accounts.put(accountNumber, account);
            writeAccountsToCSV();
            System.out.println("Account created successfully.");
        } else {
            System.out.println("Account already exists with this account number.");
        }
    }

    public void deposit(String accountNumber, double amount) {
        Account account = accounts.get(accountNumber);
        if (account != null) {
            account.deposit(amount);
            writeAccountsToCSV(); 
            System.out.println("Deposit successful. New balance: $" + account.getBalance());
        } else {
            System.out.println("Account not found.");
        }
    }

    public void withdraw(String accountNumber, double amount) {
        Account account = accounts.get(accountNumber);
        if (account != null) {
            if (account.withdraw(amount)) {
                writeAccountsToCSV(); 
                System.out.println("Withdrawal successful. New balance: $" + account.getBalance());
            } else {
                System.out.println("Insufficient funds.");
            }
        } else {
            System.out.println("Account not found.");
        }
    }

    public void checkBalance(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account != null) {
            System.out.println("Current balance: $" + account.getBalance());
        } else {
            System.out.println("Account not found.");
        }
    }
}

class Account {
    private String accountNumber;
    private String accountHolder;
    private double balance;

    public Account(String accountNumber, String accountHolder, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            return true;
        } else {
            return false;
        }
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }
}

public class BankingApplication {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter account number: ");
                    String accountNumber = scanner.nextLine();
                    System.out.print("Enter account holder name: ");
                    String accountHolder = scanner.nextLine();
                    System.out.print("Enter initial balance: $");
                    double initialBalance = scanner.nextDouble();
                    bank.createAccount(accountNumber, accountHolder, initialBalance);
                    break;

                case 2:
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.nextLine();
                    System.out.print("Enter deposit amount: $");
                    double depositAmount = scanner.nextDouble();
                    bank.deposit(accountNumber, depositAmount);
                    break;

                case 3:
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.nextLine();
                    System.out.print("Enter withdrawal amount: $");
                    double withdrawalAmount = scanner.nextDouble();
                    bank.withdraw(accountNumber, withdrawalAmount);
                    break;

                case 4:
                    System.out.print("Enter account number: ");
                    accountNumber = scanner.nextLine();
                    bank.checkBalance(accountNumber);
                    break;

                case 5:
                    System.out.println("Exiting the banking application. Thank you!");
                    bank.writeAccountsToCSV();
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
    }
}
