package org.example.Interfaces;

import org.example.models.BiologicalData;
import org.example.models.Medication;
import org.example.models.User;

import java.util.ArrayList;

public interface Iservice<T> {
    void add(BiologicalData data, User user);

    void add (T t );

    void add(Medication medication, User user);

    ArrayList<T> getAll();

        void update(T t );
        boolean delete (T t);
}
