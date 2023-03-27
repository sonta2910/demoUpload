package com.example.demoupload1.service;

import java.util.List;

public interface ICoreService <E>{
    List<E> findAll();
    void save(E e);
    void delete(int id);
    E findById(int id);
    void update(int id, E e);
}
