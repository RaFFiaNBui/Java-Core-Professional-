public class ArraySwap<T> {

    //Задание
    /*Написать метод, который меняет два элемента массива местами
    (массив может быть любого ссылочного типа);*/

    private T[] array;

    public ArraySwap(T... array) {
        this.array = array;
    }

    //метод меняющий первые два элемента местами
    public void swap () {
        print(array);
        System.out.println();
        T value = array[0];
        array[0] = array[1];
        array[1] = value;
        print(array);
        System.out.println();
    }

    //метод вывода массива в консоль
    public void print(T... nums) {
        for (int i = 0; i <nums.length; i++) {
            System.out.print(nums[i] + " ");
        }
    }
}

