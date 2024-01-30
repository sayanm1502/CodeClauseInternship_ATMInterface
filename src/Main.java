import java.util.ArrayList;
import java.util.Scanner;

class TransactionHistory {
    private ArrayList<String> history;

    public TransactionHistory() {
        this.history = new ArrayList<>();
    }

    public void addTransaction(String transaction) {
        history.add(transaction);
    }

    public void printTransactionHistory() {
        System.out.println("Transaction History:");
        for (String transaction : history) {
            System.out.println(transaction);
        }
        System.out.println();
    }
}

class Account {
    private String userId;
    private String userPin;
    private double balance;

    public Account(String userId, String userPin, double initialBalance) {
        this.userId = userId;
        this.userPin = userPin;
        this.balance = initialBalance;
    }

    public String getUserId() {
        return userId;
    }

    public boolean verifyPin(String enteredPin) {
        return userPin.equals(enteredPin);
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        } else {
            return false;
        }
    }

    public boolean transfer(Account recipient, double amount) {
        if (withdraw(amount)) {
            recipient.deposit(amount);
            return true;
        } else {
            return false;
        }
    }
}

class User {
    private String userId;
    private String userPin;
    private Account account;
    private TransactionHistory transactionHistory;

    public User(String userId, String userPin, double initialBalance) {
        this.userId = userId;
        this.userPin = userPin;
        this.account = new Account(userId, userPin, initialBalance);
        this.transactionHistory = new TransactionHistory();
    }

    public String getUserId() {
        return userId;
    }

    public boolean verifyPin(String enteredPin) {
        return userPin.equals(enteredPin);
    }

    public Account getAccount() {
        return account;
    }

    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }
}

public class Main {
    private static ArrayList<User> users;

    static {
        users = new ArrayList<>();
        users.add(new User("user1", "1234", 1000.0));
        users.add(new User("user2", "5678", 1500.0));
        users.add(new User("user3", "abcd", 2000.0));
        users.add(new User("user4", "efgh", 2500.0));
        users.add(new User("user5", "ijkl", 3000.0));
        users.add(new User("user6", "mnop", 3500.0));
        users.add(new User("user7", "qrst", 4000.0));
        users.add(new User("user8", "uvwx", 4500.0));
        users.add(new User("user9", "yz12", 5000.0));
        users.add(new User("user10", "3456", 5500.0));
    }

    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.start();
    }

    static class ATM {
        private User currentUser;

        public void start() {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine();

            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine();

            // Authenticate the user
            User user = authenticateUser(userId, pin);

            if (user != null) {
                System.out.println("Authentication successful. Welcome, " + user.getUserId() + "!");
                currentUser = user;
                showMenu(scanner);
            } else {
                System.out.println("Authentication failed. Exiting...");
            }
        }

        private User authenticateUser(String userId, String enteredPin) {
            for (User user : users) {
                if (user.getUserId().equals(userId) && user.verifyPin(enteredPin)) {
                    return user; 
                }
            }
            return null; 
        }

        private void showMenu(Scanner scanner) {
            int choice;
            do {
                System.out.println("1. Transaction History");
                System.out.println("2. Withdraw");
                System.out.println("3. Deposit");
                System.out.println("4. Transfer");
                System.out.println("5. Quit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        currentUser.getTransactionHistory().printTransactionHistory();
                        break;
                    case 2:
                        handleWithdraw(scanner);
                        break;
                    case 3:
                        handleDeposit(scanner);
                        break;
                    case 4:
                        handleTransfer(scanner);
                        break;
                    case 5:
                        System.out.println("Exiting. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 5);
        }

        private void handleWithdraw(Scanner scanner) {
            System.out.print("Enter withdrawal amount: ");
            double amount = scanner.nextDouble();

            if (currentUser.getAccount().withdraw(amount)) {
                String transaction = "Withdrawal: -$" + amount;
                currentUser.getTransactionHistory().addTransaction(transaction);
                System.out.println("Withdrawal successful. Remaining balance: $" + currentUser.getAccount().getBalance());
            } else {
                System.out.println("Withdrawal failed. Insufficient funds or invalid amount.");
            }
        }

        private void handleDeposit(Scanner scanner) {
            System.out.print("Enter deposit amount: ");
            double amount = scanner.nextDouble();

            currentUser.getAccount().deposit(amount);
            String transaction = "Deposit: +$" + amount;
            currentUser.getTransactionHistory().addTransaction(transaction);
            System.out.println("Deposit successful. New balance: $" + currentUser.getAccount().getBalance());
        }

        private void handleTransfer(Scanner scanner) {
            System.out.print("Enter recipient's User ID: ");
            String recipientId = scanner.next();

           
            User recipient = getUserById(recipientId);

            if (recipient != null) {
                System.out.print("Enter transfer amount: ");
                double amount = scanner.nextDouble();

                if (currentUser.getAccount().transfer(recipient.getAccount(), amount)) {
                    String transaction = "Transfer to " + recipientId + ": -$" + amount;
                    currentUser.getTransactionHistory().addTransaction(transaction);
                    System.out.println("Transfer successful. Remaining balance: $" + currentUser.getAccount().getBalance());
                } else {
                    System.out.println("Transfer failed. Insufficient funds or invalid amount.");
                }
            } else {
            System.out.println("Recipient not found. Transfer aborted.");
        }
    }
    }
}
