package uptc.so.rr.procesosroundrobin.domain;

import uptc.so.rr.procesosroundrobin.models.Process;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

/**
 * @implNote this class implements the {@link Manager} interface
 */
@Component
@Scope(SCOPE_SINGLETON)
public class RRManager implements Manager {

    /*
     * timeCount is the current time for the processes execution
     */
    private int timeCount = 0;

    /**
     * Executes the Round Robin algorithm for a list of processes with a given quantum,
     * and returns the list of processes executed.
     * @param processes the list of processes to be executed
     * @param quantum  the quantum of time to be allocated for each process
     * @return the list of processes with their completion times,
     * turn around times, waiting times and normalized turn around times calculated
     */
    @Override
    public List<Process> getRRData(List<Process> processes, int quantum) {
        Process firstProcess = processes.stream().min(Comparator.comparing(Process::getArrivalTime))
                .orElse(new Process());
        timeCount = firstProcess.getArrivalTime();
        Queue<Process> queue = new LinkedList<>();
        queue.add(firstProcess);
        firstProcess.setInQueue(true);
        while (processes.stream().anyMatch(p -> p.getRemainingTime() > 0)) {
            while (!queue.isEmpty()) {
                Process process = queue.poll();
                if (timeCount == 0) timeCount = process.getArrivalTime();
                if (process.getRemainingTime() >= quantum) {
                    timeCount += quantum;
                    process.setRemainingTime(process.getRemainingTime() - quantum);
                } else {
                    timeCount += process.getRemainingTime();
                    process.setRemainingTime(0);
                }
                process.setCompletionTime(timeCount);
                queue.addAll(processes.stream().filter(p -> {
                    boolean toQueue = p.getArrivalTime() <= timeCount && p.getRemainingTime() > 0
                            && !p.isInQueue();
                    if (toQueue) p.setInQueue(true);
                    return toQueue;
                }).sorted(Comparator.comparing(Process::getArrivalTime)).toList());
                boolean notTerminated = processes.stream().anyMatch(p -> p.getRemainingTime() > 0);
                if (notTerminated && queue.isEmpty()) {
                    int nextArrivalTime = processes.stream().filter(p -> p.getRemainingTime() > 0)
                            .mapToInt(Process::getArrivalTime).min().orElse(0);
                    if (nextArrivalTime > timeCount) {
                        int dif = nextArrivalTime - timeCount;
                        timeCount += dif;
                    }
                    queue.addAll(processes.stream()
                            .filter(p -> p.getArrivalTime() <= timeCount && p.getRemainingTime() > 0)
                            .sorted(Comparator.comparing(Process::getArrivalTime)).toList());
                }
            }
        }
        processes.forEach(process -> {
            process.setTurnAroundTime(process.getCompletionTime() - process.getArrivalTime());
            process.setWaitingTime(process.getTurnAroundTime() - process.getBurstTime());
            process.setNormalizedTurnAroundTime( process.getBurstTime() != 0 ?
                    process.getTurnAroundTime() / (double) process.getBurstTime() : 0);
        });
        return processes;
    }


    /**
     * Returns the average waiting time for a list of processes.
     * @param processes the list of processes.
     * @return the average waiting time.
     */
    @Override
    public double getAverageWaitingTime(List<Process> processes) {
        return processes.stream().mapToDouble(Process::getWaitingTime).sum() / ((double) processes.size());
    }

    /**
     * Returns the average normalized turn around time for a list of processes.
     * @param processes the list of processes.
     * @return the average normalized turn around time.
     */
    @Override
    public double getAverageNormalizedTurnAroundTime(List<Process> processes) {
        return processes.stream().mapToDouble(Process::getNormalizedTurnAroundTime).sum() / ((double) processes.size());
    }

    /**
     * Calculates the standard deviation of the normalized turn around time of the given list of processes.
     * @param processes the list of processes.
     * @param averageNormalizedTurnAroundTime the average normalized turn around time of the processes.
     * @return the standard deviation of the normalized turn around time.
     */
    @Override
    public double getNormalizedTurnAroundDeviation(List<Process> processes, double averageNormalizedTurnAroundTime) {
        return Math.sqrt(processes.stream().mapToDouble(process -> Math
                .pow(process.getNormalizedTurnAroundTime() - averageNormalizedTurnAroundTime, 2)).sum() /
                ((double) processes.size()));
    }

    /**
     * Calculates the total time needed for all the processes to finish.
     * @param processes the list of processes.
     * @return the total time needed for all the processes to finish.
     */
    @Override
    public int getTotalTime(List<Process> processes) {
        return processes.stream().mapToInt(Process::getCompletionTime).max()
                .orElse(0);
    }

}
