package ac.kcl.inf.has.env.common;

public class Pair<E,T> {

    E object1;
    T object2;

    public Pair(E object1, T object2){
        this.object1 = object1;
        this.object2 = object2;
    }

    public E getObject1() {
        return object1;
    }

    public T getObject2() {
        return object2;
    }

    @Override
    public String toString() {
        return "(" + object1 +
                "," + object2 +
                ')';
    }
}
