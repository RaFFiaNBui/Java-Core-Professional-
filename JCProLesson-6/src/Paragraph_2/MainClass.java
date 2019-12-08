class MainClass {

    static int[] method (int[] arr) {
        int count = arr.length % 4;
        int[] exitArr = new int[count];
        if (arr.length < 4) {
            throw new RuntimeException();
        } else {
            if (count == 1) {
                exitArr[0] = arr[arr.length-1];
            } if (count == 2) {
                exitArr[0] = arr[arr.length-2];
                exitArr[1] = arr[arr.length-1];
            } if (count == 3) {
                exitArr[0] = arr[arr.length-3];
                exitArr[1] = arr[arr.length-2];
                exitArr[2] = arr[arr.length-1];
            }
        return exitArr;
        }
    }

    static void info(int[] arr) {
        for (int value : arr) {
            System.out.print(value + " ");
        }
    }
}
