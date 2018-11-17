package model;

import java.io.*;
import java.util.*;

public class Database {

    private List<Person> people;

    public Database(){
        people = new LinkedList<Person>();
    }

    public void addPerson(Person person){
        people.add(person);
    }

    public void removePerson(int index) {
        people.remove(index);
    }
    public List<Person> getPeople(){
        return Collections.unmodifiableList(people);
    }

    public void saveToFile(File file) throws IOException {

        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        Person[] persons = people.toArray(new  Person[people.size()]);
        oos.writeObject(persons);
        oos.close();
    }

    public void loadFromFile(File file) throws IOException, ClassNotFoundException {

        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);

        try {

            Person[] persons = (Person[]) ois.readObject();

            people.clear();
            people.addAll(Arrays.asList(persons));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ois.close();
    }

}
