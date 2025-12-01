 package myPackage;

import java.util.*;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static int algorithmSelection() {
        int algorithmSelection;
        System.out.print("--------CPU Scheduling Algorithm---------\n"
                + "[1] First Come First Serve (FSFC)\n"
                + "[2] Shortest Job First (SJF) - Non-Preemtive\n"
                + "[3] Shortest Job First (SJF) - Preemtive\n"
                + "[4] Priority Scheduling - Non-Preemtive\n"
                + "[5] Priority Scheduling - Preemtive\n"
                + "\nSelect Algorithm to use: ");
        algorithmSelection = sc.nextInt();
        while (algorithmSelection <= 0 || algorithmSelection >= 6) {
            System.out.print("Invalid input. Select from (1-5):");
            algorithmSelection = sc.nextInt();
        }

        return algorithmSelection;
    }

    public static void inputs(ArrayList<CPUSceduling> cpuProcesses, int algorithmSelection) {
        int priorityNumber = 0;

        System.out.print("How many processes do you want? ");
        int processes = sc.nextInt();

        for (int i = 0; i < processes; i++) {
            System.out.println("\nProcess no. " + (i + 1));
            int processID = i + 1;
            System.out.print("Arrival Time: ");
            int arrivalTime = sc.nextInt();
            System.out.print("Burst Time: ");
            int burstTime = sc.nextInt();
            if (algorithmSelection == 4 || algorithmSelection == 5) {
                System.out.print("Priority: ");
                priorityNumber = sc.nextInt();
            }
            cpuProcesses.add(new CPUSceduling(processID, arrivalTime, burstTime, priorityNumber));
        }

        System.out.print("\nInput");
        switch (algorithmSelection) {
            case 1, 2, 3 -> /* FCFS, SJF/NP & SJF/P */ {
                System.out.println("\nProcess\t\tArrival Time\t\tBurst Time");
                for (CPUSceduling cpu : cpuProcesses) {
                    System.out.println(cpu.printInputFCFS_SJF());
                }
                System.out.println("______________________________________"
                        + "________________\n");
            }
            case 4, 5 -> /* PRIORITY/NP & PRIORITY/P */ {
                System.out.println("\nProcess\t\tArrival Time\t\tBurst Time\t\tPriority");
                for (CPUSceduling cpu : cpuProcesses) {
                    System.out.println(cpu.printInputPriority());
                }
                System.out.println("______________________________________"
                        + "___________________________________\n");
            }

        }
    }

    public static void output(ArrayList<CPUSceduling> cpuProcesses, int algorithmSelection) {
        System.out.print("\n\nOutput");
        switch (algorithmSelection) {
            case 1, 2, 3 -> /* FCFS, SJF/NP & SJF/P */ {
                System.out.println("\nProcess\t\tArrival Time\t\tBurst Time\t\tCompletion Time\t\tTurnaround Time\t\tWaiting Time");
                for (CPUSceduling cpu : cpuProcesses) {
                    System.out.println(cpu.printOutputFCFS_SJF());
                }
                System.out.print("_________________________________________________________________"
                        + "_______________________________________________________________\n");
            }
            case 4, 5 -> /* PRIORITY/NP & PRIORITY/P */ {
                System.out.println("\nProcess\t\tArrival Time\t\tBurst Time\t\tPriority\t\tCompletion Time\t\tTurnaround Time\t\tWaiting Time");
                for (CPUSceduling cpu : cpuProcesses) {
                    System.out.println(cpu.printOutputPriority());
                }
                System.out.print("_________________________________________________________________"
                        + "_______________________________________________________________"
                        + "____________________\n");
            }
        }

    }

    public static void performanceMetrics(double cpuUtilization, double aTT, double aWT) {
        System.out.println("\nPerformance Metrics");
        System.out.println("CPU Utilization Percentage: " + cpuUtilization + "%");
        System.out.println("Average Turnaround Time (ATT): " + aTT);
        System.out.println("Average Waiting Time (AWT): " + aWT);
    }

    public static void main(String[] args) {
        ArrayList<CPUSceduling> cpuProcesses = new ArrayList<>();

        int algorithmSelection = algorithmSelection();

        inputs(cpuProcesses, algorithmSelection);

        Collections.sort(cpuProcesses, Comparator.comparingInt(cpu -> cpu.getArrivalTime()));

        // Time Accumulations and Gantt Chart
        int totalBurstTime = 0;
        int totalIdleTime = 0;
        int turnAroundTimeTotal = 0;
        int waitingTimeTotal = 0;
        int aCT = 0;

        System.out.println("Gantt Chart");

        int completeProcess = 0;

        ArrayList<CPUSceduling> newl = new ArrayList<>();

        int currentTime = 0;
        int completed = 0;
        int cpuProcessesSize = cpuProcesses.size();
        int lastArrival = Collections.max(cpuProcesses, Comparator.comparingInt(CPUSceduling::getArrivalTime)).getArrivalTime();

        switch (algorithmSelection) {
            case 1: //FCFS
                for (CPUSceduling cpu : cpuProcesses) {

                    if (cpu.getArrivalTime() > currentTime) {
                        cpu.setCompletionTime(cpu.getArrivalTime());
                        System.out.print("[ IDLE |" + currentTime + "-" + cpu.getCompletionTime() + " ] ");
                        totalIdleTime += cpu.getArrivalTime() - currentTime;
                        currentTime = cpu.getArrivalTime();
                    }

                    if (cpu.getArrivalTime() <= currentTime) {
                        cpu.setCompletionTime(currentTime + cpu.getBurstTime());
                        System.out.print("[ P" + cpu.getProcessID() + " | " + currentTime + "-" + cpu.getCompletionTime() + " ]");
                        currentTime = cpu.getCompletionTime();
                    }

                    totalBurstTime += cpu.getBurstTime();

                    cpu.setTurnAroundTime(cpu.getCompletionTime() - cpu.getArrivalTime());
                    turnAroundTimeTotal += cpu.getTurnAroundTime();
                    cpu.setWaitingTime(cpu.getTurnAroundTime() - cpu.getBurstTime());
                    waitingTimeTotal += cpu.getWaitingTime();
                }
                System.out.print("\n\nFirst Come First Serve");
                break;
            case 2: //SJF/NP
                while (completed < cpuProcessesSize) {
                    ArrayList<CPUSceduling> available = new ArrayList<>();
                    for (CPUSceduling cpu : cpuProcesses) {
                        if (!cpu.isFinishedProcess() && cpu.getArrivalTime() <= currentTime) {
                            available.add(cpu);
                        }
                    }
                    
                    if (available.isEmpty()) {
                        int nextArrival = Integer.MAX_VALUE;
                        for (CPUSceduling cpu : cpuProcesses) {
                            if (!cpu.isFinishedProcess()) {
                                nextArrival = Math.min(nextArrival, cpu.getArrivalTime());
                            }
                        }
                        System.out.print("[ IDLE | " + currentTime + "-" + nextArrival + " ]");
                        totalIdleTime += (nextArrival - currentTime);
                        currentTime = nextArrival;
                        continue;
                    }

                    CPUSceduling shortestProcess = Collections.min(available, Comparator.comparingInt(CPUSceduling::getBurstTime));

                    System.out.print("[ P" + shortestProcess.getProcessID() + " | " + currentTime + "-");
                    currentTime += shortestProcess.getBurstTime();
                    shortestProcess.setCompletionTime(currentTime);
                    shortestProcess.setFinishedProcess(true);
                    System.out.print(shortestProcess.getCompletionTime() + " ]");

                    shortestProcess.setTurnAroundTime(shortestProcess.getCompletionTime() - shortestProcess.getArrivalTime());
                    shortestProcess.setWaitingTime(shortestProcess.getTurnAroundTime() - shortestProcess.getBurstTime());

                    totalBurstTime += shortestProcess.getBurstTime();
                    turnAroundTimeTotal += shortestProcess.getTurnAroundTime();
                    waitingTimeTotal += shortestProcess.getWaitingTime();

                    completed++;
                }
                System.out.print("\n\nShortest Job First / Non - Preemtive");
                break;
            case 3: //SJF/P
                for (CPUSceduling cpu : cpuProcesses) {
                    cpu.setTotalBurstTime(cpu.getBurstTime());
                }

                while (completed < cpuProcessesSize) {
                    ArrayList<CPUSceduling> available = new ArrayList<>();
                    for (CPUSceduling cpu : cpuProcesses) {
                        if (!cpu.isFinishedProcess() && cpu.getArrivalTime() <= currentTime) {
                            available.add(cpu);
                        }
                    }

                    if (available.isEmpty()) {
                        int nextArrival = Integer.MAX_VALUE;
                        for (CPUSceduling cpu : cpuProcesses) {
                            if (!cpu.isFinishedProcess()) {
                                nextArrival = Math.min(nextArrival, cpu.getArrivalTime());
                            }
                        }
                        System.out.print("[ IDLE | " + currentTime + "-" + nextArrival + " ]");
                        totalIdleTime += (nextArrival - currentTime);
                        currentTime = nextArrival;
                        continue;
                    }

                    CPUSceduling current = Collections.min(available, Comparator.comparingInt(CPUSceduling::getTotalBurstTime));

                    boolean allArrived = currentTime >= lastArrival;

                    System.out.print("[ P" + current.getProcessID() + " | " + currentTime + "-");

                    if (allArrived) {
                        currentTime += current.getTotalBurstTime();
                        current.setTotalBurstTime(0);
                    } else {
                        currentTime++;
                        current.setTotalBurstTime(current.getTotalBurstTime() - 1);
                    }

                    System.out.print(currentTime + " ]");

                    if (current.getTotalBurstTime() == 0) {
                        current.setFinishedProcess(true);
                        current.setCompletionTime(currentTime);
                        completed++;
                    }
                }

                for (CPUSceduling cpu : cpuProcesses) {
                    cpu.setTurnAroundTime(cpu.getCompletionTime() - cpu.getArrivalTime());
                    cpu.setWaitingTime(cpu.getTurnAroundTime() - cpu.getBurstTime());
                    turnAroundTimeTotal += cpu.getTurnAroundTime();
                    waitingTimeTotal += cpu.getWaitingTime();
                    totalBurstTime += cpu.getBurstTime();
                }
                
                System.out.print("\n\nShortest Job First / Preemtive");
                break;
            case 4: //PRIORITY/NP
                while (completed < cpuProcessesSize) {
                    // Get all available processes that have arrived
                    ArrayList<CPUSceduling> available = new ArrayList<>();
                    for (CPUSceduling cpu : cpuProcesses) {
                        if (!cpu.isFinishedProcess() && cpu.getArrivalTime() <= currentTime) {
                            available.add(cpu);
                        }
                    }

                    // If no process has arrived yet → CPU idle
                    if (available.isEmpty()) {
                        int nextArrival = Integer.MAX_VALUE;
                        for (CPUSceduling cpu : cpuProcesses) {
                            if (!cpu.isFinishedProcess()) {
                                nextArrival = Math.min(nextArrival, cpu.getArrivalTime());
                            }
                        }
                        System.out.print("[ IDLE | " + currentTime + "-" + nextArrival + " ]");
                        totalIdleTime += (nextArrival - currentTime);
                        currentTime = nextArrival;
                        continue;
                    }

                    CPUSceduling highestPriority = Collections.max(available, Comparator.comparingInt(CPUSceduling::getPriorityNumber));

                    System.out.print("[ P" + highestPriority.getProcessID() + " | " + currentTime + "-");
                    currentTime += highestPriority.getBurstTime();
                    highestPriority.setCompletionTime(currentTime);
                    highestPriority.setFinishedProcess(true);
                    System.out.print(highestPriority.getCompletionTime() + " ]");

                    highestPriority.setTurnAroundTime(highestPriority.getCompletionTime() - highestPriority.getArrivalTime());
                    highestPriority.setWaitingTime(highestPriority.getTurnAroundTime() - highestPriority.getBurstTime());

                    totalBurstTime += highestPriority.getBurstTime();
                    turnAroundTimeTotal += highestPriority.getTurnAroundTime();
                    waitingTimeTotal += highestPriority.getWaitingTime();

                    completed++;
                }
                System.out.print("\n\nPriority / Non - Preemtive");
                break;
            case 5: //PRIORITY/P
                for (CPUSceduling cpu : cpuProcesses) {
                    cpu.setTotalBurstTime(cpu.getBurstTime());
                }

                while (completed < cpuProcessesSize) {
                    ArrayList<CPUSceduling> available = new ArrayList<>();
                    for (CPUSceduling cpu : cpuProcesses) {
                        if (!cpu.isFinishedProcess() && cpu.getArrivalTime() <= currentTime) {
                            available.add(cpu);
                        }
                    }

                    if (available.isEmpty()) {
                        int nextArrival = Integer.MAX_VALUE;
                        for (CPUSceduling cpu : cpuProcesses) {
                            if (!cpu.isFinishedProcess()) {
                                nextArrival = Math.min(nextArrival, cpu.getArrivalTime());
                            }
                        }
                        System.out.print("[ IDLE | " + currentTime + "-" + nextArrival + " ]");
                        totalIdleTime += (nextArrival - currentTime);
                        currentTime = nextArrival;
                        continue;
                    }

                    CPUSceduling current = Collections.max(available, Comparator.comparingInt(CPUSceduling::getPriorityNumber));

                    boolean allArrived = currentTime >= lastArrival;

                    System.out.print("[ P" + current.getProcessID() + " | " + currentTime + "-");

                    if (allArrived) {
                        currentTime += current.getTotalBurstTime();
                        current.setTotalBurstTime(0);
                    } else {
                        currentTime++;
                        current.setTotalBurstTime(current.getTotalBurstTime() - 1);
                    }

                    System.out.print(currentTime + " ]");

                    if (current.getTotalBurstTime() == 0) {
                        current.setFinishedProcess(true);
                        current.setCompletionTime(currentTime);
                        completed++;
                    }
                }

                for (CPUSceduling cpu : cpuProcesses) {
                    cpu.setTurnAroundTime(cpu.getCompletionTime() - cpu.getArrivalTime());
                    cpu.setWaitingTime(cpu.getTurnAroundTime() - cpu.getBurstTime());
                    turnAroundTimeTotal += cpu.getTurnAroundTime();
                    waitingTimeTotal += cpu.getWaitingTime();
                    totalBurstTime += cpu.getBurstTime();
                }
                System.out.print("\n\nPriority / Preemtive");
                break;
        }

        int ganttChartTotalValue = totalBurstTime + totalIdleTime;

        double cpuUtilization = ((double) totalBurstTime / (double) ganttChartTotalValue) * 100;

        double aTT = (double) turnAroundTimeTotal / (double) cpuProcesses.size();

        double aWT = (double) waitingTimeTotal / (double) cpuProcesses.size();

        Collections.sort(cpuProcesses, Comparator.comparingInt(s -> s.getProcessID()));

        output(cpuProcesses, algorithmSelection);

        performanceMetrics(cpuUtilization, aTT, aWT);
    }
}
