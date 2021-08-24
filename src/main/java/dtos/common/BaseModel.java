package dtos.common;

public class BaseModel<T> {

    protected Integer id;

    public Integer getId() {
        return id;
    }

    public T setId(Integer id) {
        this.id = id;
        return (T) this;
    }
}