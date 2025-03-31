package uptc.so.rr.procesosroundrobin;

import uptc.so.rr.procesosroundrobin.configs.AppConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * @implNote This class is used to launch the application
 */
public class App extends Application {

    private ApplicationContext context;

    
    @Override
    public void init() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("Simulación cola de procesos Round Robin");
        stage.setScene(scene);
        stage.setMinHeight(520);
        stage.setMinWidth(720);
        stage.show();
    }

    
    public static void main(String[] args) {
        launch();
    }

}