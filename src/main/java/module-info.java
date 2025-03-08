module org.example.oop2024_30423_ghile_patricia_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    requires java.sql;
    requires org.postgresql.jdbc;

    exports org.example.oop2024_30423_ghile_patricia_project;

    opens org.example.oop2024_30423_ghile_patricia_project.books to javafx.base;
    exports org.example.oop2024_30423_ghile_patricia_project.books;

    opens org.example.oop2024_30423_ghile_patricia_project.connection to java.sql;
    exports org.example.oop2024_30423_ghile_patricia_project.connection;

    opens org.example.oop2024_30423_ghile_patricia_project.library to java.sql;
    exports org.example.oop2024_30423_ghile_patricia_project.library;
    opens org.example.oop2024_30423_ghile_patricia_project to java.sql, javafx.fxml;

}