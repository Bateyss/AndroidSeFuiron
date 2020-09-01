package com.fm.modules.api;

import java.util.List;

public interface AbstracInterfApi<T> {
    List<T> findAll();
    T findById(Integer id);
    boolean create(T bean);
    boolean delete(Integer id);
    boolean update(T bean, Integer id);
}
