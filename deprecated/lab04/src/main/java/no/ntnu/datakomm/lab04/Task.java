package no.ntnu.datakomm.lab04;

import java.util.ArrayList;
import java.util.List;

/**
 * A task class. This object is sent inside the HTTP OK response from
 * the task server, as a response to task request
 * @author Girts Strazdins, 2016-10-09
 */
public class Task {
    protected int type;// ID of this task
    protected int sessionId;
    protected String description; // Description of the task
    protected List<String> arguments; // Argument list. Can be empty

    public Task() {
        this.arguments = new ArrayList<>();
    }
    
    public int getType() {
        return type;
    }

    public void setType(int id) {
        this.type = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getArguments() {
        return arguments;
    }
    
    public int getArgumentCount() {
        return arguments.size();
    }
    
    /**
     * Get the i-th argument in the list. Indexing starts from zero.
     * 
     * @param index
     * @return the argument as a String, or null if the argument was not found
     */
    public String getArgumentByIndex(int index) {
        if (index < 0 || index >= arguments.size()) {
            return null;
        }
        return arguments.get(index);
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
    
}
