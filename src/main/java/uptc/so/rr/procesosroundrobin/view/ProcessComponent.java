package uptc.so.rr.procesosroundrobin.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import uptc.so.rr.procesosroundrobin.domain.model.Process;

/**
 * @implNote This class represents a process visually, so it's a visual component
 */
public class ProcessComponent extends VBox {

    /**
     * @implNote This interface is used to define a callback
     */
    public interface VoidCallback {
        /**
         * Execute the callback
         */
        void execute();
    }

    private final Label nameLabel;

    private final TextField valueBt;

    private final TextField valueAt;

    private String processName;

    private VoidCallback callback;

    public ProcessComponent(int processNumber) {
        this.nameLabel = new Label("Proceso %d (P%d)".formatted(processNumber, processNumber));
        processName = "P%d".formatted(processNumber);
        HBox btBox = new HBox();
        Label btLabel = new Label("Tiempo de proceso:");
        valueBt = new TextField("0");
        valueBt.setMaxWidth(50);
        valueBt.setTextFormatter(getNumberFormatter());
        btBox.setSpacing(10);
        btBox.setAlignment(javafx.geometry.Pos.CENTER);
        btBox.getChildren().addAll(btLabel, valueBt);
        HBox atBox = new HBox();
        Label atLabel = new Label("Tiempo de llegada:");
        valueAt = new TextField("0");
        valueAt.setMaxWidth(50);
        valueAt.setTextFormatter(getNumberFormatter());
        atBox.setSpacing(10);
        atBox.setAlignment(javafx.geometry.Pos.CENTER);
        atBox.getChildren().addAll(atLabel, valueAt);
        Button deleteButton = getDeleteButton();
        this.setStyle("-fx-border-color: #000000; -fx-border-radius: 5; -fx-background-radius: 5;");
        this.setSpacing(5);
        this.setPadding(new javafx.geometry.Insets(5, 2, 5, 2));
        this.setAlignment(javafx.geometry.Pos.CENTER);
        this.getChildren().addAll(nameLabel, atBox, btBox, deleteButton);
    }

    /**
     * Gets a TextFormatter for a TextField that only allows numbers or empty.
     *
     * @return a TextFormatter that only allows numbers or empty
     */
    private static TextFormatter<String> getNumberFormatter() {
        return new TextFormatter<>(change -> change.getText().matches("[0-9]*") ? change : null);
    }

    /**
     * Generates a delete button with the specified style.
     * @return the delete button
     */
    private Button getDeleteButton() {
        Button deleteButton = new Button("Eliminar proceso");
        deleteButton.setCursor(javafx.scene.Cursor.HAND);
        deleteButton.setStyle("-fx-background-color: #dd0505; -fx-border-color: red; -fx-border-radius: 5; -fx-background-radius: 5;");
        deleteButton.setOnMouseEntered(event -> deleteButton.setStyle("-fx-background-color: red; -fx-border-color: red; -fx-border-radius: 5; -fx-background-radius: 5;"));
        deleteButton.setOnMouseExited(event -> deleteButton.setStyle("-fx-background-color: #ef2424; -fx-border-color: red; -fx-border-radius: 5; -fx-background-radius: 5;"));
        deleteButton.setOnAction(event -> callback.execute());
        return deleteButton;
    }

    /**
     * Updates the name of this process component to match the given process number.
     * @param processNumber the new process number
     */
    public void setProcessNumber(int processNumber) {
        this.nameLabel.setText("Proceso %d (P%d)".formatted(processNumber, processNumber));
        processName = "P%d".formatted(processNumber);
    }

    /**
     * Sets the callback to be executed when the delete button is clicked.
     * @param callback the callback to be executed
     */
    public void setCallback(VoidCallback callback) {
        this.callback = callback;
    }

    /**
     * Generates a {@link Process} class with the name, burst time and arrival time shown in this component.
     * @return the generated process class
     */
    public Process getProcess() {
        return new Process(processName, Integer.parseInt(valueBt.getText()), Integer.parseInt(valueAt.getText()));
    }
}
