package repositorio;

import java.util.List;

public interface Repositorio <T>{
public T findById(int id);
public List<T> findAll();
public List<T> delete(int id);
public boolean create(T t);
}
