import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) throws   IllegalAccessException,
                                                    InvocationTargetException,
                                                    InstantiationException {
        Class cl = TestClass.class;
        start(cl);
    }

    private static void start ( Class cl) throws IllegalAccessException,
                                                InstantiationException,
                                                InvocationTargetException {
        Method[] methods = cl.getDeclaredMethods();
        Method before = null;
        Method after = null;

        //добавим методы с аннотацией @Test в коллекцию
        ArrayList<Method> arrayList = new ArrayList<>();
        for (Method o : methods) {
            if (o.isAnnotationPresent(Test.class)) {
                arrayList.add(o);
            }

            //проверяем, что метод BeforeSuit в единственном экземпляре
            if(o.isAnnotationPresent(BeforeSuite.class)) {
                if (before  == null) {
                    before = o;
                } else {
                    throw new RuntimeException("Метод с аннотацией BeforeSuit не в единственном экземпляре!");
                }
            }

            //проверяем, что метод AfterSuit в единственном экземпляре
            if(o.isAnnotationPresent(AfterSuite.class)) {
                if (after  == null) {
                    after = o;
                } else {
                    throw new RuntimeException("Метод с аннотацией AfterSuit не в единственном экземпляре!");
                }
            }
        }
        //отсортируем коллекцию по приоритету
        arrayList.sort(new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                return o2.getAnnotation(Test.class).priority() - o1.getAnnotation(Test.class).priority();
            }
        });

        //создаем объект класса TestClass
        Object object = cl.newInstance();

        //запускаем тесты
        if (before != null) {
            before.invoke(object);
        }

        for (Method o : arrayList) {
            o.invoke(object);
        }

        if (after != null) {
            after.invoke(object);
        }
    }
}
