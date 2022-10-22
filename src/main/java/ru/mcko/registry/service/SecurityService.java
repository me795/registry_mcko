package ru.mcko.registry.service;

public interface SecurityService {

    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
