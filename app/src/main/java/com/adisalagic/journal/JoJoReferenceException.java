package com.adisalagic.journal;

class JoJoReferenceException extends RuntimeException{
    JoJoReferenceException() {
        throw new RuntimeException("JoJo reference is not allowed here");
    }
}
