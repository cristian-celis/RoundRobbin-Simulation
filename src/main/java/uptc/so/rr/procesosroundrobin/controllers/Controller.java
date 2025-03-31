package uptc.so.rr.procesosroundrobin.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import uptc.so.rr.procesosroundrobin.domain.Repository;
import uptc.so.rr.procesosroundrobin.domain.model.Process;
import uptc.so.rr.procesosroundrobin.view.ItemLabel;
import uptc.so.rr.procesosroundrobin.view.ProcessComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@Component
@Scope(SCOPE_SINGLETON)
public class Controller {

    private final Repository repository;

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

    public Controller(Repository repository) {
        this.repository = repository;
    }

    @FXML
    protected void onEnteredAddProcess() {
        addProcessButton.setStyle("-fx-background-color: #ae8308; -fx-border-color: #b98b10; -fx-border-radius: 10; -fx-background-radius: 10;");
    }

    @FXML
    protected void onExitedAddProcess() {
        addProcessButton.setStyle("-fx-background-color: #ffde86; -fx-border-color: #b98b10; -fx-border-radius: 10; -fx-background-radius: 10;");
    }

    @FXML
    protected void onEnteredExecute() {
         executeProcessesButton.setStyle("-fx-background-color: #c8af20; -fx-border-color: #c8af20; -fx-border-radius: 10; -fx-background-radius: 10;");
    }

    @FXML
    protected void onExitedExecute() {
        executeProcessesButton.setStyle("-fx-background-color: #ffde86; -fx-border-color: #b98b10; -fx-border-radius: 10; -fx-background-radius: 10;");
    }

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

    @FXML
    protected void executeProcesses() {
        processesResults.getChildren().clear();
        List<Process> data = repository.getRRData(processes.stream().map(ProcessComponent::getProcess).toList(),
                Integer.parseInt(quantumText.getText()));
        watInfoLabel.setText("WTprom = %.2f".formatted(repository.getAverageWaitingTime(data)));
        double promNtat = repository.getAverageNormalizedTurnAroundTime(data);
        ntatInfoLabel.setText("NTATprom = %.2f".formatted(promNtat));
        ntatdesvLabel.setText("NTATdesv.est. = %.2f".formatted(repository.getNormalizedTurnAroundDeviation(data, promNtat)));
        totalTimeLabel.setText("Tiempo total = %d".formatted(repository.getTotalTime(data)));
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

    private void updateProcessesNumber() {
        for (int i = 0; i < processes.size(); i++) {
            processes.get(i).setProcessNumber(i + 1);
        }
    }

    @FXML
    public void initialize() {
        processesResults.getRowConstraints().remove(0);
        quantumText.setTextFormatter(getNumberFormatter());
    }

    private static TextFormatter<String> getNumberFormatter() {
        return new TextFormatter<>(change -> change.getText().matches("[0-9]*") ? change : null);
    }
}