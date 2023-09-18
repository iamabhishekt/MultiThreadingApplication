![](https://lh5.googleusercontent.com/wPIQY3bLzSZppFvLQnYauqntQ-d9GVxmEFl7b1dSXXqFYDHOBGTGnbkOWW8owAaTn9chKtT_VobHeCE610o3rj5ePiSTu4uM6xBHn3eUuMFVnRWNTYJofYqf53D2nzm6NUgAWPDf5AIN)

ASSIGNMENT:

1. Create a Swing form in Eclipse looking like the above form

2. Create a thread class that will accept as parameters:

   1. Progress bar
   2. Thread total label
   3. Grand total label
   4. Interval in ms to be used as a parameter for sleep method between each step of a thread

3. Implement a thread class to:

   1. Count from 1 to 100 with a sleep time between steps defined by a parameter
   2. On each step update in a thread safe manner GUI display (progress bar, total label and grand total label)

4. IMPORTANT: Create a critical section within a thread to update ‘Grand Total’ label value.  This critical section has to include sleep(50).  The only access to Grand Total should be through the text property of ‘Grand Total’ label (don’t keep any additional variable to store it).

5. When pressing ‘Start’ button start 4 instances of a thread class with different intervals and be able to view their progress

6. Implement ‘Pause’ and ‘Resume’ buttons

7. Use proper way to update Swing GUI components from within a thread
