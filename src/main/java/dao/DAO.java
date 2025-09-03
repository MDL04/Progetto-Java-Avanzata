package dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> selectById(long id);
    List<T> selectAll();
    void delete(T t);
}
