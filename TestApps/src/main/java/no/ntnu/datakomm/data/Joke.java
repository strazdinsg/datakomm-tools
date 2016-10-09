package no.ntnu.datakomm.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A test class representing structure used by jokes in the JSON API:
 * http://api.icndb.com/jokes/random
 * @author Girts Strazdins, 2016-10-09
 */
public class Joke {
    int id;
    String joke;
    List<String> categories = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
    
    @Override
    public String toString() {
        return "(ID " + id + " ): " + joke + "(categories: " + categories + ")";
    }
}
