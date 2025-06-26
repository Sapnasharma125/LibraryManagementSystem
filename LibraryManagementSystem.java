import java.util.*;

class Book {
    private int id;
    private String title;
    private String author;
    private boolean isIssued;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isIssued = false;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isIssued() { return isIssued; }
    public void setIssued(boolean issued) { this.isIssued = issued; }

    @Override
    public String toString() {
        return id + " - " + title + " by " + author + (isIssued ? " [Issued]" : "");
    }
}

class User {
    private String name;
    private List<Book> issuedBooks;

    public User(String name) {
        this.name = name;
        this.issuedBooks = new ArrayList<>();
    }

    public String getName() { return name; }
    public List<Book> getIssuedBooks() { return issuedBooks; }

    public void issueBook(Book book) {
        issuedBooks.add(book);
    }

    public void returnBook(Book book) {
        issuedBooks.remove(book);
    }

    @Override
    public String toString() {
        return name + " has issued " + issuedBooks.size() + " book(s)";
    }
}

class Library {
    private List<Book> books = new ArrayList<>();
    private Map<String, User> users = new HashMap<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public void showAvailableBooks() {
        System.out.println("\nAvailable Books:");
        boolean found = false;
        for (Book book : books) {
            if (!book.isIssued()) {
                System.out.println(book);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books available.");
        }
    }

    public void showAllUsers() {
        System.out.println("\nUsers and Their Books:");
        if (users.isEmpty()) {
            System.out.println("No users in the system.");
            return;
        }
        for (User user : users.values()) {
            System.out.println(user);
            for (Book b : user.getIssuedBooks()) {
                System.out.println("  - " + b);
            }
        }
    }

    private User getUserCaseInsensitive(String name) {
        for (String key : users.keySet()) {
            if (key.equalsIgnoreCase(name)) {
                return users.get(key);
            }
        }
        return null;
    }

    public boolean issueBook(String userName, int bookId) {
        User user = getUserCaseInsensitive(userName);
        if (user == null) {
            user = new User(userName);
            users.put(userName, user);
            System.out.println("New user '" + userName + "' added.");
        }

        for (Book book : books) {
            if (book.getId() == bookId && !book.isIssued()) {
                book.setIssued(true);
                user.issueBook(book);
                System.out.println("Book issued to " + user.getName());
                return true;
            }
        }

        System.out.println("Book not available.");
        return false;
    }

    public boolean returnBook(String userName, int bookId) {
        User user = getUserCaseInsensitive(userName);
        if (user == null) {
            System.out.println("User not found.");
            return false;
        }

        for (Book book : user.getIssuedBooks()) {
            if (book.getId() == bookId) {
                book.setIssued(false);
                user.returnBook(book);
                System.out.println("Book returned by " + user.getName());
                return true;
            }
        }

        System.out.println("Book not found in user's issued list.");
        return false;
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        // Sample books
        library.addBook(new Book(1, "The Hobbit", "J.R.R. Tolkien"));
        library.addBook(new Book(2, "1984", "George Orwell"));
        library.addBook(new Book(3, "Java Basics", "James Gosling"));
        library.addBook(new Book(4, "Clean Code", "Robert C. Martin"));

        int choice;
        do {
            System.out.println("\nLibrary Menu:");
            System.out.println("1. Show available books");
            System.out.println("2. Issue book");
            System.out.println("3. Return book");
            System.out.println("4. Show users and their books");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Enter number: ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            switch (choice) {
                case 1:
                    library.showAvailableBooks();
                    break;
                case 2:
                    System.out.print("Enter user name: ");
                    String uname = scanner.nextLine();
                    System.out.print("Enter book ID to issue: ");
                    int bid = scanner.nextInt();
                    library.issueBook(uname.trim(), bid);
                    break;
                case 3:
                    System.out.print("Enter user name: ");
                    String uname2 = scanner.nextLine();
                    System.out.print("Enter book ID to return: ");
                    int bid2 = scanner.nextInt();
                    library.returnBook(uname2.trim(), bid2);
                    break;
                case 4:
                    library.showAllUsers();
                    break;
                case 0:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);

        scanner.close();
    }
}

