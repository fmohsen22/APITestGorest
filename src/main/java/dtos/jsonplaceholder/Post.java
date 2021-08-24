package dtos.jsonplaceholder;

import dtos.common.BaseModel;

public class Post extends BaseModel<Post> {


    private Integer     id;
    private String      name;
    private String      gender;
    private String      email;
    private String      status;

    public Integer getUserId() {
        return id;
    }

    public Post setUserId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }
    public Post setName(String name) {
        this.name = name;
        return this;
    }

    public String getGender() {
        return gender;
    }
    public Post setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getEmail() {
        return email;
    }
    public Post setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getStatus() {
        return status;
    }
    public Post setStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

