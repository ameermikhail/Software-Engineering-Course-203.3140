package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class user {

    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "password")
    private String password;
    @Column(name="Isconnected")
private int Isconnected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsconnected() {
        return Isconnected;
    }

    public void setIsconnected(int isconnected) {
        Isconnected = isconnected;
    }
// Standard getters and setters
}
