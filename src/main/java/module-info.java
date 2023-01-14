module com.example.lab6map {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lab6map to javafx.fxml;
    opens GuiClasses to javafx.fxml;

    exports com.example.lab6map;
    exports GuiClasses;
    exports Model;
    exports Controller;
}