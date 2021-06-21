/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboard;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import login.Banking;
import login.LoginScreenController;

public class DashboardController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    public static String ac = LoginScreenController.acc;

    @FXML
    private Pane dashboard_main;

    @FXML
    private Text name;

    @FXML
    private FontAwesomeIconView ico;

    @FXML
    private Circle profilepic;

    @FXML
    private void closeApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);

    }

    @FXML
    private void minimizeApp(MouseEvent event) {
        Stage stage = (Stage) ico.getScene().getWindow();
        stage.setIconified(true);

    }

    public void setData() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "");
            String sql = "SELECT * FROM userdata WHERE AccountNo=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, LoginScreenController.acc);

            rs = ps.executeQuery();
            if (rs.next()) {

                name.setText(rs.getString("Name"));
                InputStream is = rs.getBinaryStream("ProfilePic");
                OutputStream os = new FileOutputStream(new File("pic.jpg"));
                byte[] content = new byte[1024];
                int size = 0;

                while ((size = is.read(content)) != -1) {
                    os.write(content, 0, size);
                }
                os.close();
                is.close();
                Image img = new Image("file:pic.jpg", false);
                profilepic.setFill(new ImagePattern(img));
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Error in login");
                a.setContentText("Your account number or pin is wrong. Enter again..!!!");
                a.showAndWait();
            }

        } catch (Exception e) {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText("Invalid in login");
            a.setContentText("Please check your username or password and try again! " + e.getCause());
            a.showAndWait();
        }
    }

    @FXML
    public void click(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    public void drag(MouseEvent event) {
        LoginScreenController.stage.setX(event.getScreenX() - xOffset);
        LoginScreenController.stage.setY(event.getScreenY() - yOffset);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setData();
//        Banking.stage.close();

    }

}
