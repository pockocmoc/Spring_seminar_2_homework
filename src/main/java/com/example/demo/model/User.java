package com.example.demo.model;


import lombok.Data;
import lombok.extern.java.Log;

@Data
@Log
public class User {

    private int id;

    private String firstName;

    private String lastName;
}
