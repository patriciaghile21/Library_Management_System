package org.example.oop2024_30423_ghile_patricia_project;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import org.example.oop2024_30423_ghile_patricia_project.books.Book;
import org.example.oop2024_30423_ghile_patricia_project.library.LibraryInventory;
import java.util.List;

public class HelloController {

    @FXML
    private TextField bookIdTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField authorTextField;
    @FXML
    private TextField genreTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField stockTextField;

    //TableView and TableColumns
    @FXML
    private TableView<Book> bookTableView;
    @FXML
    private TableColumn<Book, Integer> bookIdColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> genreColumn;
    @FXML
    private TableColumn<Book, Double> priceColumn;
    @FXML
    private TableColumn<Book, Integer> stockColumn;

    private final LibraryInventory libraryInventory = new LibraryInventory();

    @FXML
    protected void addBook() {
        String title = titleTextField.getText();
        String authorName = authorTextField.getText();
        String genreName = genreTextField.getText();
        String priceText = priceTextField.getText();
        String stockText = stockTextField.getText();

        if (title.isEmpty() || authorName.isEmpty() || genreName.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
            showAlert(AlertType.WARNING, "Input error", "Please fill in all the text fields.");
            return;
        }

        double price;
        int stockQuantity;

        try {
            price = Double.parseDouble(priceText);
            stockQuantity = Integer.parseInt(stockText);
        } catch (NumberFormatException e) {
            showAlert(AlertType.WARNING, "Input error", "Price and stock must be valid numbers.");
            return;
        }

        //get or create the author and genre IDs
        int authorId = libraryInventory.getOrCreateAuthorId(authorName);
        int genreId = libraryInventory.getOrCreateGenreId(genreName);

        Book newBook = new Book(0, title, authorId, genreId, price, stockQuantity);

        Book createdBook = libraryInventory.createBook(newBook);

        if (createdBook != null) {
            showAlert(AlertType.INFORMATION, "Success", "Book added successfully!");
            displayAll();
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to add the book.");
        }
    }

    @FXML
    protected void deleteBook() {

        String title = titleTextField.getText();

        if (title.isEmpty()) {
            showAlert(AlertType.WARNING, "Input error", "Please enter the title of the book to delete.");
            return;
        }

        boolean isDeleted = libraryInventory.deleteBookByTitle(title);

        if (isDeleted) {
            showAlert(AlertType.INFORMATION, "Success", "Book deleted successfully!");
        } else {
            showAlert(AlertType.WARNING, "Error", "No book found with the title: " + title);
        }
    }

    @FXML
    protected void updateStock() {

        String bookIdText = bookIdTextField.getText();
        String titleText = titleTextField.getText();
        String stockText = stockTextField.getText();

        if (bookIdText.isEmpty() && titleText.isEmpty()) {
            showAlert(AlertType.WARNING, "Input error", "Please enter a book ID or a title.");
            return;
        }

        if (stockText.isEmpty()) {
            showAlert(AlertType.WARNING, "Input error", "Please enter the new stock quantity.");
            return;
        }

        int newStockQuantity;

        try {
            newStockQuantity = Integer.parseInt(stockText);
        } catch (NumberFormatException e) {
            showAlert(AlertType.WARNING, "Input error", "Stock quantity must be a valid number.");
            return;
        }

        boolean isUpdated = false;

        if (!bookIdText.isEmpty()) {
            try {
                int bookId = Integer.parseInt(bookIdText);
                isUpdated = libraryInventory.updateBookStockQuantity(bookId, null, newStockQuantity);
            } catch (NumberFormatException e) {
                showAlert(AlertType.WARNING, "Input error", "Book ID must be a valid number.");
                return;
            }
        }

        else if (!titleText.isEmpty()) {
            isUpdated = libraryInventory.updateBookStockQuantity(null, titleText, newStockQuantity);
        }

        if (isUpdated) {
            showAlert(AlertType.INFORMATION, "Success", "Stock quantity updated successfully!");
        } else {
            showAlert(AlertType.WARNING, "Error", "No book found with the provided ID or title.");
        }
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void displayAll() {

        List<Book> books = libraryInventory.getAllBooks();

        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(cellData -> {
            int authorId = cellData.getValue().getAuthorId();
            String authorName = libraryInventory.getAuthorNameById(authorId);
            return new SimpleStringProperty(authorName);
        });
        genreColumn.setCellValueFactory(cellData -> {
            int genreId = cellData.getValue().getGenreId();
            String genreName = libraryInventory.getGenreNameById(genreId);
            return new SimpleStringProperty(genreName);
        });
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));

        bookTableView.getItems().setAll(books);
    }

    @FXML
    protected void filterByGenre() {
        String genreName = genreTextField.getText();

        if (genreName.isEmpty()) {
            showAlert(AlertType.WARNING, "Input error", "Please enter a genre.");
            return;
        }

        List<Book> filteredBooks = libraryInventory.getBooksByGenre(genreName);

        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(cellData -> {
            int authorId = cellData.getValue().getAuthorId();
            String authorNameFromDb = libraryInventory.getAuthorNameById(authorId);
            return new SimpleStringProperty(authorNameFromDb);
        });
        genreColumn.setCellValueFactory(cellData -> {
            int genreId = cellData.getValue().getGenreId();
            String genreNameFromDb = libraryInventory.getGenreNameById(genreId);
            return new SimpleStringProperty(genreNameFromDb);
        });
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));

        if (filteredBooks.isEmpty()) {
            showAlert(AlertType.INFORMATION, "No result", "No books found for the genre: " + genreName);
        } else {
            bookTableView.getItems().setAll(filteredBooks);
        }
    }

    @FXML
    protected void search() {

        String title = titleTextField.getText();
        String authorName = authorTextField.getText();
        String genreName = genreTextField.getText();

        if (title.isEmpty() && authorName.isEmpty() && genreName.isEmpty()) {
            showAlert(AlertType.WARNING, "Input error", "Please enter at least one search criterion.");
            return;
        }

        List<Book> searchResults = libraryInventory.searchBooks(title, authorName, genreName);

        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(cellData -> {
            int authorId = cellData.getValue().getAuthorId();
            String authorNameFromDb = libraryInventory.getAuthorNameById(authorId);
            return new SimpleStringProperty(authorNameFromDb);
        });
        genreColumn.setCellValueFactory(cellData -> {
            int genreId = cellData.getValue().getGenreId();
            String genreNameFromDb = libraryInventory.getGenreNameById(genreId);
            return new SimpleStringProperty(genreNameFromDb);
        });
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));

        if (searchResults.isEmpty()) {
            showAlert(AlertType.INFORMATION, "No result", "No books found.");
        } else {
            bookTableView.getItems().setAll(searchResults);
        }
    }

    @FXML
    protected void sortByPrice() {

        List<Book> sortedBooks = libraryInventory.sortBooksByPrice();

        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(cellData -> {
            int authorId = cellData.getValue().getAuthorId();
            String authorName = libraryInventory.getAuthorNameById(authorId);
            return new SimpleStringProperty(authorName);
        });
        genreColumn.setCellValueFactory(cellData -> {
            int genreId = cellData.getValue().getGenreId();
            String genreName = libraryInventory.getGenreNameById(genreId);
            return new SimpleStringProperty(genreName);
        });
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));

        bookTableView.getItems().setAll(sortedBooks);
    }
}