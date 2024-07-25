package entities;

public class User {
    private int id;
    private String name;
    private String surname;
    private int groupId;
    private int cin;

    // Constructors
    public User() {}

    public User(String name, String surname, int groupId, int cin) {
        this.name = name;
        this.surname = surname;
        this.groupId = groupId;
        this.cin = cin;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }
}
