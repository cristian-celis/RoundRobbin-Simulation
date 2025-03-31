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

    /**
     * Initialize the spring context.
     * This method is called before the {@link #start(Stage)} method.
     * The default implementation does nothing.
     * It uses the {@link AppConfig} class for configure the spring context.
     */
    @Override
    public void init() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    /**
     * Starts the application.
     * This method is called after {@link #init()} method.
     * It loads the view.fxml file and sets the controller factory to use the spring context.
     * The method also gives another visual configs.
     * @param stage the stage to show, this class is provided by JavaFx.
     * @throws IOException if the view.fxml file can not be loaded.
     */
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

    /**
     * This is the main method of the application.
     * It calls the {@link #launch(String[])} method to launch the application.
     * @param args the command line arguments, they are not used in this application.
     */
    public static void main(String[] args) {
        launch();
    }

}