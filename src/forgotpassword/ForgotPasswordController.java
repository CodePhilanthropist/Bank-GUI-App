package forgotpassword;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import login.Banking;

public class ForgotPasswordController implements Initializable {

    @FXML
    private TextField accountno;

    @FXML
    private ComboBox<String> question;

    @FXML
    private TextField answer;

    ObservableList<String> list = FXCollections.observableArrayList("What is your pet name?", "What's your childhood twon", "Who's cool?");

    public void backToLogin(MouseEvent event) throws IOException {
        Banking.stage.getScene().setRoot(FXMLLoader.load(getClass().getResource("/login/LoginScreen.fxml")));
    }

    public void closeApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    public void recoverAccount(MouseEvent event) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
            String sql = "SELECT * FROM userdata WHERE AccountNo=? and SecurityQuestion=? and Answer=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, accountno.getText());
            ps.setString(2, question.getValue());
            ps.setString(3, answer.getText());

            rs = ps.executeQuery();
            if (rs.next()) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Password Recovery");
                a.setHeaderText("Below is your passworrd!");
                a.setContentText("Account No: " + rs.getString("AccountNo")+"\nPIN: " + rs.getString("PIN"));
                a.showAndWait();
            }
            con.close();
            ps.close();

        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Wrong PIN");
            a.setContentText("Please check your PIN and try again! " + e.getMessage());
            a.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        question.setItems(list);
    }

}
