package org.example.Interfaces;

import org.example.models.BiologicalData;
import org.example.models.Medication;

import java.util.ArrayList;

public interface Iservice<T> {


    void add(BiologicalData data);

    void add (T t );

    void add(Medication medication);

    ArrayList<T> getAll();

        void update(T t );
        boolean delete (T t);
}
