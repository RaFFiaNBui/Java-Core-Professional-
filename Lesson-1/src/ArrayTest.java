import java.util.List;

public class ArrayTest {

    public static void main(String args[]) {

        //инициализируем 3 массива разного типа
        ArraySwap<Integer> intArray = new ArraySwap<>(1, 2, 3);
        ArraySwap<String> strArray = new ArraySwap<>("Строка 1", "Строка 2", "Строка 3");
        ArraySwap<Double> dblArray = new ArraySwap<>(1.1, 2.2, 3.3);

        //метод замены элементов
        intArray.swap(0,1);
        strArray.swap(1,2);
        dblArray.swap(0,3);

        //метод преобразования массива в ArrayList
        List <Integer> integerList = intArray.arrayToArrayList();
        List <String> stringList = strArray.arrayToArrayList();
        List <Double> doubleList = dblArray.arrayToArrayList();
    }
}