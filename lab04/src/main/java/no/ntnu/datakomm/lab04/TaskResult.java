package no.ntnu.datakomm.lab04;

/**
 * Feedback for one specific task
 * @author Girts Strazdins, 2016-10-09
 */
public class TaskResult {
    boolean success;
    String comment;
    int sessionId;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
    
    
}
