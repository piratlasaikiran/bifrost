package org.bhavani.constructions.services;

public interface UserService {

    String getHashedPassword(String userName);

    void createNewUser(String userName, String password);
}
