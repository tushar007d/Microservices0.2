package org.example;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Main {

    public static void main(String[] args) {
    Employee employee = new Employee(1, "tushar");
        Employee employee1 = new Employee(1, "tushar");
        Employee employee2 = new Employee(3, "tushr");
        Employee employee3 = new Employee(4, "tsar");
        Set<Employee> ls = new HashSet<>();
        ls.add(employee);
        ls.add(employee1);
        ls.add(employee2);
        ls.add(employee3);

        System.out.println(ls);


    }
}