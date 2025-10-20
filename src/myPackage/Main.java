package myPackage;

import java.util.*;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static int algorithmSelection() {
        int algorithmSelection;
        System.out.print("--------CPU Scheduling Algorithm---------:\n"
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

        // Compilation Time Accumulation and Gantt Chart
        int totalBurstTime = 0;
        int totalIdleTime = 0;
        int turnAroundTimeTotal = 0;
        int waitingTimeTotal = 0;
        int num = 0;

        System.out.println("Gantt Chart");
        for (CPUSceduling cpu : cpuProcesses) {

            if (cpu.getArrivalTime() > num) {
                cpu.setCompletionTime(cpu.getArrivalTime());
                System.out.print("[ IDLE |" + num + "-" + cpu.getCompletionTime() + " ]");
                totalIdleTime = cpu.getArrivalTime() - num;
                num = cpu.getArrivalTime();
            }

            if (cpu.getArrivalTime() <= num) { 
                cpu.setCompletionTime(num + cpu.getBurstTime());
                System.out.print("[ P" + cpu.getProcessID() + " | " + num);
                System.out.print("-" + cpu.getCompletionTime() + " ]");
                num = cpu.getCompletionTime();
            }

            totalBurstTime += cpu.getBurstTime(); //Accumulated Burst Time

            cpu.setTurnAroundTime(cpu.getCompletionTime() - cpu.getArrivalTime()); //Turnaround Time (TT = CT - AT)

            turnAroundTimeTotal += cpu.getTurnAroundTime();  //Accumulated Turnaround Time

            cpu.setWaitingTime(cpu.getTurnAroundTime() - cpu.getBurstTime()); //Waiting Time (WT = TT - BT)

            waitingTimeTotal += cpu.getWaitingTime(); //Accumulated Waiting Time
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
