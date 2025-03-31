package uptc.so.rr.procesosroundrobin.domain;

import uptc.so.rr.procesosroundrobin.controllers.Controller;
import uptc.so.rr.procesosroundrobin.models.Process;

import java.util.List;

/**
 * @implNote This interface is used by the {@link Controller}
 */
public interface Manager {

    List<Process> getRRData(List<Process> processes, int quantum);

    double getAverageWaitingTime(List<Process> processes);

    double getAverageNormalizedTurnAroundTime(List<Process> processes);

    double getNormalizedTurnAroundDeviation(List<Process> processes, double averageNormalizedTurnAroundTime);

    int getTotalTime(List<Process> processes);
}
