package uptc.so.rr.procesosroundrobin.models;

/**
 * @implNote This class represents a process
 */
public class Process {

    private int burstTime;
    private int arrivalTime;
    private String name;
    private int completionTime;
    private int turnAroundTime;
    private int waitingTime;
    private double normalizedTurnAroundTime;
    private int remainingTime;
    private boolean isInQueue;

    public Process(String name, int burstTime, int arrivalTime) {
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.name = name;
        this.remainingTime = burstTime;
        this.isInQueue = false;
    }

    public boolean isInQueue() {
        return isInQueue;
    }

    public void setInQueue(boolean isInQueue) {
        this.isInQueue = isInQueue;
    }

    public Process() {
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public double getNormalizedTurnAroundTime() {
        return normalizedTurnAroundTime;
    }

    public void setNormalizedTurnAroundTime(double normalizedTurnAroundTime) {
        this.normalizedTurnAroundTime = normalizedTurnAroundTime;
    }
}
