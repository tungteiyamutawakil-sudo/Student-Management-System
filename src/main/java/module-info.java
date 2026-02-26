module com.mimi.sms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mimi.sms.ui to javafx.fxml;
    exports com.mimi.sms;
}