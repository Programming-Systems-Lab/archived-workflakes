/*
 * TaskMonitorGUI.java
 *
 * Created on December 16, 2002, 7:55 PM
 */

package psl.workflakes.littlejil;

import org.apache.log4j.Logger;
import psl.worklets.WVM;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.*;
import java.util.Vector;

/**
 * This GUI can be used to test the TaskExecutorWorkletPlugin. As it receives worklets for each task,
 * the user can choose whether the task succeeds or fails. The worklet is then shipped back to the Executor plugin
 * to report on the outcome.
 *
 * @author  matias
 */
public class TaskMonitorGUI extends javax.swing.JFrame implements TaskMonitor {

    private static final Logger logger = Logger.getLogger(TaskMonitorGUI.class);

    private WVM wvm;
    private Vector tasksWaiting = new Vector();
    private TaskDecision currentTask;

    public TaskMonitorGUI(String hostname) {

        logger.debug("initializing WVM...");
        wvm = new WVM(this, hostname, "TaskMonitor");

        initComponents();

        DefaultListModel model = new DefaultListModel();
        tasksList.setModel(model);

        (new ShowTasksThread()).start();

    }

    public boolean executing(String taskName) {

        logger.info("received worklet for task " + taskName);

        TaskDecision taskDecision = new TaskDecision(taskName);
        synchronized (tasksWaiting) {
            tasksWaiting.add(taskDecision);
            tasksWaiting.notifyAll();
        }

        synchronized(taskDecision) {
            try {
                taskDecision.wait();
            } catch (InterruptedException e) {
                logger.debug("interrupted!");
            }
        }

        return taskDecision.success;

    }


    private void initComponents() {//GEN-BEGIN:initComponents
        jScrollPane2 = new javax.swing.JScrollPane();
        tasksList = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        taskText = new javax.swing.JTextField();
        failButton = new javax.swing.JButton();
        succeedButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();

        setTitle("LittleJIL Task Monitor");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jScrollPane2.setViewportView(tasksList);

        getContentPane().add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

        jPanel1.setBorder(new javax.swing.border.TitledBorder(null, "New task", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12)));
        taskText.setEditable(false);
        taskText.setForeground(Color.RED.darker());

        jPanel1.add(taskText);

        ActionListener buttonActionListener = new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonActionPerformed(evt);
            }
        };

        failButton.setText("Fail");
        failButton.addActionListener(buttonActionListener);

        jPanel1.add(failButton);

        succeedButton.setText("Succeed");
        succeedButton.addActionListener(buttonActionListener);
        jPanel1.add(succeedButton);

        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        jPanel1.add(clearButton);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        pack();
    }//GEN-END:initComponents

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        ((DefaultListModel)tasksList.getModel()).removeAllElements();
    }//GEN-LAST:event_clearButtonActionPerformed


    private void buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_failButtonActionPerformed

        if (currentTask == null)
            return;

        boolean isSuccess = (evt.getSource() == succeedButton);

        ((DefaultListModel) tasksList.getModel()).addElement(currentTask.taskName + ": " +
                (isSuccess ? "succeeded" : "failed"));

        taskText.setText("");
        currentTask.success = isSuccess;

        synchronized (currentTask) {
            currentTask.notifyAll();
        }
    }//GEN-LAST:event_failButtonActionPerformed


    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        new TaskMonitorGUI(args[0]).show();

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList tasksList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField taskText;
    private javax.swing.JButton failButton;
    private javax.swing.JButton succeedButton;
    private javax.swing.JButton clearButton;
    // End of variables declaration//GEN-END:variables


    private class TaskDecision {
        String taskName;
        boolean success;

        public TaskDecision(String taskName) {
            this.taskName = taskName;
        }
    }

    private class ShowTasksThread extends Thread {
        public void run() {

            while (true) {
                synchronized (tasksWaiting) {
                    try {
                        logger.debug("waiting for more tasks...");
                        tasksWaiting.wait();
                        logger.debug("done waiting");
                    } catch (InterruptedException e) {

                    }
                }

                while (tasksWaiting.size() > 0) {
                    logger.debug("got new task");
                    currentTask = (TaskDecision) tasksWaiting.elementAt(0);
                    tasksWaiting.removeElementAt(0);

                    taskText.setText(currentTask.taskName);

                    // now wait until the decision is made on this task
                    synchronized(currentTask) {
                        try {
                            currentTask.wait();
                        } catch (InterruptedException e) {

                        }
                    }
                }
            }

        }
    }
}
