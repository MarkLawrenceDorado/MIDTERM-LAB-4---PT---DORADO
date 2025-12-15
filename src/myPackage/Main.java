package myPackage;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

public class Main {
    private static ArrayList<CPUScheduling> cpuProcesses = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static int algorithmSelection() {
        int algorithmSelection;
        System.out.print("""
                         --------CPU Scheduling Algorithm---------
                         [1] First Come First Serve (FSFC)
                         [2] Shortest Job First (SJF) - Non-Preemtive
                         [3] Shortest Job First (SJF) - Preemtive
                         [4] Priority Scheduling - Non-Preemtive
                         [5] Priority Scheduling - Preemtive
                         
                         Select Algorithm to use: """);
        algorithmSelection = sc.nextInt();
        while (algorithmSelection <= 0 || algorithmSelection >= 6) {
            System.out.print("Invalid input. Select from (1-5):");
            algorithmSelection = sc.nextInt();
        }
        return algorithmSelection;
    }

    public static void inputs(int algorithmSelection) {
        int processes;
        while (true) {
            System.out.print("How many processes do you want? ");
            processes = sc.nextInt();
            if (processes <= 0) {
                System.out.println("Invalid. Processes must at least 1.");
            } else {
                break;
            }
        }
        
        int priorityNumber = 0;
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
            cpuProcesses.add(new CPUScheduling(processID, arrivalTime, burstTime, priorityNumber));
        }

        System.out.print("\nInput");
        switch (algorithmSelection) {
            case 1, 2, 3 -> /* FCFS, SJF/NP & SJF/P */ {
                System.out.println("\nProcess\t\tArrival Time\t\tBurst Time");
                for (CPUScheduling cpu : cpuProcesses) {
                    System.out.println(cpu.printInputFCFS_SJF());
                }
                System.out.println("______________________________________________________\n");
            }
            case 4, 5 -> /* PRIORITY/NP & PRIORITY/P */ {
                System.out.println("\nProcess\t\tArrival Time\t\tBurst Time\t\tPriority");
                for (CPUScheduling cpu : cpuProcesses) {
                    System.out.println(cpu.printInputPriority());
                }
                System.out.println("_________________________________________________________________________\n");
            }

        }
    }

    public static void output(int algorithmSelection) {
        System.out.print("\n\nOutput");
        switch (algorithmSelection) {
            case 1, 2, 3 -> /* FCFS, SJF/NP & SJF/P */ {
                System.out.println("\nProcess\t\tArrival Time\t\tBurst Time\t\tCompletion Time\t\tTurnaround Time\t\tWaiting Time");
                for (CPUScheduling cpu : cpuProcesses) {
                    System.out.println(cpu.printOutputFCFS_SJF());
                }
                System.out.print("________________________________________________________________________________________________________________________________\n");
            }
            case 4, 5 -> /* PRIORITY/NP & PRIORITY/P */ {
                System.out.println("\nProcess\t\tArrival Time\t\tBurst Time\t\tPriority\t\tCompletion Time\t\tTurnaround Time\t\tWaiting Time");
                for (CPUScheduling cpu : cpuProcesses) {
                    System.out.println(cpu.printOutputPriority());
                }
                System.out.print("____________________________________________________________________________________________________________________________________________________\n");
            }
        }
    }

    public static int FCFS() {
        int currentTime = 0;
        int totalIdleTime = 0;

        for (CPUScheduling cpu : cpuProcesses) {
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
        }
        return totalIdleTime;
    }

    public static int SJF(int algorithmSelection) {
        int completed = 0;
        int currentTime = 0;
        int totalIdleTime = 0;
        while (completed < cpuProcesses.size()) {
            ArrayList<CPUScheduling> available = new ArrayList<>();
            for (CPUScheduling cpu : cpuProcesses) {
                if (!cpu.isFinishedProcess() && cpu.getArrivalTime() <= currentTime) {
                    available.add(cpu);
                }
            }

            if (available.isEmpty()) {
                int nextArrival = Integer.MAX_VALUE;
                for (CPUScheduling cpu : cpuProcesses) {
                    if (!cpu.isFinishedProcess() && cpu.getArrivalTime() > currentTime) {
                        nextArrival = Math.min(nextArrival, cpu.getArrivalTime());
                    }
                }
                if (nextArrival == Integer.MAX_VALUE) {
                    break;
                }
                System.out.print("[ IDLE | " + currentTime + "-" + nextArrival + " ]");
                totalIdleTime += (nextArrival - currentTime);
                currentTime = nextArrival;
                continue;
            }

            CPUScheduling sjf = Collections.min(available, Comparator.comparingInt(CPUScheduling::getTotalBurstTime));
            System.out.print("[ P" + sjf.getProcessID() + " | " + currentTime + "-");
            if (algorithmSelection == 2) {
                currentTime += sjf.getTotalBurstTime();
                sjf.setTotalBurstTime(0);
                sjf.setCompletionTime(currentTime);
                sjf.setFinishedProcess(true);
                completed++;
            } else if (algorithmSelection == 3) {
                currentTime++;
                sjf.setTotalBurstTime(sjf.getTotalBurstTime() - 1);
                if (sjf.getTotalBurstTime() == 0) {
                    sjf.setFinishedProcess(true);
                    sjf.setCompletionTime(currentTime);
                    completed++;
                }
            }
            System.out.print(currentTime + " ]");
        }
        return totalIdleTime;
    }

    public static int PRIORITY(int algorithmSelection) {
        int currentTime = 0;
        int completed = 0;
        int totalIdleTime = 0;
        while (completed < cpuProcesses.size()) {
            ArrayList<CPUScheduling> available = new ArrayList<>();
            for (CPUScheduling cpu : cpuProcesses) {
                if (!cpu.isFinishedProcess() && cpu.getArrivalTime() <= currentTime) {
                    available.add(cpu);
                }
            }

            if (available.isEmpty()) {
                int nextArrival = Integer.MAX_VALUE;
                for (CPUScheduling cpu : cpuProcesses) {
                    if (!cpu.isFinishedProcess() && cpu.getArrivalTime() > currentTime) {
                        nextArrival = Math.min(nextArrival, cpu.getArrivalTime());
                    }
                }
                if (nextArrival == Integer.MAX_VALUE) {
                    break;
                }
                System.out.print("[ IDLE | " + currentTime + "-" + nextArrival + " ]");
                totalIdleTime += (nextArrival - currentTime);
                currentTime = nextArrival;
                continue;
            }

            CPUScheduling priority = Collections.max(available, Comparator.comparingInt(CPUScheduling::getPriorityNumber));
            System.out.print("[ P" + priority.getProcessID() + " | " + currentTime + "-");
            if (algorithmSelection == 4) {
                currentTime += priority.getBurstTime();
                priority.setCompletionTime(currentTime);
                priority.setFinishedProcess(true);
                completed++;
            } else if (algorithmSelection == 5) {
                currentTime++;
                priority.setTotalBurstTime(priority.getTotalBurstTime() - 1);
                if (priority.getTotalBurstTime() == 0) {
                    priority.setFinishedProcess(true);
                    priority.setCompletionTime(currentTime);
                    completed++;
                }
            }
            System.out.print(currentTime + " ]");
        }
        return totalIdleTime;
    }

    public static void performanceMetrics(double cpuUtilization, double aTT, double aWT) {
        System.out.println("\nPerformance Metrics");
        System.out.printf("CPU Utilization Percentage: %.2f%%\n", cpuUtilization);
        System.out.printf("Average Turnaround Time (ATT): %.2f\n", aTT);
        System.out.printf("Average Waiting Time (AWT): %.2f\n", aWT);
    }

    public static void main(String[] args) {
        int algorithmSelection = algorithmSelection();
        inputs(algorithmSelection);

        cpuProcesses.sort(Comparator.comparingInt(CPUScheduling::getArrivalTime));

        int totalBurstTime = 0;
        int totalIdleTime = 0;
        int totalTurnAroundTime = 0;
        int totalWaitingTime = 0;

        for (CPUScheduling cpu : cpuProcesses) {
            totalBurstTime += cpu.getBurstTime();
            cpu.setTotalBurstTime(cpu.getBurstTime());
        }
        System.out.println("Gantt Chart");
        switch (algorithmSelection) {
            case 1 -> {
                totalIdleTime = FCFS();
            }
            case 2, 3 -> {
                totalIdleTime = SJF(algorithmSelection);
            }
            case 4, 5 -> {
                totalIdleTime = PRIORITY(algorithmSelection);
            }
        }

        for (CPUScheduling cpu : cpuProcesses) {
            cpu.setTurnAroundTime(cpu.getCompletionTime() - cpu.getArrivalTime());
            totalTurnAroundTime += cpu.getTurnAroundTime();
            cpu.setWaitingTime(cpu.getTurnAroundTime() - cpu.getBurstTime());
            totalWaitingTime += cpu.getWaitingTime();
        }

        int ganttChartTotalValue = totalBurstTime + totalIdleTime;
        double cpuUtilization = ((double) totalBurstTime / (double) ganttChartTotalValue) * 100;
        double aTT = (double) totalTurnAroundTime / (double) cpuProcesses.size();
        double aWT = (double) totalWaitingTime / (double) cpuProcesses.size();

        cpuProcesses.sort(Comparator.comparingInt(CPUScheduling::getProcessID));
        output(algorithmSelection);
        performanceMetrics(cpuUtilization, aTT, aWT);
    }
}
