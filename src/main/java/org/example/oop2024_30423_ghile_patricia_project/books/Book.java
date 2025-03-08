package org.example.oop2024_30423_ghile_patricia_project.books;

public class Book {
    private int bookId;
    private String title;
    private int authorId;  // Reverted to integer for authorId
    private int genreId;   // Reverted to integer for genreId
    private double price;
    private int stockQuantity;

    // Constructor
    public Book(int bookId, String title, int authorId, int genreId, double price, int stockQuantity) {
        this.bookId = bookId;
        this.title = title;
        this.authorId = authorId;
        this.genreId = genreId;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    // Override the toString() method to display the details of the book
    @Override
    public String toString() {
        return "\n" +
                "Book Details:\n" +
                "Book ID: " + bookId + "\n" +
                "Title: " + title + "\n" +
                "Author ID: " + authorId + "\n" +
                "Genre ID: " + genreId + "\n" +
                "Price: " + price + "\n" +
                "Stock Quantity: " + stockQuantity + "\n\n";
    }
}