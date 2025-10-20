package myPackage;

public class CPUSceduling {

    private int processID;
    private int arrivalTime;
    private int burstTime;
    private int priorityNumber;
    private int cpuUtilization;
    private int totalBurstTime;
    private int waitingTime;
    private int turnAroundTime;
    private int completionTime;
    private int idleTime;
    private boolean finishedProcess;

    public CPUSceduling(int processID, int arrivalTime, int burstTime, int priorityNumber) {
        this.processID = processID;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
        this.finishedProcess = false;
    }

    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getPriorityNumber() {
        return priorityNumber;
    }

    public void setPriorityNumber(int priorityNumber) {
        this.priorityNumber = priorityNumber;
    }

    public int getCpuUtilization() {
        return cpuUtilization;
    }

    public void setCpuUtilization(int cpuUtilization) {
        this.cpuUtilization += cpuUtilization;
    }

    public int getTotalBurstTime() {
        return totalBurstTime;
    }

    public void setTotalBurstTime(int totalBurstTime) {
        this.totalBurstTime = totalBurstTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int compilationTime) {
        this.completionTime = compilationTime;
    }

    public String printInputFCFS_SJF() {
        return "______________________________________"
                + "________________\n"
                + "P" + getProcessID()
                + "\t\t" + getArrivalTime()
                + "\t\t\t" + getBurstTime();
    }

    public String printInputPriority() {
        return "______________________________________"
                + "___________________________________\n"
                + "P" + getProcessID()
                + "\t\t" + getArrivalTime()
                + "\t\t\t" + getBurstTime()
                + "\t\t\t" + getPriorityNumber();
    }

    public String printOutputFCFS_SJF() {
        return "_________________________________________________________________"
                + "_______________________________________________________________\n"
                + "P" + getProcessID() + "\t\t" + getArrivalTime()
                + "\t\t\t" + getBurstTime() + "\t\t\t" + getCompletionTime()
                + "\t\t\t" + getTurnAroundTime() + "\t\t\t" + getWaitingTime();
    }

    public String printOutputPriority() {
        return "_________________________________________________________________"
                + "_______________________________________________________________"
                + "____________________\n"
                + "P" + getProcessID() + "\t\t" + getArrivalTime()
                + "\t\t\t" + getBurstTime() + "\t\t\t" + getPriorityNumber()
                + "\t\t\t" + getCompletionTime()
                + "\t\t\t" + getTurnAroundTime() + "\t\t\t" + getWaitingTime();
    }

}
