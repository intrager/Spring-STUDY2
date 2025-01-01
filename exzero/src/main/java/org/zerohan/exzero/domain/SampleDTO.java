package org.zerohan.exzero.domain;

public class SampleDTO {
    private String name;
    private int age;

    public SampleDTO() {}

    public SampleDTO(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "SampleDTO(" +
                "name='" + name + '\'' +
                ", age=" + age +
                ')';
    }
}
