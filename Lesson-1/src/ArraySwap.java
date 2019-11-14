import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArraySwap<T> {

    //Задание
    /*Написать метод, который меняет два элемента массива местами
    (массив может быть любого ссылочного типа);*/

    private T[] array;

    public ArraySwap(T... array) {
        this.array = array;
    }

    //метод меняющий первые два элемента местами
    public void swap (int el1, int el2) {
        if (el1 >= 0 && el1 < array.length &&
            el2 >= 0 && el2 < array.length) {
            print();
            System.out.println();
            T value = array[el1];
            array[el1] = array[el2];
            array[el2] = value;
            print();
            System.out.println();
        } else {
            System.out.println("Ошибка! Введите индекс от 0 до 2");
        }
    }

    //метод вывода массива в консоль
    public void print() {
        for (int i = 0; i <array.length; i++) {
            System.out.print(array[i] + " ");
        }
    }

    //Задание 2
    //Написать метод, который преобразует массив в ArrayList;
    public List<T> arrayToArrayList () {
        List<T> arrayList = new ArrayList<>(Arrays.asList(array));
        System.out.println(arrayList);
        return arrayList;
    }
}

