package com.foodease.model;

import com.foodease.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * OOP INHERITANCE:
 * Represents administrator personnel extending User.
 */
@Entity
@Table(name = "admins")
public class Admin extends User {

    @Column(nullable = false)
    private String department;

    private int permissionLevel;

    public Admin() {
        super();
        setRole(UserRole.ADMIN);
    }

    public Admin(String name, String email, String department, int permissionLevel) {
        super(name, email, UserRole.ADMIN);
        this.department = department;
        this.permissionLevel = permissionLevel;
    }

    // Method Overriding (OOP Polymorphism)
    @Override
    public String getDashboardAccess() {
        return "ADMIN_MANAGEMENT_CONSOLE";
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(int permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
}
