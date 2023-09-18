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
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(marginSize, marginSize, marginSize, marginSize));
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

    //Thread Class
    class ThreadTask extends Thread {
        private JProgressBar progressBar;
        private JLabel threadTotalLabel;
        private JLabel grandTotalLabel;
        private long sleepInterval; // this is in milliseconds
    
        public ThreadTask(JProgressBar progressBar, JLabel threadTotalLabel, JLabel grandTotalLabel, long sleepInterval) {
            this.progressBar = progressBar;
            this.threadTotalLabel = threadTotalLabel;
            this.grandTotalLabel = grandTotalLabel;
            this.sleepInterval = sleepInterval;
        }
    
        @Override
        public void run() {
            // Thread run method
        }
    }

    //Running an application
        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ThreadTestApplicationWindow().setVisible(true);
        });
    }
}