package part3;

public class Test {

    public static void main(String[] args) {

        //создаем ящик с яблоками и ящик с апельсинами
        Box<Apple> appleBox = new Box<>();
        Box<Orange> orangeBox = new Box<>();

        //наполняем ящики соответствующими фруктами
        appleBox.add(new Apple());
        appleBox.add(new Apple());
        appleBox.add(new Apple());

        orangeBox.add(new Orange());
        orangeBox.add(new Orange());

        //убедимся что не можем положить яблоко в коробку с апельсинами
//        orangeBox.add(new Apple());

        //узнаем массу наполненых ящиков
        System.out.println(appleBox.getWeight());
        System.out.println(orangeBox.getWeight());

        //сравниваем вес ящиков
        System.out.println(appleBox.compare(orangeBox));

        //создаем пустой ящик для под яблоки
        Box<Apple> box = new Box<>();
        //перекладываем все яблоки в пустой ящик
        appleBox.addFruitToBox(box);
        //проверяем массы ящиков с яблоками
        System.out.println(appleBox.getWeight());
        System.out.println(box.getWeight());
        //убеждаемся что не можем положить апельсины в ящик с яблоками
//        System.out.println(orangeBox.addFruitToBox(box));
    }
}
