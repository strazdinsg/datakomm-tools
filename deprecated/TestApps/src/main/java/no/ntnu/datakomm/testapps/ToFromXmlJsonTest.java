package no.ntnu.datakomm.testapps;

import no.ntnu.alesund.Marshalling;
import no.ntnu.alesund.JsonMarshalling;
import no.ntnu.alesund.XMLMarshalling;
import no.ntnu.datakomm.data.Person;

/**
 * Testing how marshalling to/from XML and JSON works
 *
 * @author Girts Strazdins, 2016-10-08
 */
public class ToFromXmlJsonTest {
    private final Person p;
    private final String alias;
    
    public ToFromXmlJsonTest() {
        // Create a test person, try to marshal it to XML and JSON and then unmarshall
        p = new Person("Bill", "Gates", 60, "555-555-5");
        alias = "person";
    }
    
    public static void main(String[] args) {
        ToFromXmlJsonTest test = new ToFromXmlJsonTest();
        test.run();
    }
    
    public void run() {
        System.out.println("Original person:");
        System.out.println(p);
        System.out.println("");

        // To/from XML
        XMLMarshalling xmlMarshalling = new XMLMarshalling();
        marshallAndUnmarshall("XML", xmlMarshalling);

        // To/from JSON
        JsonMarshalling jsonMarshalling = new JsonMarshalling();
        marshallAndUnmarshall("JSON", jsonMarshalling);
    }

    /**
     * Marshall and unmarshall the Person object p to/from the given 
     * format
     * @param formatName used only for debug
     * @param marshalling the marshalling technique to be used
     */
    private void marshallAndUnmarshall(String formatName, 
            Marshalling marshalling) {
        // Convert to string
        marshalling.setAlias(alias);
        String s = marshalling.marshall(p, Person.class);
        System.out.println("Person as " + formatName + ": ");
        System.out.println(s);

        // Convert back from XML to Person
        Person p2 = (Person) marshalling.unmarshall(s, Person.class);
        System.out.println("");
        System.out.println("Person restored from " + formatName + ":");
        System.out.println(p2);
        System.out.println("");
        
    }

}
