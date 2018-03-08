import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Created  by Michal Nowak
 **/
public class SerpWieleFrazApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SerpWieleFrazApp.class.getResource("SerpWieleFraz.fxml"));
            Parent layout = fxmlLoader.load();
            Scene scene = new Scene(layout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Serp - jedna fraza");
            scene.getStylesheets().add("style.css");
            primaryStage.show();
        }catch(Exception ex){
            System.out.print(ex.getMessage());
        }
    }
}
