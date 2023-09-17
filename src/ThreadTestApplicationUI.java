import javax.swing.*;

public class ThreadTestApplicationUI extends JFrame {
    
    private static final long serialVersionUID = 1L; 
    private JLabel[] threadTotals;
    private JProgressBar[] progressBars;
    private JButton startButton;
    private JButton pauseButton;
    private JButton resumeButton;
    private JLabel grandTotalLabel;
    private JLabel titleLabel;

    public ThreadTestApplicationUI() {
        setTitle("Thread Test Application");
        setSize(545, 190);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        getContentPane().setLayout(null);
        
        titleLabel = new JLabel("Thread Test Application", SwingConstants.CENTER);
        titleLabel.setBounds(175, 10, 200, 20);
        getContentPane().add(titleLabel);

        threadTotals = new JLabel[4];
        progressBars = new JProgressBar[4];
        for (int i = 0; i < 4; i++) {
            JLabel threadLabel = new JLabel((i + 1) + ":");
            threadLabel.setBounds(10, (i * 20) + 30, 25, 10);
            getContentPane().add(threadLabel);

            progressBars[i] = new JProgressBar(0, 100);
            progressBars[i].setBounds(40, (i * 20) + 30, 440, 10);
            getContentPane().add(progressBars[i]);

            threadTotals[i] = new JLabel("0");
            threadTotals[i].setBounds(490, (i * 20) + 30, 40, 10);
            getContentPane().add(threadTotals[i]);
        }

        startButton = new JButton("Start");
        startButton.setBounds(30, 120, 70, 20);
        getContentPane().add(startButton);

        pauseButton = new JButton("Pause");
        pauseButton.setBounds(105, 120, 70, 20);
        pauseButton.setEnabled(false);
        getContentPane().add(pauseButton);

        resumeButton = new JButton("Resume");
        resumeButton.setBounds(180, 120, 85, 20);
        resumeButton.setEnabled(false); // Keeping the same initial behavior
        getContentPane().add(resumeButton);

        grandTotalLabel = new JLabel("Grand Total:      0");
        grandTotalLabel.setBounds(400, 120, 120, 20);
        getContentPane().add(grandTotalLabel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ThreadTestApplicationUI frame = new ThreadTestApplicationUI();
            frame.setVisible(true);
        });
    }
}

