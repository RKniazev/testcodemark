package ru.rkniazev.testcodemark.controllers;

public enum Errors {
    PASSWORD("Password hasn't uppercase letter or number"),
    ROLES_NOT_CORRECT("Role/roles not found"),
    ROLES_EMPTY("Argument roles is empty"),
    LOGIN_EXIST("This login already exists"),
    USER_NOT_FIND("Not find user by login")
    ;

    private final String textError;

    Errors(String s) {
        this.textError = s;
    }

    @Override
    public String toString() {
        return this.textError;
    }
}
