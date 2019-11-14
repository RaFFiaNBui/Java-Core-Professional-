public class ArrayTest {

    public static void main(String args[]) {

        //инициализируем 3 массива разного типа
        ArraySwap<Integer> intArray = new ArraySwap<>(1, 2, 3);
        ArraySwap<String> strArray = new ArraySwap<>("1", "2", "3");
        ArraySwap<Double> dblArray = new ArraySwap<>(1.1, 2.2, 3.3);

        //запускаем метод замены элементов
        intArray.swap();
        strArray.swap();
        dblArray.swap();
    }
}