import java.awt.*;
import javax.swing.*;

public class ThreadTestApplicationWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    private JLabel[] threadTotalLabels;
    private JProgressBar[] threadProgressBars;
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

    // Thread Task Interface
    interface ThreadTaskControl {
        void startTask();

        void pauseTask();

        void resumeTask();
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

        threadTotalLabels = new JLabel[4];
        threadProgressBars = new JProgressBar[4];

        
        
        startButton = new JButton("Start");
        // Action Listeners for buttons
        // start button
        startButton.addActionListener(e -> {
            initializeThreads();
            startAllThreads();
            startButton.setEnabled(true);
            pauseButton.setEnabled(true);
            resumeButton.setEnabled(false);
        });
        
        pauseButton = new JButton("Pause");
        // Action Listeners for buttons
        // pasuse button
        pauseButton.addActionListener(e -> {
            pauseAllThreads();
            pauseButton.setEnabled(false);
            resumeButton.setEnabled(true);
        });
        
        resumeButton = new JButton("Resume");
        // Action Listeners for buttons
        // resume button
        resumeButton.addActionListener(e -> {
            resumeAllThreads();
            pauseButton.setEnabled(true);
            resumeButton.setEnabled(false);
        });

        grandTotalLabel = new JLabel("Grand Total: 0");
    }

    // Initialize Thread Method for action listener
    private void initializeThreads() {
        double[] threadSleepIntervals = { 400, 300, 500, 200 }; //taking longer threadSleepIntervals
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
    public class ThreadTask extends Thread implements ThreadTaskControl {
    
        private JProgressBar progressBar;
        private JLabel threadTotalLabel;
        private JLabel grandTotalLabel;
        private double sleepInterval;
        private volatile boolean paused = false;
        private static final Object GRAND_TOTAL_LOCK = new Object();
    
        public ThreadTask(JProgressBar progressBar, JLabel threadTotalLabel, JLabel grandTotalLabel, double sleepInterval) {
            this.progressBar = progressBar;
            this.threadTotalLabel = threadTotalLabel;
            this.grandTotalLabel = grandTotalLabel;
            this.sleepInterval = sleepInterval;
        }
    
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

    // Thread Operation Manager Class
    public class ThreadOperationManager {
    
        private ThreadTaskControl[] tasks = new ThreadTaskControl[4];
    
        public void initializeTasks(JProgressBar[] threadProgressBars, JLabel[] threadTotalLabels, JLabel grandTotalLabel, double[] threadSleepIntervals) {
            for (int i = 0; i < 4; i++) {
                tasks[i] = new ThreadTask(threadProgressBars[i], threadTotalLabels[i], grandTotalLabel, threadSleepIntervals[i]);
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


    // Running an application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ThreadTestApplicationWindow().setVisible(true);
        });
    }
}