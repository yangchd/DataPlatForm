package com.yangchd.data.table;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author yangchd  2017/12/22.
 */
@Entity
@Table(name = "syn_user")
public class User {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    @Column(name = "id")
    private String id;
    private String code;
    private String name;
    private String password;
    private String permission;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", permission='" + permission + '\'' +
                '}';
    }
}
