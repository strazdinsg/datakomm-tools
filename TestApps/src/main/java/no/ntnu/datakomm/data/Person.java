package no.ntnu.datakomm.data;

/**
 * A test class representing a person structure
 * @author Girts Strazdins, 2016-10-08
 */
public class Person {
    private String firstName;
    private String lastName;
    private int age;
    private String phone;

    public Person() {}
    
    public Person(String firstname, String lastname, int age, String phone) {
        this.firstName = firstname;
        this.lastName = lastname;
        this.age = age;
        this.phone = phone;
    }
    
    @Override
    public String toString() {
        return firstName + " " + lastName + " " 
                + age + " year(s) old, phone: " + phone;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    
}
