package no.ntnu.datakomm.lab04;

import java.util.HashMap;
import java.util.Map;

/**
 * Feedback class representing results for a specific student
 * The results will be in the form
 * criteria: percent 
 * , where points are from 0 to 100 (percentage)
 * The totalResult field represents the final score of this assignment, 
 * as a percentage
 * 
 * @author Girts Strazdins, 2016-10-09
 */
public class Feedback {
    // Minimum grade to pass the assignment
    private static final int PASS_THRESHOLD = 50; 
    
    String student;
    Map<String, Integer> results;
    int totalResult;
    boolean passed;
    
    public Feedback() {
        results = new HashMap<>();
        totalResult = 0;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public Map<String, Integer> getResults() {
        return results;
    }

    public void setResults(Map<String, Integer> results) {
        this.results = results;
    }
    
    /**
     * Add a single result, increment the totalResult as well
     * @param task
     * @param resultPercent how many percent of the overall grade is this
     * achievement worth
     */
    public void addResult(String task, int resultPercent) {
        results.put(task, resultPercent);
        totalResult += resultPercent;
        updatePassedStatus();
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    private void updatePassedStatus() {
        passed = totalResult >= PASS_THRESHOLD;
    }
    
}
