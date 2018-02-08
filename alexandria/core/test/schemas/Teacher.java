package schemas;

import java.time.Instant;

public class Teacher extends Person {
    public String university;

    public Teacher() {
    }

    public Teacher(String name, double money, Instant birthDate, Country country) {
        super(name, money, birthDate, country);
    }
}
