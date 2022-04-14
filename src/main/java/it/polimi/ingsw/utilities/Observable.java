package it.polimi.ingsw.utilities;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {

    private final List<Observer<T>> observersList = new ArrayList<>();

    public void addObserver(Observer<T> observer)
    {
        synchronized (observersList){
            observersList.add(observer);
        }
    }
    public void removeObserver(Observer<T> observer)
    {
        synchronized (observersList)
        {
            observersList.remove(observer);
        }
    }

    public void notify(T message)
    {
        synchronized (observersList)
        {
            for(Observer<T> observer: observersList)
            {
                observer.update(message);
            }

        }
    }
}
