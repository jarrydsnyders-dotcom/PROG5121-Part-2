//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Scanner;
import java.util.Random;
import java.util.regex.Pattern;

class QuickChatApp {

    public static class Login {
        private String firstName;
        private String lastName;

        private boolean isRegistered = false;
        private String registeredUsername;
        private String registeredPassword;
        private String registeredCell;

        public Login() {}

        public Login(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public boolean checkUserName(String username) {
            if (username == null) return false;
            boolean hasUnderscore = username.contains("_");
            boolean correctLength = username.length() <= 5;
            return hasUnderscore && correctLength;
        }

        public boolean checkPasswordComplexity(String password) {
            if (password == null) return false;
            boolean minLength = password.length() >= 8;
            boolean hasCapital = !password.equals(password.toLowerCase());
            boolean hasNumber = Pattern.compile("[0-9]").matcher(password).find();
            boolean hasSpecial = Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();
            return minLength && hasCapital && hasNumber && hasSpecial;
        }

        public boolean checkCellPhoneNumber(String cellPhoneNumber) {
            if (cellPhoneNumber == null) return false;
            String regex = "^\\+\\d{1,3}\\d{1,10}$";
            return Pattern.matches(regex, cellPhoneNumber);
        }

        public String registerUser(String username, String password, String cellPhoneNumber, String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;

            if (!checkUserName(username)) {
                return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
            }

            if (!checkPasswordComplexity(password)) {
                return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
            }

            if (!checkCellPhoneNumber(cellPhoneNumber)) {
                return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
            }

            this.registeredUsername = username;
            this.registeredPassword = password;
            this.registeredCell = cellPhoneNumber;
            this.isRegistered = true;

            return "Username successfully captured.\nPassword successfully captured.\nCell number successfully captured.";
        }

        public boolean loginUser(String enteredUsername, String enteredPassword) {
            if (!isRegistered) return false;
            return enteredUsername.equals(registeredUsername) && enteredPassword.equals(registeredPassword);
        }

        public String returnLoginStatus(boolean loginSuccess) {
            if (loginSuccess) {
                return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
            } else {
                return "Username or password incorrect, please try again.";
            }
        }
    }

    public static class Message {
        private String messageID;
        private int numSentMessages;
        private String recipient;
        private String messageText;
        private String messageHash;

        public Message() {
            this.messageID = generateMessageID();
        }

        public Message(String recipient, String messageText, int sentCount) {
            this.messageID = generateMessageID();
            this.recipient = recipient;
            this.messageText = messageText;
            this.numSentMessages = sentCount;
            this.messageHash = createMessageHash();
        }

        private String generateMessageID() {
            Random rand = new Random();
            long id = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
            return String.valueOf(id);
        }

        public boolean checkMessageID() {
            return messageID != null && messageID.length() <= 10;
        }

        public boolean checkRecipientCell(String recipientCell) {
            if (recipientCell == null) return false;
            String regex = "^\\+\\d{1,3}\\d{1,10}$";
            return Pattern.matches(regex, recipientCell);
        }

        public String createMessageHash() {
            if (messageID == null || messageID.length() < 2 || messageText == null || messageText.trim().isEmpty()) {
                return "";
            }
            String firstTwoId = messageID.substring(0, 2);
            String cleanedMsg = messageText.replaceAll("[^a-zA-Z ]", "").trim();
            String[] words = cleanedMsg.split("\\s+");

            String firstWord = words.length > 0 ? words[0] : "";
            String lastWord = words.length > 0 ? words[words.length - 1] : "";
            String combinedWords = (firstWord + lastWord).toUpperCase();

            return firstTwoId + ":" + numSentMessages + ":" + combinedWords;
        }

        public boolean checkMessageLength() {
            return messageText != null && messageText.length() <= 250;
        }

        public String sentMessage(int choice) {
            switch (choice) {
                case 1:
                    return "Message successfully sent.";
                case 2:
                    return "Press 0 to delete the message.";
                case 3:
                    return "Message successfully stored.";
                default:
                    return "Invalid option selected.";
            }
        }

        public String printMessages() {
            return "Message ID: " + messageID + "\n" +
                    "Message Hash: " + messageHash + "\n" +
                    "Recipient: " + recipient + "\n" +
                    "Message: " + messageText;
        }

        public int returnTotalMessages() {
            return numSentMessages;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println(" WELCOME TO QUICKCHAT APP ");
        System.out.println("========================================");

        System.out.print("Enter First Name: ");
        String fName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lName = scanner.nextLine();

        Login loginSystem = new Login(fName, lName);

        boolean registered = false;
        while (!registered) {
            System.out.println("\n--- Registration ---");
            System.out.print("Enter Username (must contain '_' and be <= 5 chars, e.g., kyl_1): ");
            String uname = scanner.nextLine();
            System.out.print("Enter Password (>= 8 chars, 1 capital, 1 number, 1 special): ");
            String pwd = scanner.nextLine();
            System.out.print("Enter Cell Phone Number (with international code, e.g., +27838968976): ");
            String cell = scanner.nextLine();

            String regResult = loginSystem.registerUser(uname, pwd, cell, fName, lName);
            System.out.println(regResult);

            if (regResult.contains(" successfully captured.")) {
                registered = true;
            }
        }

        // Login Loop
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("\n--- Login ---");
            System.out.print("Enter Username: ");
            String loginUser = scanner.nextLine();
            System.out.print("Enter Password: ");
            String loginPwd = scanner.nextLine();

            loggedIn = loginSystem.loginUser(loginUser, loginPwd);
            String statusMsg = loginSystem.returnLoginStatus(loggedIn);
            System.out.println(statusMsg);
        }

        System.out.println("\nWelcome to QuickChat.");

        System.out.print("How many messages do you wish to enter? ");
        int numMessages = Integer.parseInt(scanner.nextLine());

        Message[] messageList = new Message[numMessages];
        int sentCount = 0;

        int choice = 0;
        while (choice != 3) {
            System.out.println("\n----------------- MENU -----------------");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages (Coming Soon)");
            System.out.println("3) Quit");
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    for (int i = 0; i < numMessages; i++) {
                        System.out.println("\n--- Processing Message " + (i + 1) + " of " + numMessages + " ---");

                        System.out.print("Enter Recipient Cell Number (e.g., +27718693002): ");
                        String recipientCell = scanner.nextLine();

                        Message tempValidator = new Message();
                        while (!tempValidator.checkRecipientCell(recipientCell)) {
                            System.out.println("Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.");
                            System.out.print("Re-enter Recipient Cell Number: ");
                            recipientCell = scanner.nextLine();
                        }

                        System.out.print("Enter Message text (max 250 characters): ");
                        String text = scanner.nextLine();

                        Message msg = new Message(recipientCell, text, (sentCount + 1));

                        while (!msg.checkMessageLength()) {
                            System.out.println("Please enter a message of less than 250 characters.");
                            System.out.print("Re-enter Message text: ");
                            text = scanner.nextLine();
                            msg = new Message(recipientCell, text, (sentCount + 1));
                        }

                        System.out.println("Message ready to send.");
                        System.out.println("Choose an action:");
                        System.out.println("1) Send Message");
                        System.out.println("2) Disregard Message (Delete)");
                        System.out.println("3) Store Message to send later");
                        System.out.print("Enter choice (1-3): ");
                        int actionChoice = Integer.parseInt(scanner.nextLine());

                        String actionResponse = msg.sentMessage(actionChoice);
                        System.out.println(actionResponse);

                        if (actionChoice == 1 || actionChoice == 3) {
                            sentCount++;
                            messageList[i] = msg;
                            System.out.println("\n--- Message Details ---");
                            System.out.println(msg.printMessages());
                        } else if (actionChoice == 2) {
                            System.out.println("Press 0 to delete the message.");
                        }
                    }
                    System.out.println("\nTotal messages successfully processed/sent: " + sentCount);
                    break;

                case 2:
                    System.out.println("Coming Soon.");
                    break;

                case 3:
                    System.out.println("Exiting QuickChat. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }
}
