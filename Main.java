package org.example;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Book {
    private String book_author;
    private int total_copies;
    private String book_title;
    private int avai_copies;
    private String Book_ID;


    public Book(String book_title, String Book_ID, int total_copies, String book_author) {
        this.book_author = book_author;
        this.Book_ID = Book_ID;
        this.avai_copies = total_copies;
        this.total_copies = total_copies;
        this.book_title = book_title;
    }

    public String get_Title() {
        return book_title;
    }
    public String get_bookID() {
        return Book_ID;
    }

    public int get_totalcopies() {
        return total_copies;
    }

    public String get_author() {
        return book_author;
    }


    public int get_avaicopies() {
        return avai_copies;
    }

    public void decrease_avaicopies() {
        avai_copies = avai_copies-1;
    }

    public void increase_avaicopies() {
        avai_copies = avai_copies+1;
    }
}

class Member {
    private String name;
    private List<String> borrow_books;
    private String Ph_num;
    private int penalty;
    private Map<String, Instant> issue_books;


    public Member(String Ph_num, String name) {
        this.Ph_num = Ph_num;
        this.borrow_books = new ArrayList<>();
        this.name = name;
        this.issue_books = new HashMap<>();
        this.penalty = 0;
    }

    public String get_name() {
        return name;
    }

    public String get_Ph_num() {
        return Ph_num;
    }

    public List<String> get_borrowbooks() {
        return borrow_books;
    }

    public int get_penalty() {
        return penalty;
    }

    public void removeBorrowedBook(String Book_ID) {
        borrow_books.remove(Book_ID);
        issue_books.remove(Book_ID);
    }
    public void addBorrowedBook(Instant issueTime, String Book_ID) {
        borrow_books.add(Book_ID);
        issue_books.put(Book_ID, issueTime);
    }



    public void increasePenalty(int amount) {
        penalty += penalty + amount;
    }

    public Instant getBookIssueTime(String Book_ID) {
        return issue_books.get(Book_ID);
    }
}

public class Main {
    private Map<String, Book> books;
    private Map<String, Member> members;
    private Member current_mem;

    public Main() {
        books = new HashMap<>();
        members = new HashMap<>();
    }

    public void add_book(String book_title, String Book_ID, int total_copies, String book_author) {
        Book book = new Book(book_title, Book_ID, total_copies, book_author);
        books.put(Book_ID, book);
    }

    public void removeBook(String Book_ID) {
        if (books.containsKey(Book_ID)) {
            books.remove(Book_ID);
        }
    }

    public void register_member(String name, String Ph_num) {
        if (!members.containsKey(Ph_num)) {
            Member member = new Member(Ph_num, name);
            members.put(Ph_num, member);
        } else {
            System.out.println("Member with the same phone number already exists.");
        }
    }

    public void removeMember(String Ph_num) {
        if (members.containsKey(Ph_num)) {
            members.remove(Ph_num);
        }
    }

    public void Enter_as_member(String name, String Ph_num) {
        if (members.containsKey(Ph_num)) {
            current_mem = members.get(Ph_num);
            System.out.println("Logged in as " + current_mem.get_name());
        } else {
            System.out.println("Member not found. Please register first.");
        }
    }

    public void searchBookByTitle(String book_title) {
        System.out.println("Searching for books with book_title: " + book_title);
        for (Book book : books.values()) {
            if (book.get_Title().equalsIgnoreCase(book_title)) {
                System.out.println("ID: " + book.get_bookID() + ", Title: " + book.get_Title() + ", Author: " + book.get_author());
            }
        }
    }

    public void searchBookByAuthor(String book_author) {
        System.out.println("Searching for books by book_author: " + book_author);
        for (Book book : books.values()) {
            if (book.get_author().equalsIgnoreCase(book_author)) {
                System.out.println("ID: " + book.get_bookID() + ", Title: " + book.get_Title() + ", Author: " + book.get_author());
            }
        }
    }

    public void list_MembersWithNoPenalties() {
        System.out.println("Members with no pending fines:");
        for (Member member : members.values()) {
            if (member.get_penalty() == 0) {
                System.out.println("Name: " + member.get_name() + ", Phone Number: " + member.get_Ph_num());
            }
        }
    }

    public void listBorrowedBooksForMember(String Ph_num) {
        if (members.containsKey(Ph_num)) {
            Member member = members.get(Ph_num);
            List<String> borrow_books = member.get_borrowbooks();
            if (!borrow_books.isEmpty()) {
                System.out.println("Borrowed Books for " + member.get_name() + ":");
                for (String Book_ID : borrow_books) {
                    Book book = books.get(Book_ID);
                    System.out.println("ID: " + book.get_bookID() + ", Title: " + book.get_Title() + ", Author: " + book.get_author());
                }
            } else {
                System.out.println(member.get_name() + " has not borrowed any books.");
            }
        } else {
            System.out.println("Member not found.");
        }
    }

    public void issueBook(String Book_ID) {
        if (current_mem == null) {
            System.out.println("Please log in as a member first.");
        } else {
            if (books.containsKey(Book_ID)) {
                Book book = books.get(Book_ID);
                if (current_mem.get_penalty() == 0) {
                    if (book.get_avaicopies() > 0 && current_mem.get_borrowbooks().size() < 2) {
                        book.decrease_avaicopies();
                        Instant issueTime = Instant.now();
                        current_mem.addBorrowedBook(issueTime, Book_ID);
                        System.out.println("Book '" + book.get_Title() + "' issued successfully.");
                    } else {
                        System.out.println("No available copies of the book or member has reached the limit of borrowed books.");
                    }
                } else {
                    System.out.println("Please clear your pending dues before borrowing a new book.");
                }
            } else {
                System.out.println("Book not found.");
            }
        }
    }

    public void returnBook(String Book_ID) {
        if (current_mem == null) {
            System.out.println("Please log in as a member first.");
        } else {
            if (current_mem.get_borrowbooks().contains(Book_ID)) {
                Book book = books.get(Book_ID);
                Instant issueTime = current_mem.getBookIssueTime(Book_ID);
                Instant returnTime = Instant.now();
                Duration duration = Duration.between(issueTime, returnTime);
                long secondsLate = duration.getSeconds() - 10;
                int fine = Math.max(0, (int) (secondsLate * 3));
                if (fine > 0) {
                    current_mem.increasePenalty(fine);
                    System.out.println("Please pay a fine of " + fine + " rupees for returning the book late.");
                }
                book.increase_avaicopies();
                current_mem.removeBorrowedBook(Book_ID);
                System.out.println("Book '" + book.get_Title() + "' returned successfully.");
            } else {
                System.out.println("You haven't borrowed this book.");
            }
        }
    }

    public void listBooks() {
        System.out.println("Available Books:");
        for (Book book : books.values()) {
            System.out.println("Book ID: " + book.get_bookID() + ", Book Title: " + book.get_Title() + ", Book Author: " + book.get_author() + ", Number of Copies available: " + book.get_avaicopies());
        }
    }

    public void list_Members() {
        System.out.println("Registered Members:");
        for (Member member : members.values()) {
            System.out.println("Name: " + member.get_name() + ", Phone Number: " + member.get_Ph_num() + ", Penalty: " + member.get_penalty() + " rupees");
            List<String> borrow_books = member.get_borrowbooks();
            if (!borrow_books.isEmpty()) {
                System.out.println("Borrowed Books:");
                for (String Book_ID : borrow_books) {
                    Book book = books.get(Book_ID);
                    System.out.println("  ID: " + book.get_bookID() + ", Title: " + book.get_Title() + ", Author: " + book.get_author());
                }
            }
        }
    }

    public static void main(String[] args) {
        Main library = new Main();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Library Portal Initialized….\n-------------------------------");


        while (true) {
            System.out.println("1. Enter as Librarian\n2. Enter as Member\n3. Exit\n------------------------------");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // Librarian Menu
                    while (true) {
                        System.out.println("------------------------------\n1. Register a member\n2. Remove a member\n3. Add a book\n4. Remove a book\n5. View all members along with their books and fines to be paid\n6. View all books\n7. Back\n-------------------------------");

                        int librarianChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (librarianChoice) {
                            case 1:
                                System.out.println("--------------------------------\nName: ");
                                String name = scanner.nextLine();
                                System.out.print("Phone No.: ");
                                String Ph_num = scanner.nextLine();
                                library.register_member(name, Ph_num);
                                System.out.println("Member successfully registered");
                                break;

                            case 2:
                                System.out.print("Enter Phone Number of the member to remove: ");
                                String removeMemberPhoneNumber = scanner.nextLine();
                                library.removeMember(removeMemberPhoneNumber);
                                System.out.println("Member removed successfully.");
                                break;

                            case 3:
                                System.out.print("Enter Book ID: ");
                                String Book_ID = scanner.nextLine();
                                System.out.print("Enter Title: ");
                                String book_title = scanner.nextLine();
                                System.out.print("Enter Author: ");
                                String book_author = scanner.nextLine();
                                System.out.print("Enter Total Copies: ");
                                int total_copies = scanner.nextInt();
                                library.add_book(book_title, Book_ID, total_copies, book_author);
                                System.out.println("Book added successfully.");
                                break;

                            case 4:
                                System.out.print("Enter Book ID: ");
                                String removeBookId = scanner.nextLine();
                                library.removeBook(removeBookId);
                                System.out.println("Book removed successfully.");
                                break;

                            case 5:
                                library.list_Members();
                                break;

                            case 6:
                                library.listBooks();
                                break;

                            case 7:
                                break;

                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }

                        if (librarianChoice == 7) {
                            break;
                        }
                    }
                    break;

                case 2:
                    System.out.println("-------------------------------\nName: ");
                    String memberName = scanner.nextLine();
                    System.out.print("Phone No.: ");
                    String memberPhoneNumber = scanner.nextLine();
                    library.Enter_as_member(memberName, memberPhoneNumber);

                    // Check if member is found
                    if (library.current_mem == null) {
                        break;  // Return to the main menu
                    }

                    // Member Menu
                    while (true) {
                        System.out.println("-------------------------------\n1. List Available Books\n2. List My Books\n3. Issue Book\n4. Return Book\n5. Pay Fine\n6. Back\n--------------------------------");

                        int memberChoice = scanner.nextInt();
                        scanner.nextLine();
                        switch (memberChoice) {
                            case 1:
                                library.listBooks();
                                break;

                            case 2:
                                List<String> borrow_books = library.current_mem.get_borrowbooks();
                                if (borrow_books.isEmpty()) {
                                    System.out.println("You haven't borrowed any books.");
                                } else {
                                    System.out.println("Your Borrowed Books:");
                                    for (String Book_ID : borrow_books) {
                                        Book book = library.books.get(Book_ID);
                                        System.out.println("Book ID: " + book.get_bookID() + ", Book Title: " + book.get_Title() + ", Book Author: " + book.get_author());
                                    }
                                }
                                break;

                            case 3:
                                System.out.print("Enter Book ID to borrow: ");
                                String issueBookId = scanner.nextLine();
                                library.issueBook(issueBookId);
                                break;

                            case 4:
                                System.out.print("Enter Book ID to return: ");
                                String returnBookId = scanner.nextLine();
                                library.returnBook(returnBookId);
                                break;

                            case 5:
                                int penalty = library.current_mem.get_penalty();
                                if (penalty == 0) {
                                    System.out.println("You don't have any pending fines.");
                                } else {
                                    System.out.println("Your pending fine amount is " + penalty + " rupees.");
                                    System.out.print("Enter the amount to pay (Enter 0 to cancel): ");
                                    int amountToPay = scanner.nextInt();
                                    scanner.nextLine();
                                    if (amountToPay > 0 && amountToPay <= penalty) {
                                        library.current_mem.increasePenalty(-amountToPay);
                                        System.out.println("Payment successful. Remaining fine: " + library.current_mem.get_penalty() + " rupees.");
                                    } else if (amountToPay > penalty) {
                                        System.out.println("You cannot pay more than your pending fine.");
                                    } else {
                                        System.out.println("Payment cancelled.");
                                    }
                                }
                                break;

                            case 6:
                                library.current_mem = null;
                                break;

                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }

                        if (memberChoice == 6) {
                            break;
                        }
                    }
                    break;  // Return to the main menu


                case 3:
                    System.out.println("Thanks for visiting!");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}