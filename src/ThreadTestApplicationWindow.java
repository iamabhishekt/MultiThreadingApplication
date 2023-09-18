import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

/**
 * The main window for the Thread Test Application.
 * Displays progress bars, controls, and totals for multiple threads.
 */
public class ThreadTestApplicationWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    // UI components for thread info and actions
    private JLabel[] threadTotalLabels;
    private JProgressBar[] threadProgressBars;
    private JButton startButton;
    private JButton pauseButton;
    private JButton resumeButton;
    private JLabel grandTotalLabel;
    private JLabel titleLabel;

    // Manager to handle thread operations
    private ThreadOperationManager threadManager = new ThreadOperationManager();

    /**
     * Sets up the main window's properties and layouts.
     */
    public ThreadTestApplicationWindow() {
        setupFrame();
        initializeUIComponents();
        layoutComponents();
    }

    /* UI Components */

    /**
     * Initializes the main properties of the application window.
     */
    private void setupFrame() {
        setTitle("Thread Test Application");
        setSize(545, 190);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        int marginSize = 10;
        ((JComponent) getContentPane())
                .setBorder(BorderFactory.createEmptyBorder(marginSize, marginSize, marginSize, marginSize));
        getContentPane().setLayout(new BorderLayout(0, 0));
    }

    /**
     * Initializes UI components including progress bars, labels, and buttons.
     */
    private void initializeUIComponents() {
        titleLabel = new JLabel("Thread Test Application");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        threadTotalLabels = new JLabel[4];
        threadProgressBars = new JProgressBar[4];

        // Setup buttons and their action listeners
        startButton = new JButton("Start");
        startButton.addActionListener(this::handleStartAction);
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(this::handlePauseAction);
        resumeButton = new JButton("Resume");
        resumeButton.addActionListener(this::handleResumeAction);

        grandTotalLabel = new JLabel("Grand Total: 0");
    }

    /**
     * Arranges the initialized UI components on the window.
     */
    private void layoutComponents() {
        getContentPane().add(titleLabel, BorderLayout.NORTH);

        JPanel threadDisplayPanel = new JPanel(new GridBagLayout());
        getContentPane().add(threadDisplayPanel, BorderLayout.CENTER);

        setupThreadDisplayComponents(threadDisplayPanel);
        setupButtonPanel(threadDisplayPanel);
        setupGrandTotalPanel(threadDisplayPanel);
    }

    /**
     * Arranges the thread-related display components like labels and progress bars.
     */
    private void setupThreadDisplayComponents(JPanel threadDisplayPanel) {
        for (int i = 0; i < 4; i++) {
            JLabel threadLabel = new JLabel((i + 1) + ": ");
            threadLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            GridBagConstraints threadLabelConstraints = new GridBagConstraints();
            threadLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
            threadLabelConstraints.weightx = 0;
            threadLabelConstraints.weighty = 1;
            threadLabelConstraints.gridx = 0;
            threadLabelConstraints.gridy = i;
            threadLabelConstraints.insets = new Insets(5, 5, 5, 2);
            threadDisplayPanel.add(threadLabel, threadLabelConstraints);

            threadProgressBars[i] = new JProgressBar();
            GridBagConstraints progressBarConstraints = (GridBagConstraints) threadLabelConstraints.clone();
            progressBarConstraints.gridx = 1;
            progressBarConstraints.weightx = 1;
            progressBarConstraints.gridwidth = 2;
            progressBarConstraints.insets = new Insets(5, 2, 5, 5);
            threadDisplayPanel.add(threadProgressBars[i], progressBarConstraints);

            threadTotalLabels[i] = new JLabel("0");
            GridBagConstraints threadTotalConstraints = (GridBagConstraints) threadLabelConstraints.clone();
            threadTotalConstraints.gridx = 3;
            threadTotalConstraints.gridwidth = 1;
            threadDisplayPanel.add(threadTotalLabels[i], threadTotalConstraints);
        }
    }

    /**
     * Sets up and arranges the control buttons on the window.
     */
    private void setupButtonPanel(JPanel threadDisplayPanel) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(resumeButton);

        GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
        buttonPanelConstraints.insets = new Insets(0, 0, 0, 5);
        buttonPanelConstraints.gridy = 4;
        buttonPanelConstraints.gridx = 1;
        buttonPanelConstraints.gridwidth = 1;
        buttonPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonPanelConstraints.weighty = 1;
        threadDisplayPanel.add(buttonPanel, buttonPanelConstraints);
    }

    /**
     * Sets up and arranges the grand total display on the window.
     */
    private void setupGrandTotalPanel(JPanel threadDisplayPanel) {
        JPanel grandTotalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        grandTotalPanel.add(grandTotalLabel);

        GridBagConstraints grandTotalPanelConstraints = new GridBagConstraints();
        grandTotalPanelConstraints.gridy = 4;
        grandTotalPanelConstraints.gridx = 2;
        grandTotalPanelConstraints.gridwidth = 2;
        grandTotalPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        grandTotalPanelConstraints.weighty = 1;
        threadDisplayPanel.add(grandTotalPanel, grandTotalPanelConstraints);
    }

    /* Thread Logic */

    // Action when 'Start' button is clicked
    private void handleStartAction(ActionEvent e) {
        initializeThreads();
        startAllThreads();
        startButton.setEnabled(true);
        pauseButton.setEnabled(true);
        resumeButton.setEnabled(false);
    }

    // Action when 'Pause' button is clicked
    private void handlePauseAction(ActionEvent e) {
        pauseAllThreads();
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(true);
    }

    // Action when 'Resume' button is clicked
    private void handleResumeAction(ActionEvent e) {
        resumeAllThreads();
        pauseButton.setEnabled(true);
        resumeButton.setEnabled(false);
    }

    // Initialize Thread Method for action listener
    private void initializeThreads() {
        double[] threadSleepIntervals = { 400, 300, 500, 200 };
        for (int i = 0; i < 4; i++) {
            ThreadTaskControl task = threadManager.getTasks()[i];
            if (task instanceof ThreadTask) {
                ThreadTask threadTask = (ThreadTask) task;
                if (threadTask.isAlive()) {
                    threadTask.interrupt();
                }
            }
            threadProgressBars[i].setValue(0);
            threadTotalLabels[i].setText("0");
        }
        grandTotalLabel.setText("Grand Total: 0");
        threadManager.initializeTasks(threadProgressBars, threadTotalLabels, grandTotalLabel, threadSleepIntervals);
    }

    // start all threads method from Thread Operation Manager
    private void startAllThreads() {
        threadManager.startAllTasks();
    }

    // Pause all threads method
    private void pauseAllThreads() {
        threadManager.pauseAllTasks();
    }

    // Resume all threads method
    private void resumeAllThreads() {
        threadManager.resumeAllTasks();
    }

    // Interface for controlling thread tasks
    interface ThreadTaskControl {

        void startTask();

        void pauseTask();

        void resumeTask();
    }

    /**
     * Represents a single thread task that updates its progress and grand total.
     * This class is controllable (start, pause, resume) and its progress can be
     * visualized in the UI.
     */
    public class ThreadTask extends Thread implements ThreadTaskControl {

        /**
         * Constructor to initialize thread with associated UI components.
         *
         * @param progressBar      The UI progress bar associated with this thread.
         * @param threadTotalLabel The label showing this thread's total.
         * @param grandTotalLabel  The label showing the grand total across all threads.
         * @param sleepInterval    The interval for the thread to sleep.
         */

        private JProgressBar progressBar;
        private JLabel threadTotalLabel;
        private JLabel grandTotalLabel;
        private double sleepInterval;
        private volatile boolean paused = false;
        private static final Object GRAND_TOTAL_LOCK = new Object();

        public ThreadTask(JProgressBar progressBar, JLabel threadTotalLabel, JLabel grandTotalLabel,
                double sleepInterval) {
            this.progressBar = progressBar;
            this.threadTotalLabel = threadTotalLabel;
            this.grandTotalLabel = grandTotalLabel;
            this.sleepInterval = sleepInterval;
        }

        /**
         * Core logic of the thread. Updates progress bar, thread total, and grand
         * total.
         */
        @Override
        public void run() {
            for (int i = 1; i <= 100 && !isInterrupted(); i++) {
                synchronized (this) {
                    while (paused) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }

                final int progressValue = i;
                SwingUtilities.invokeLater(() -> {
                    progressBar.setValue(progressValue);

                    synchronized (GRAND_TOTAL_LOCK) {
                        String[] parts = grandTotalLabel.getText().split(" ");
                        int grandTotal = Integer.parseInt(parts[parts.length - 1]);

                        int threadTotal = Integer.parseInt(threadTotalLabel.getText());
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            return;
                        }

                        grandTotalLabel.setText("Grand Total: " + (grandTotal + 1));
                        threadTotalLabel.setText(Integer.toString(threadTotal + 1));
                    }
                });

                try {
                    long millis = (long) sleepInterval;
                    int nanos = (int) ((sleepInterval - millis) * 1_000_000); // Convert fractional part to nanoseconds
                    Thread.sleep(millis, nanos);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }

        @Override
        public void startTask() {
            this.start();
        }

        @Override
        public synchronized void pauseTask() {
            paused = true;
        }

        @Override
        public synchronized void resumeTask() {
            paused = false;
            notify();
        }
    }

    /**
     * A manager to control operations and states of multiple thread tasks.
     * Provides facilities to start, pause, and resume all thread tasks.
     */
    public class ThreadOperationManager {

        private ThreadTaskControl[] tasks = new ThreadTaskControl[4];

        // Initializes the thread tasks.
        public void initializeTasks(JProgressBar[] threadProgressBars, JLabel[] threadTotalLabels,
                JLabel grandTotalLabel, double[] threadSleepIntervals) {
            for (int i = 0; i < 4; i++) {
                tasks[i] = new ThreadTask(threadProgressBars[i], threadTotalLabels[i], grandTotalLabel,
                        threadSleepIntervals[i]);
            }
        }

        public ThreadTaskControl[] getTasks() {
            return tasks;
        }

        public void startAllTasks() {
            for (int i = 0; i < tasks.length; i++) {
                tasks[i].startTask();
            }
        }

        public void pauseAllTasks() {
            for (ThreadTaskControl task : tasks) {
                task.pauseTask();
            }
        }

        public void resumeAllTasks() {
            for (ThreadTaskControl task : tasks) {
                task.resumeTask();
            }
        }
    }

    /* Main Class */
    /**
     * The main entry point of the application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ThreadTestApplicationWindow().setVisible(true);
        });
    }
}