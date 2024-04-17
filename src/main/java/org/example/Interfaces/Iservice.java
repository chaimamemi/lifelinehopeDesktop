package org.example.Interfaces;

import java.util.ArrayList;

public interface Iservice<T> {
        void add (T t );
        ArrayList<T> getAll();

        void update(T t );
        boolean delete (T t);
}
