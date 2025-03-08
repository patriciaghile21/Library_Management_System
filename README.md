# Library Management System

The **Library Management System** is a Java-based application designed to manage a library’s inventory. The system connects to a PostgreSQL database named **Library** and allows users to store and retrieve book data. It provides a graphical user interface (GUI) built with JavaFX for managing books, tracking stock quantities, and searching for books by title, author, and genre. The application also supports user login authentication.

## Features

- **CRUD Operations**: Add, update, delete, and view books in the database.
- **Book Search**: Search for books by title, author, or genre.
- **Inventory Tracking**: Track and update the stock quantity of each book.
- **Login Authentication**: Secure login system for user authentication.
- **Book Sorting**: Sort books by price and genre.

## Technologies Used

- **Java**: Version 21 (for backend development).
- **PostgreSQL**: Version 17 (for database management).
- **JavaFX**: For developing the graphical user interface (GUI).
- **PostgreSQL JDBC Driver**: For database connection.

## Requirements

Before running the application, make sure you have the following installed:

- **PostgreSQL 17**: For database setup and data management.
- **Java 21**: For compatibility with JavaFX 21 and application development.
- **IntelliJ IDEA** or any Java IDE (for project development and execution).
- **Git**: For cloning the repository.

## Instructions for Running the Application

1. **Clone the Repository**
    - In GitBash, type the following command to clone the repository:
      ```bash
      git clone https://github.com/patriciaghile21/Library_Management_System
      ```
    - Then, import the project into **IntelliJ IDEA**.

2. **Set Up the Database**
    - Download and install **PostgreSQL 17** if you don't have it installed already.
    - Create a new database named **Library** in PostgreSQL.
    - Create the necessary tables by importing the schema or creating them manually (you can include a schema file in your repository to make it easier).

3. **Set Up the Project in IntelliJ IDEA**
    - Open the project in **IntelliJ IDEA** or your preferred Java IDE.
    - Make sure you have **Java 21** installed and configured in the IDE.
    - Download the **PostgreSQL JDBC Driver** and add it to your project dependencies.
    - Build and run the project.

4. **Run the Application**
    - Once everything is set up, you can run the application:
    - The graphical user interface (GUI) will open.
    - Users will be able to log in, view, add, delete, and update books in the library system.

## Libraries

- **PostgreSQL JDBC Driver** (postgresql-42.7.4.jar): Required for database connectivity.
- **JavaFX**: Used for the graphical user interface.








