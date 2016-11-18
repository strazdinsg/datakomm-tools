package no.ntnu.datakomm;

/**
 * A structure for giving feedback about the operation results.
 * Background thread uses it to signal status to the main GUI thread
 *
 * Created by Girts Strazdins on 29/10/16.
 */
public class Feedback {
    private boolean success;
    private String errMsg;

    public Feedback(boolean success, String errMsg) {
        this.success = success;
        this.errMsg = errMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
