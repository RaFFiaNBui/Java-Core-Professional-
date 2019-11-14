package part3;

import java.util.ArrayList;
import java.util.List;

class Box <T extends Fruit> {

    private List<T> fruits= new ArrayList<>();

    //метод получения веса ящика
    Double getWeight() {
        Double sum = 0.0;
        for(T value: fruits) {
            sum += value.getWeight();
        }
        return sum;
    }

    //метод сравнения веса двух ящиков
    //<?> дает возможность использовать любые ящики
    boolean compare(Box<?> another) {
        return Math.abs(this.getWeight() - another.getWeight()) < 0.001;
    }

    //метод перекладывающий фрукты из одного ящика в другой
    //<Т> указывает на то, что типы ящиков должны быть одинаковые
    void addFruitToBox (Box<T> anotherBox) {
        for(T val : fruits) {
            anotherBox.add(val);
        }
        fruits.clear();
    }

    //метод добавления фрукта в ящик
    void add (T fruit) {
        fruits.add(fruit);
    }
}
