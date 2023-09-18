import java.awt.*;
import javax.swing.*;

public class ThreadTestApplicationWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    private JLabel[] threadTotals;
    private JProgressBar[] progressBars;
    private JButton startButton;
    private JButton pauseButton;
    private JButton resumeButton;
    private JLabel grandTotalLabel;
    private JLabel titleLabel;

    // Thread Operation Manager variable
    private ThreadOperationManager threadManager = new ThreadOperationManager();

    public ThreadTestApplicationWindow() {
        setupFrame();
        initializeComponents();
        layoutComponents();
    }

    private void setupFrame() {
        setTitle("Thread Test Application");
        setSize(545, 190);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        int marginSize = 10;
        ((JComponent) getContentPane())
                .setBorder(BorderFactory.createEmptyBorder(marginSize, marginSize, marginSize, marginSize));
        getContentPane().setLayout(new BorderLayout(0, 0));
    }

    private void initializeComponents() {
        titleLabel = new JLabel("Thread Test Application");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        threadTotals = new JLabel[4];
        progressBars = new JProgressBar[4];

        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        resumeButton = new JButton("Resume");
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);

        grandTotalLabel = new JLabel("Grand Total: 0");

        // Action Listeners for buttons
        // start button
        startButton.addActionListener(e -> {
            initializeThreads();
            startAllThreads();
            startButton.setEnabled(true);
            pauseButton.setEnabled(true);
            resumeButton.setEnabled(true);
        });

        // Action Listeners for buttons
        // pasuse button
        pauseButton.addActionListener(e -> {
            initializeThreads();
            pauseAllThreads();
            pauseButton.setEnabled(true);
            resumeButton.setEnabled(true);
        });

        // Action Listeners for buttons
        // resume button
        resumeButton.addActionListener(e -> {
            initializeThreads();
            resumeAllThreads();
            pauseButton.setEnabled(true);
            resumeButton.setEnabled(true);
        });
    }

    // Initialize Thread Method for action listener
    private void initializeThreads() {
        double[] intervals = { 346.5, 252, 462, 198 };
        for (int i = 0; i < 4; i++) {
            ThreadTaskInterface task = threadManager.getTasks()[i];
            if (task instanceof ThreadTask) {
                ThreadTask threadTask = (ThreadTask) task;
                if (threadTask.isAlive()) {
                    threadTask.interrupt();
                }
            }
            progressBars[i].setValue(0);
            threadTotals[i].setText("0");
        }
        grandTotalLabel.setText("Grand Total: 0");
        threadManager.initializeTasks(progressBars, threadTotals, grandTotalLabel, intervals);
    }

    // start all threads method from Thread Operation Manager
    private void startAllThreads() {
        threadManager.startTasks();
    }

    // Pause all threads method
    private void pauseAllThreads() {
        threadManager.pauseTasks();
    }

    // Resume all threads method
    private void resumeAllThreads() {
        threadManager.resumeTasks();
    }

    private void layoutComponents() {
        getContentPane().add(titleLabel, BorderLayout.NORTH);

        JPanel threadDisplayPanel = new JPanel(new GridBagLayout());
        getContentPane().add(threadDisplayPanel, BorderLayout.CENTER);

        setupThreadDisplayComponents(threadDisplayPanel);
        setupButtonPanel(threadDisplayPanel);
        setupGrandTotalPanel(threadDisplayPanel);
    }

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

            progressBars[i] = new JProgressBar();
            GridBagConstraints progressBarConstraints = (GridBagConstraints) threadLabelConstraints.clone();
            progressBarConstraints.gridx = 1;
            progressBarConstraints.weightx = 1;
            progressBarConstraints.gridwidth = 2;
            progressBarConstraints.insets = new Insets(5, 2, 5, 5);
            threadDisplayPanel.add(progressBars[i], progressBarConstraints);

            threadTotals[i] = new JLabel("0");
            GridBagConstraints threadTotalConstraints = (GridBagConstraints) threadLabelConstraints.clone();
            threadTotalConstraints.gridx = 3;
            threadTotalConstraints.gridwidth = 1;
            threadDisplayPanel.add(threadTotals[i], threadTotalConstraints);
        }
    }

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

    // Thread Class with Interface
    public class ThreadTask extends Thread implements ThreadTaskInterface {
        private JProgressBar progressBar;
        private JLabel threadTotalLabel;
        private JLabel grandTotalLabel;
        private double sleepInterval; // this is in milliseconds

        // lock object for critical section
        private static final Object lock = new Object();

        // for pausing the and resuming the thread
        private volatile boolean paused = false;

        public ThreadTask(JProgressBar progressBar, JLabel threadTotalLabel, JLabel grandTotalLabel,
                double sleepInterval) {
            this.progressBar = progressBar;
            this.threadTotalLabel = threadTotalLabel;
            this.grandTotalLabel = grandTotalLabel;
            this.sleepInterval = sleepInterval;
        }

        @Override
        public void run() {

            // thread run method
            for (int i = 1; i <= 100 && !isInterrupted(); i++) {
                // logic for pause button
                synchronized (this) {
                    while (paused) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            return;  
                        }
                    }
                }

                final int progressValue = i; // created final copy of i

                // Update the GUI in the Event Dispatch Thread.
                // thread-safe manner
                SwingUtilities.invokeLater(() -> {
                    progressBar.setValue(progressValue);

                    // Extract the grand total from the grandTotalLabel's text, increase it, and set
                    // it back

                    // Critical Section
                    synchronized (lock) {
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

                // Sleep for the interval duration.
                try {
                    long millis = (long) sleepInterval;
                    int nanos = (int) ((sleepInterval - millis) * 1_000_000); // Convert fractional part to nanoseconds
                    Thread.sleep(millis, nanos);
                } catch (InterruptedException e) {
                    // Handle the interrupt
                    return;
                }
            }
        }

        // inherited method from interface
        @Override
        public void startTask() {
            this.start();
        }

        @Override
        public void pauseTask() {
            paused = true;
        }

        @Override
        public void resumeTask() {
            paused = false;
        }

    }

    // Thread Operation Manager Class
    public class ThreadOperationManager {

        private ThreadTaskInterface[] tasks = new ThreadTaskInterface[4];

        public void initializeTasks(JProgressBar[] progressBars, JLabel[] threadTotals, JLabel grandTotalLabel,
                double[] intervals) {
            for (int i = 0; i < 4; i++) {
                tasks[i] = new ThreadTask(progressBars[i], threadTotals[i], grandTotalLabel, intervals[i]);
            }
        }

        public ThreadTaskInterface[] getTasks() {
            return tasks;
        }

        public void startTasks() {
            for (int i = 0; i < tasks.length; i++) {
                tasks[i].startTask();
            }
        }

        public void pauseTasks() {
            for (ThreadTaskInterface task : tasks) {
                task.pauseTask();
            }
        }

        public void resumeTasks() {
            for (ThreadTaskInterface task : tasks) {
                task.resumeTask();
            }
        }
    }

    // Running an application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ThreadTestApplicationWindow().setVisible(true);
        });
    }

    // Thread Task Interface
    interface ThreadTaskInterface {
        void startTask();

        void pauseTask();

        void resumeTask();
    }
}