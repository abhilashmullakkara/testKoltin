module org.example.testkoltin {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    requires com.dlsc.formsfx;
    requires firebase.admin;
    requires com.google.auth.oauth2;
    requires com.google.gson;

    opens org.example.testkoltin to javafx.fxml;
    exports org.example.testkoltin;
    opens org.example to javafx.fxml, firebase.admin;
    exports org.example to firebase.admin;


}