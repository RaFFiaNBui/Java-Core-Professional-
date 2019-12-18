import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        chain(5, 4);
    }

    private static void chain(int n, int m) {
        //определяем массив
        int[][] arr = new int[n][m];
        //задаем число "счетчик"
        int c = 1;
        //задаем граничные параметры обхода
        int x_min = 0;
        int x_max = n;
        int y_min = 0;
        int y_max = m;
        //узнаем необходимое количество обходов
        int z_start = 0;
        int z_end;
        if (m >= n) {
            if (n % 2 == 0) {
                z_end = n / 2;
            } else
                z_end = n / 2 + 1;
        } else if (m % 2 == 0) {
            z_end = m / 2;
        } else
            z_end = m / 2 + 1;
        //зацикливаем нужное количество обходов
        while (z_start < z_end) {
            //движение вправо
            for (int i = y_min; i < y_max; i++) {
                arr[x_min][i] = c++;
            }
            x_min = x_min + 1;
            //движение вниз
            for (int i = x_min; i < x_max; i++) {
                arr[i][y_max - 1] = c++;
            }
            y_max = y_max - 1;
            //движение влево
            for (int i = y_max; i > y_min; i--) {
                arr[x_max - 1][i - 1] = c++;
            }
            x_max = x_max - 1;
            //движение вверх
            for (int i = x_max; i > x_min; i--) {
                arr[i - 1][y_min] = c++;
            }
            y_min = y_min + 1;
            z_start++;
        }
        //вывод массива в консоль и в файл
        printArr(arr);
        writeToFile(arr);
    }

    //метод отрисовки массива в консоли
    private static void printArr(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }

    //метод вывода массива в файл
    private static void writeToFile (int[][] arr) {
        try {
            PrintWriter printWriter = new PrintWriter("src/array.txt");
            //1 способ печати в файл
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[i].length; j++) {
                    printWriter.write(arr[i][j] + " ");
                }
                printWriter.write(System.lineSeparator());
            }
            //2 способ печати в файл
            //printWriter.write(Arrays.deepToString(arr));
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}