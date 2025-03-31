package uptc.so.rr.procesosroundrobin.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import uptc.so.rr.procesosroundrobin.domain.Manager;
import uptc.so.rr.procesosroundrobin.models.Process;
import uptc.so.rr.procesosroundrobin.view.ItemLabel;
import uptc.so.rr.procesosroundrobin.view.ProcessComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

/**
 * @implNote This class is used by the {@link uptc.so.rr.procesosroundrobin.App},
 * to link the view (the view.fxml file inside the src/main/resources folder),
 * and the domain manager {@link Manager} for the interaction events
 */
@Component
@Scope(SCOPE_SINGLETON)
public class Controller {

    private final Manager manager;

    @FXML
    private TextField quantumText;

    @FXML
    private Button addProcessButton;

    @FXML
    private Button executeProcessesButton;

    @FXML
    private VBox processesPanel;

    @FXML
    private Label noProcessesLabel;

    @FXML
    private GridPane processesResults;

    @FXML
    private Label watInfoLabel;

    @FXML
    private Label ntatInfoLabel;

    @FXML
    private Label ntatdesvLabel;

    @FXML
    private Label totalTimeLabel;

    private final List<ProcessComponent> processes = new ArrayList<>();

    public Controller(Manager manager) {
        this.manager = manager;
    }

    /**
     * Called when the mouse enters the {@link #addProcessButton} button.
     * Changes the color of the button to a darker orange to indicate that it's being hovered.
     */
    @FXML
    protected void onEnteredAddProcess() {
        addProcessButton.setStyle("-fx-background-color: #ae8308; -fx-border-color: #b98b10; -fx-border-radius: 10; -fx-background-radius: 10;");
    }

    /**
     * Called when the mouse exits the {@link #addProcessButton} button.
     * Changes the color of the button back to its original color.
     */
    @FXML
    protected void onExitedAddProcess() {
        addProcessButton.setStyle("-fx-background-color: #ffde86; -fx-border-color: #b98b10; -fx-border-radius: 10; -fx-background-radius: 10;");
    }

    /**
     * Called when the mouse enters the {@link #executeProcessesButton} button.
     * Changes the color of the button to a darker green to indicate that it's being hovered.
     */
    @FXML
    protected void onEnteredExecute() {
         executeProcessesButton.setStyle("-fx-background-color: #c8af20; -fx-border-color: #c8af20; -fx-border-radius: 10; -fx-background-radius: 10;");
    }

    /**
     * Called when the mouse exits the {@link #executeProcessesButton} button.
     * Changes the color of the button back to its original color.
     */
    @FXML
    protected void onExitedExecute() {
        executeProcessesButton.setStyle("-fx-background-color: #ffde86; -fx-border-color: #b98b10; -fx-border-radius: 10; -fx-background-radius: 10;");
    }

    /**
     * Adds a new process to the list component of processes.
     * When the delete button of the added process is clicked, it's removed from the list.
     * The {@link #executeProcessesButton} button is enabled when there are processes and disabled when there aren't.
     * The {@link #noProcessesLabel} label is shown when there aren't processes and hidden otherwise.
     * The {@link #processesResults} panel is shown when there are processes and hidden otherwise.
     */
    @FXML
    protected void addProcess() {
        ProcessComponent processComponent = new ProcessComponent(processes.size() + 1);
        processComponent.setCallback(() -> {
            processesPanel.getChildren().remove(processComponent);
            processes.remove(processComponent);
            executeProcessesButton.setDisable(processes.isEmpty());
            updateProcessesNumber();
            noProcessesLabel.setVisible(processes.isEmpty());
            processesResults.setVisible(!processes.isEmpty());
        });
        processesPanel.getChildren().add(processComponent);
        processes.add(processComponent);
        executeProcessesButton.setDisable(false);
        noProcessesLabel.setVisible(false);
        processesResults.setVisible(true);
    }

    /**
     * Called when the {@link #executeProcessesButton} button is clicked.
     * It calls the {@link Manager} to execute the round-robin algorithm,
     * with the given quantum and processes list, and shows the results.
     * The results are shown in the {@link #processesResults} component.
     * The {@link #watInfoLabel}, {@link #ntatInfoLabel}, {@link #ntatdesvLabel}
     * and {@link #totalTimeLabel} labels are updated with the results.
     */
    @FXML
    protected void executeProcesses() {
        processesResults.getChildren().clear();
        List<Process> data = manager.getRRData(processes.stream().map(ProcessComponent::getProcess).toList(),
                Integer.parseInt(quantumText.getText()));
        watInfoLabel.setText("WTprom = %.2f".formatted(manager.getAverageWaitingTime(data)));
        double promNtat = manager.getAverageNormalizedTurnAroundTime(data);
        ntatInfoLabel.setText("NTATprom = %.2f".formatted(promNtat));
        ntatdesvLabel.setText("NTATdesv.est. = %.2f".formatted(manager.getNormalizedTurnAroundDeviation(data, promNtat)));
        totalTimeLabel.setText("Tiempo total = %d".formatted(manager.getTotalTime(data)));
        int index = 0;
        for (Process process : data) {
            ItemLabel name = new ItemLabel(process.getName());
            ItemLabel arrivalTime = new ItemLabel(String.valueOf(process.getArrivalTime()));
            ItemLabel burstTime = new ItemLabel(String.valueOf(process.getBurstTime()));
            ItemLabel completionTime = new ItemLabel(String.valueOf(process.getCompletionTime()));
            ItemLabel turnAroundTime = new ItemLabel(String.valueOf(process.getTurnAroundTime()));
            ItemLabel normalizedTurnAroundTime = new ItemLabel("%.2f".formatted(process.getNormalizedTurnAroundTime()));
            ItemLabel waitingTime = new ItemLabel(String.valueOf(process.getWaitingTime()));
            processesResults.add(name, 0, index);
            processesResults.add(arrivalTime, 1, index);
            processesResults.add(burstTime, 2, index);
            processesResults.add(completionTime, 3, index);
            processesResults.add(turnAroundTime, 4, index);
            processesResults.add(normalizedTurnAroundTime, 5, index);
            processesResults.add(waitingTime, 6, index);
            index++;
        }
    }

    /**
     * Updates the process number of each process component in the {@link #processes} list,
     * and update the list in the {@link #processesPanel} component.
     * So that the first process component is process number 1, the second is process number 2, ...
     */
    private void updateProcessesNumber() {
        for (int i = 0; i < processes.size(); i++) {
            processes.get(i).setProcessNumber(i + 1);
        }
    }

    /**
     * This method is called when the {@link Controller} is initialized.
     * It prepares the visual components.
     */
    @FXML
    public void initialize() {
        processesResults.getRowConstraints().remove(0);
        quantumText.setTextFormatter(getNumberFormatter());
    }

    /**
     * Gets a TextFormatter for a TextField that only allows numbers or empty.
     *
     * @return a TextFormatter that only allows numbers or empty
     */
    private static TextFormatter<String> getNumberFormatter() {
        return new TextFormatter<>(change -> change.getText().matches("[0-9]*") ? change : null);
    }
}