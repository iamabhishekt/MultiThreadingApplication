import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class ThreadTestApplicationWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int NUM_THREADS = 4;
    private static final int FRAME_WIDTH = 545;
    private static final int FRAME_HEIGHT = 190;
    private static final int PROGRESS_MAX = 100;
    private static final int THREAD_SLEEP_DURATION = 50;
    private static final String APP_TITLE = "Thread Test Application";

    private JLabel[] threadTotalLabels;
    private JProgressBar[] threadProgressBars;
    private JButton startButton;
    private JButton pauseButton;
    private JButton resumeButton;
    private JLabel grandTotalLabel;
    private JLabel titleLabel;

    private ControlledThreadManager threadManager = new ControlledThreadManager();

    public ThreadTestApplicationWindow() {
        setupFrame();
        initializeUIComponents();
        layoutComponents();
    }

    private void setupFrame() {
        setTitle(APP_TITLE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        int marginSize = 10;
        ((JComponent) getContentPane())
                .setBorder(BorderFactory.createEmptyBorder(marginSize, marginSize, marginSize, marginSize));
        getContentPane().setLayout(new BorderLayout(0, 0));
    }

    private void initializeUIComponents() {
        titleLabel = new JLabel("Thread Test Application");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        threadTotalLabels = new JLabel[NUM_THREADS];
        threadProgressBars = new JProgressBar[NUM_THREADS];

        startButton = new JButton("Start");
        startButton.addActionListener(this::handleStartAction);
        startButton.setEnabled(true);

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(this::handlePauseAction);
        pauseButton.setEnabled(false);

        resumeButton = new JButton("Resume");
        resumeButton.addActionListener(this::handleResumeAction);
        resumeButton.setEnabled(false);

        grandTotalLabel = new JLabel("Grand Total: 0");
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
        for (int i = 0; i < NUM_THREADS; i++) {
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

    private boolean areAllThreadsFinished() {
        for (ThreadControl thread : threadManager.getThreads()) {
            if (((Thread) thread).isAlive()) {
                return false;
            }
        }
        return true;
    }

    private void handleStartAction(ActionEvent e) {
        initializeThreads();
        startAllThreads();
        startButton.setEnabled(true);
        pauseButton.setEnabled(true);
        resumeButton.setEnabled(false);
    }

    private void handlePauseAction(ActionEvent e) {
        pauseAllThreads();
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(true);
    }

    private void handleResumeAction(ActionEvent e) {
        resumeAllThreads();
        pauseButton.setEnabled(true);
        resumeButton.setEnabled(false);
    }

    private void initializeThreads() {
        int[] threadSleepIntervals = { 400, 300, 500, 200 };
        for (int i = 0; i < NUM_THREADS; i++) {
            ThreadControl thread = threadManager.getThreads()[i];
            if (thread instanceof ControlledThread) {
                ControlledThread controlledThread = (ControlledThread) thread;
                if (controlledThread.isAlive()) {
                    controlledThread.interrupt();
                }
            }
            threadProgressBars[i].setValue(0);
            threadTotalLabels[i].setText("0");
        }
        grandTotalLabel.setText("Grand Total: 0");
        threadManager.initializeThreads(threadProgressBars, threadTotalLabels, grandTotalLabel, threadSleepIntervals);
    }

    private void startAllThreads() {
        threadManager.startAllThreads();
    }

    private void pauseAllThreads() {
        threadManager.pauseAllThreads();
    }

    private void resumeAllThreads() {
        threadManager.resumeAllThreads();
    }

    interface ThreadControl {

        void startThread();

        void pauseThread();

        void resumeThread();
    }

    public class ControlledThread extends Thread implements ThreadControl {

        private JProgressBar progressBar;
        private JLabel threadTotalLabel;
        private JLabel grandTotalLabel;
        private int sleepInterval;
        private volatile boolean paused = false;
        private static final Object GRAND_TOTAL_LOCK = new Object();
        private final Object pauseLock = new Object();

        public ControlledThread(JProgressBar progressBar, JLabel threadTotalLabel, JLabel grandTotalLabel,
                int sleepInterval) {
            this.progressBar = progressBar;
            this.threadTotalLabel = threadTotalLabel;
            this.grandTotalLabel = grandTotalLabel;
            this.sleepInterval = sleepInterval;
        }

        @Override
        public void run() {
            for (int i = 1; i <= PROGRESS_MAX && !isInterrupted(); i++) {
                synchronized (pauseLock) {
                    while (paused) {
                        try {
                            pauseLock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                        break;
                    }
                }

                final int progressValue = i;
                SwingUtilities.invokeLater(() -> {
                    progressBar.setValue(progressValue);

                    synchronized (GRAND_TOTAL_LOCK) {
                        String[] parts = grandTotalLabel.getText().split(" ");
                        int grandTotal = Integer.parseInt(parts[parts.length - 1]);

                        int threadTotal = Integer.parseInt(threadTotalLabel.getText());

                        grandTotalLabel.setText("Grand Total: " + (grandTotal + 1));
                        threadTotalLabel.setText(Integer.toString(threadTotal + 1));

                        try {
                            Thread.sleep(THREAD_SLEEP_DURATION);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                });

                try {
                    Thread.sleep(sleepInterval);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }

        @Override
        public void startThread() {
            this.start();
        }

        @Override
        public void pauseThread() {
            synchronized (pauseLock) {
                paused = true;
            }
        }

        @Override
        public void resumeThread() {
            synchronized (pauseLock) {
                paused = false;
                pauseLock.notify();
            }
        }
    }

    public class ControlledThreadManager {

        private ThreadControl[] threads = new ThreadControl[NUM_THREADS];

        public void initializeThreads(JProgressBar[] threadProgressBars, JLabel[] threadTotalLabels,
                JLabel grandTotalLabel, int[] threadSleepIntervals) {
            for (int i = 0; i < NUM_THREADS; i++) {
                threads[i] = new ControlledThread(threadProgressBars[i], threadTotalLabels[i], grandTotalLabel,
                        threadSleepIntervals[i]);
            }
        }

        public ThreadControl[] getThreads() {
            return threads;
        }

        public void startAllThreads() {
            for (int i = 0; i < threads.length; i++) {
                threads[i].startThread();
            }
        }

        public void pauseAllThreads() {
            for (ThreadControl thread : threads) {
                thread.pauseThread();
            }
        }

        public void resumeAllThreads() {
            for (ThreadControl thread : threads) {
                thread.resumeThread();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ThreadTestApplicationWindow().setVisible(true);
        });
    }
}