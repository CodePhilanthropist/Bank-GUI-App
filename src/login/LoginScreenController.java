package login;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginScreenController implements Initializable {

    public static Stage stage = null;
    public static String acc;

    @FXML
    private Pane main_area;

    @FXML
    private void closeApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private TextField accountno;

    @FXML
    private TextField pin;

    @FXML
    private void createAccount(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/createaccount/CreateAccount.fxml"));
        main_area.getChildren().removeAll();
        main_area.getChildren().addAll(fxml);
    }

    @FXML
    private void forgotPassword(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("/forgotpassword/ForgotPassword.fxml"));
        main_area.getChildren().removeAll();
        main_area.getChildren().addAll(fxml);
    }

    public void loginAccount(MouseEvent event) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
            String sql = "SELECT * FROM userdata WHERE AccountNo=? AND PIN=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, accountno.getText());
            ps.setString(2, pin.getText());
            acc = accountno.getText();

            rs = ps.executeQuery();
            if (rs.next()) {              

                Parent root = FXMLLoader.load(getClass().getResource("/dashboard/Dashboard.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/design/style.css").toExternalForm());
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                stage.show();
                this.stage = stage;
            } else {
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Invalid username or password");
                a.setContentText("Please check your username or password and try again! ");
                a.showAndWait();
            }
//            con.close();
//            ps.close();

        } catch (Exception e) {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("There's something wrong");
            a.setContentText("Error message: " + e.getMessage());
            a.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
