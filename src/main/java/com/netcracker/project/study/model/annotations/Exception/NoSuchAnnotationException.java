package com.netcracker.project.study.model.annotations.Exception;

import java.util.NoSuchElementException;

public class NoSuchAnnotationException extends NoSuchElementException {

    public NoSuchAnnotationException(){}

    public NoSuchAnnotationException(String message){
        super(message);
    }
}
