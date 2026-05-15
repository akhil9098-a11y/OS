import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class App extends JFrame {
    private final PaymentProcessor processor = new PaymentProcessor();
    private final JTextField createNameField = new JTextField(15);
    private final JTextField addCustomerIdField = new JTextField(10);
    private final JTextField addAmountField = new JTextField(10);
    private final JTextField paymentPayerIdField = new JTextField(10);
    private final JTextField paymentPayeeField = new JTextField(10);
    private final JTextField paymentAmountField = new JTextField(10);
    private final JTextField detailsCustomerIdField = new JTextField(10);
    private final JTextArea outputArea = new JTextArea(10, 40);
    private final DefaultListModel<String> transactionListModel = new DefaultListModel<>();

    public App() {
        super("Java Payment App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        add(createMainPanel(), BorderLayout.CENTER);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        appendOutput("Welcome to the Java Payment App GUI.");
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(createOperationsPanel(), BorderLayout.CENTER);
        mainPanel.add(createTransactionPanel(), BorderLayout.EAST);
        return mainPanel;
    }

    private JPanel createOperationsPanel() {
        JPanel operationsPanel = new JPanel();
        operationsPanel.setLayout(new BoxLayout(operationsPanel, BoxLayout.Y_AXIS));
        operationsPanel.add(createAccountPanel());
        operationsPanel.add(Box.createVerticalStrut(10));
        operationsPanel.add(addFundsPanel());
        operationsPanel.add(Box.createVerticalStrut(10));
        operationsPanel.add(makePaymentPanel());
        operationsPanel.add(Box.createVerticalStrut(10));
        operationsPanel.add(viewDetailsPanel());
        return operationsPanel;
    }

    private JPanel createAccountPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Create Customer"));
        panel.add(new JLabel("Name:"));
        panel.add(createNameField);
        JButton button = new JButton("Create");
        button.addActionListener(e -> createCustomer());
        panel.add(button);
        return panel;
    }

    private JPanel addFundsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Add Funds"));
        panel.add(new JLabel("Customer ID:"));
        panel.add(addCustomerIdField);
        panel.add(new JLabel("Amount:"));
        panel.add(addAmountField);
        JButton button = new JButton("Add Funds");
        button.addActionListener(e -> addFunds());
        panel.add(button);
        return panel;
    }

    private JPanel makePaymentPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Make Payment"));
        panel.add(new JLabel("Payer ID:"));
        panel.add(paymentPayerIdField);
        panel.add(new JLabel("Payee:"));
        panel.add(paymentPayeeField);
        panel.add(new JLabel("Amount:"));
        panel.add(paymentAmountField);
        JButton button = new JButton("Pay");
        button.addActionListener(e -> makePayment());
        panel.add(button);
        return panel;
    }

    private JPanel viewDetailsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        panel.add(new JLabel("Customer ID:"));
        panel.add(detailsCustomerIdField);
        JButton button = new JButton("Show");
        button.addActionListener(e -> showCustomerDetails());
        panel.add(button);
        return panel;
    }

    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Transactions"));
        JList<String> transactionList = new JList<>(transactionListModel);
        transactionList.setVisibleRowCount(15);
        panel.add(new JScrollPane(transactionList), BorderLayout.CENTER);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshTransactionHistory());
        panel.add(refreshButton, BorderLayout.SOUTH);
        return panel;
    }

    private void createCustomer() {
        String name = createNameField.getText().trim();
        if (name.isEmpty()) {
            appendOutput("Error: Name cannot be empty.");
            return;
        }
        Customer customer = processor.createCustomer(name);
        appendOutput("Created customer: " + customer.getId() + " - " + customer.getName());
        createNameField.setText("");
        refreshTransactionHistory();
    }

    private void addFunds() {
        String customerId = addCustomerIdField.getText().trim();
        double amount = parseAmount(addAmountField.getText());
        if (customerId.isEmpty() || amount <= 0) {
            appendOutput("Error: Please enter a valid customer ID and amount.");
            return;
        }
        boolean success = processor.addFunds(customerId, amount);
        if (success) {
            appendOutput("Funds added successfully to customer " + customerId + ".");
        } else {
            appendOutput("Error: Customer not found.");
        }
        addAmountField.setText("");
        refreshTransactionHistory();
    }

    private void makePayment() {
        String payerId = paymentPayerIdField.getText().trim();
        String payeeName = paymentPayeeField.getText().trim();
        double amount = parseAmount(paymentAmountField.getText());
        if (payerId.isEmpty() || payeeName.isEmpty() || amount <= 0) {
            appendOutput("Error: Enter payer ID, payee name, and a valid amount.");
            return;
        }
        PaymentResult result = processor.makePayment(payerId, payeeName, amount);
        appendOutput(result.message());
        refreshTransactionHistory();
    }

    private void showCustomerDetails() {
        String customerId = detailsCustomerIdField.getText().trim();
        if (customerId.isEmpty()) {
            appendOutput("Error: Enter a customer ID.");
            return;
        }
        Optional<Customer> customer = processor.findCustomer(customerId);
        if (customer.isPresent()) {
            appendOutput(customer.get().toString());
        } else {
            appendOutput("Customer not found.");
        }
    }

    private void refreshTransactionHistory() {
        transactionListModel.clear();
        if (processor.getTransactions().isEmpty()) {
            transactionListModel.addElement("No transactions yet.");
            return;
        }
        processor.getTransactions().forEach(t -> transactionListModel.addElement(t.toString()));
    }

    private void appendOutput(String message) {
        outputArea.append(message + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    private double parseAmount(String input) {
        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }

    static class PaymentProcessor {
        private final List<Customer> customers = new ArrayList<>();
        private final List<Transaction> transactions = new ArrayList<>();
        private int nextCustomerId = 1;

        public Customer createCustomer(String name) {
            Customer customer = new Customer(String.valueOf(nextCustomerId++), name, 0.0);
            customers.add(customer);
            return customer;
        }

        public boolean addFunds(String customerId, double amount) {
            Optional<Customer> customer = findCustomer(customerId);
            customer.ifPresent(c -> c.setBalance(c.getBalance() + amount));
            customer.ifPresent(c -> transactions.add(new Transaction(customerId, "FUNDING", amount, "Deposit funds")));
            return customer.isPresent();
        }

        public PaymentResult makePayment(String payerId, String payeeName, double amount) {
            Optional<Customer> payerOpt = findCustomer(payerId);
            if (payerOpt.isEmpty()) {
                return new PaymentResult(false, "Error: Payer customer not found.");
            }
            Customer payer = payerOpt.get();
            if (payer.getBalance() < amount) {
                return new PaymentResult(false, "Error: Insufficient balance.");
            }
            payer.setBalance(payer.getBalance() - amount);
            transactions.add(new Transaction(payerId, "PAYMENT", amount, "Paid " + payeeName));
            return new PaymentResult(true, "Payment successful. Remaining balance: " + String.format("%.2f", payer.getBalance()));
        }

        public Optional<Customer> findCustomer(String customerId) {
            return customers.stream().filter(c -> c.getId().equals(customerId)).findFirst();
        }

        public List<Transaction> getTransactions() {
            return new ArrayList<>(transactions);
        }
    }

    static class Customer {
        private final String id;
        private final String name;
        private double balance;

        public Customer(String id, String name, double balance) {
            this.id = id;
            this.name = name;
            this.balance = balance;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        @Override
        public String toString() {
            return "Customer ID=" + id + ", Name='" + name + "', Balance=" + String.format("%.2f", balance);
        }
    }

    static class Transaction {
        private final String customerId;
        private final String type;
        private final double amount;
        private final String description;

        public Transaction(String customerId, String type, double amount, String description) {
            this.customerId = customerId;
            this.type = type;
            this.amount = amount;
            this.description = description;
        }

        @Override
        public String toString() {
            return String.format("[%s] Customer=%s Amount=%.2f Desc='%s'", type, customerId, amount, description);
        }
    }

    record PaymentResult(boolean success, String message) {}
}
