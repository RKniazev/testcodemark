package ru.rkniazev.testcodemark.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Column(name = "name")
    private String name;
    @Id
    @Column(name = "login", nullable = false)
    private String login;
    @Column(name = "pass")
    private String password;
    @ManyToMany
    @Column(name = "roles")
    private List<Role> roles;

    public User() {
    }

    public User(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getLogin() {
        return login;
    }

    public void setRole(Role role) {
        if (this.roles == null){
            this.roles = new ArrayList<Role>();
        }
        this.roles.add(role);
    }

    @Override
    public String toString() {
        String result = "";
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule(
                "CustoUserSerializer",
                new Version(1, 0, 0, null, null, null));
        module.addSerializer(User.class, new UserCustomSerializer());
        mapper.registerModule(module);
        try {
            result = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String toStringWithRoles() {
        String result = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    class UserCustomSerializer extends StdSerializer<User> {

        public UserCustomSerializer() {
            this(null);
        }

        public UserCustomSerializer(Class<User> t) {
            super(t);
        }

        @Override
        public void serialize(
                User user, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("login", user.getLogin());
            jsonGenerator.writeStringField("name", user.getName());
            jsonGenerator.writeStringField("password", user.getPassword());
            jsonGenerator.writeEndObject();
        }
    }
}
