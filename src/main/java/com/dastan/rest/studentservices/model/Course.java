package com.dastan.rest.studentservices.model;

import java.util.List;

public record Course(String id,
                     String name,
                     String description,
                     List<String> steps) {

}