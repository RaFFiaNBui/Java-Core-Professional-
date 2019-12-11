package Paragraph_3;

class Class14 {

    static boolean check (int[] arr) {
        boolean val1 = false;
        boolean val4 = false;
        for (int value : arr) {
            if (value == 1) {
                val1 = true;
            } else if (value == 4) {
                val4 = true;
            }
        }
        return val1 && val4;
    }
}
