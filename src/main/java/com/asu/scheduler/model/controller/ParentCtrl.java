/*FCFS, SJF-P, SJF-NP*/
package com.asu.scheduler.model.controller;

import com.asu.scheduler.model.process.Process;
import com.asu.scheduler.ProcessorSchedulerApplication;
import com.asu.scheduler.model.processor.Processor;
import com.asu.scheduler.model.processor.fcfs.FCFSProcessor;

import com.asu.scheduler.model.processor.rr.RRProcessor;
import com.asu.scheduler.model.processor.sjf.SJFNPProcessor;
import com.asu.scheduler.model.processor.sjf.SJFPProcessor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static com.asu.scheduler.ProcessorSchedulerApplication.*;
import static com.asu.scheduler.ProcessorSchedulerApplication.scene;


public class ParentCtrl implements Initializable {

    @FXML
    private Text AT_label;

    @FXML
    private TextField Arrival_Time;

    @FXML
    private Text BT_label;

    @FXML
    private TextField Burst_Time;

    @FXML
    private Button Solve_Button;

    @FXML
    private TableView<Process> table;

    @FXML
    private TableColumn<Process, Integer> arrivalTimeColumn;

    @FXML
    private TableColumn<Process, Integer> burstTimeColumn;

    @FXML
    private TableColumn<Process, Integer> priorityColumn;

    @FXML
    private TableColumn<Process, String> PIDColumn;

//    private int pidCounter = 1;
    @FXML
    private TableColumn<Process, Integer> remainingTimeColumn;

    @FXML
    private Button PreviousButton;
    String fxmlFileName = "";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        PIDColumn.setCellValueFactory(new PropertyValueFactory<Process, String>("pid"));

        arrivalTimeColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("arrivalTime"));
        burstTimeColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("burstTime"));
        remainingTimeColumn.setCellValueFactory(new PropertyValueFactory<Process, Integer>("remainingTime"));
        Process.pidCounter=1;
    }
    @FXML
    void Add(ActionEvent event){
        Process process = new Process(
                Integer.parseInt(Arrival_Time.getText()),
                Integer.parseInt(Burst_Time.getText())
                );

        //pidCounter++;
//        ObservableList<Process> currentTableData = table.getItems();
        processes= table.getItems();
        addProcess(process);
        table.setItems(processes);
//        System.out.println(Process.pidString);

        Arrival_Time.clear();
        Burst_Time.clear();


    }

    @FXML
    void Solve(ActionEvent event) {
        // Change the processor type here
        if (AlgoCtrl.selectedScene.equals("FCFS")){
            initProcessor(new FCFSProcessor());
        }
        else if (AlgoCtrl.selectedScene.equals("SJF-NP")){
            initProcessor(new SJFNPProcessor());
        }
        else if (AlgoCtrl.selectedScene.equals("SJF-P")){
            initProcessor(new SJFPProcessor());
        }
        else if(AlgoCtrl.selectedScene.equals("Round Robin")){
            initProcessor(new RRProcessor(RR_Ctrl.quantum));
        }

        // Add test data according to the processor type
//        addTempData();

//        StringBuilder ganntChart = new StringBuilder();
//        ganntChart
//                .append("Gannt Chart for ")
//                .append(processor.getProcessorType())
//                .append(" Processor:\n");

        // Simulate the application
        while (true) {
            // Add new processes to the processor
            populateProcesses();

            // Run the processor
            processor.execute();

            // Increment the global time
            com.asu.scheduler.model.GlobalStopWatch.incrementTime();

            if (processes.size() == 0 && processor.state == Processor.ProcessorState.IDLE) {
                // System.out.println(ganntChart.toString());
                // System.out.println("\nAll processes have been terminated");
                break;
                // System.exit(0);
            } else {
                // ganntChart.append(processor.getCurrentProcess() == null ? "I" : "P(" + processor.getCurrentProcess().getPID() + ") ");
                if (processor.getCurrentProcess().color == null) {
                    processor.getCurrentProcess().color = Color.rgb(
                            (int) (Math.random() * 256),
                            (int) (Math.random() * 256),
                            (int) (Math.random() * 256)
                    );
                }

                ganttChartProcesses.add(processor.getCurrentProcess());
            }
        }

        // Debugging
        // for (Process process : ganttChartProcesses) {
        //     System.out.print(process.getPID() + " -> ");
        // }

        // Initialize the Gantt Chart Scene
        initGanttChartScene();
        Stage stage = (Stage) Solve_Button.getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    private void PrevScene(ActionEvent event) throws IOException {
        fxmlFileName = "AlgoScene.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(ProcessorSchedulerApplication.class.getResource(fxmlFileName));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        Stage stage = (Stage) PreviousButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleSceneSelection(ActionEvent event) throws IOException {

    }

}

