import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @ClassName RosterTest
 * @Description TODO
 * @Author liyinglong
 * @Date 2020/1/21 4:00 下午
 * @Version 1.0
 **/
public class RosterTest {

    interface CheckPerson {
        boolean test(Person p);
    }

    /*
     * @Author liyinglong
     * @Description //匹配符合某一特征的成员的方法
     * @Date 4:14 下午 2020/1/21
     * @Param [roster, age]
     * @return void
     **/
    public static void printPersonOlderThan(List<Person> roster, int age) {
        for (Person p : roster) {
            if (p.getAge() >= age) {
                p.printPerson();
            }
        }
    }

    /*
     * @Author liyinglong
     * @Description //匹配年龄在某一区间的成员
     * @Date 4:19 下午 2020/1/21
     * @Param
     * @return
     **/
    public static void printPersonsWithinAgeRange(List<Person> roster, int low, int high) {
        for (Person p : roster) {
            if (low <= p.getAge() && p.getAge() < high) {
                p.printPerson();
            }
        }
    }

    /*
     * @Author liyinglong
     * @Description //不要处女座的、只要邮箱是163的，怎么搞？
     * 方法1：在本地类中指定搜索条件代码，通过接口方式，不同的需求对应不同的实现类，
     *		 每次都要新建实现类，写大量的代码
     * 方法2：在匿名类中指定搜索条件代码，不需要写各种实现，但是还要写个interface CheckPerson，
     *       而且匿名类写起来也挺麻烦
     * 方法3：Lambda表达式是懒人的不二之选，CheckPerson是一个只包含一个抽象方法的接口，
     *	     比较简单，Lambda可以省略其实现
     * @Date 4:44 下午 2020/1/21
     * @Param [roster, tester]
     * @return void
     **/
    public static void printPersons(List<Person> roster, CheckPerson tester) {
        for (Person p : roster) {
            if (tester.test(p)) {
                p.printPerson();
            }
        }
    }

    /*
     * @Author liyinglong
     * @Description //可以使用标准的函数接口来代替接口CheckPerson，从而进一步减少所需的代码量
     * 			java.util.function包中定义了标准的函数接口
     * 			我们可以使用JDK8提供的 Predicate<T>接口来代替CheckPerson。
     *			该接口包含方法boolean test(T t)
     * @Date 4:48 下午 2020/1/21
     * @Param [roster, tester]
     * @return void
     **/
    public static void printPersonsWithPredicate(List<Person> roster, Predicate<Person> tester) {
        for (Person p : roster) {
            if (tester.test(p)) {
                p.printPerson();
            }
        }
    }

    /*
     * @Author liyinglong
     * @Description //Lambda表达式可不只是能够简化匿名类
     * 		简化 p.printPerson(),
     * 		使用Consumer<T>接口的void accept(T t)方法，相当于入参的操作
     * @Date 4:57 下午 2020/1/21
     * @Param [roster, tester, block]
     * @return void
     **/
    public static void processPersons(List<Person> roster, Predicate<Person> tester, Consumer<Person> block) {
        for (Person p : roster) {
            if (tester.test(p)) {
                block.accept(p);
            }
        }
    }

    /*
     * @Author liyinglong
     * @Description //Function<T,R>接口，相当于输入类型，mapper定义参数，block负责方对给定的参数进行执行
     * @Date 5:06 下午 2020/1/21
     * @Param [roster, tester, mapper, block]
     * @return void
     **/
    public static void processPersonsWithFunction(List<Person> roster, Predicate<Person> tester, Function<Person, String> mapper, Consumer<String> block) {
        for (Person p : roster) {
            if (tester.test(p)) {
                String data = mapper.apply(p);
                block.accept(data);
            }
        }
    }

    /*
     * @Author liyinglong
     * @Description //使用泛型
     * @Date 5:11 下午 2020/1/21
     * @Param [source, tester, mapper, block]
     * @return void
     **/
    public static <X, Y> void processElements(Iterable<X> source, Predicate<X> tester, Function<X, Y> mapper, Consumer<Y> block) {
        for (X x : source) {
            if (tester.test(x)) {
                Y data = mapper.apply(x);
                block.accept(data);
            }
        }
    }

    public static void main(String[] args) {
        List<Person> roster = Person.createRoster();
        /**
         * 输出年龄大于20的成员
         */
        System.out.println("Persons older than 20:");
        printPersonOlderThan(roster, 20);
        System.out.println();

        /**
         * 输出年龄14-30之间的成员
         */
        System.out.println("Persons range between 14 and 30:");
        printPersonsWithinAgeRange(roster, 14, 30);
        System.out.println();

        /**
         * 输出年龄在18到25岁的男性成员
         *       （在本地类中指定搜索条件）
         * 您可以使用一个匿名类而不是一个本地类，并且不必为每个搜索声明一个新类
         */
        System.out.println("Persons who are eligible for Selective Service:");
        class CheckPersonEligibleForSelectiveService implements CheckPerson {
            @Override
            public boolean test(Person p) {
                return p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25;
            }
        }
//        通过行为参数化传递代码
        printPersons(roster, new CheckPersonEligibleForSelectiveService());
        System.out.println();
//        匿名类中指定搜索条件代码
        printPersons(roster, new CheckPerson() {
            @Override
            public boolean test(Person p) {
                return p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25;
            }
        });
        System.out.println();
//      使用lambda简化代码
        printPersons(roster, (Person p) -> p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25);
        System.out.println();
//      使用lambda标准功能接口
        printPersons(roster, p -> p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25);
        System.out.println();
//      使用Predicate和Consumer参数
        processPersons(roster, p -> p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25, p -> p.printPerson());
        System.out.println();
//      通过Function<T,R> 指定输出类型
        processPersonsWithFunction(roster, p -> p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25, p -> p.getEmailAddress(), email -> System.out.println(email));
        System.out.println();
//      使用泛型
        processElements(roster, p -> p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25, p -> p.getEmailAddress(), email -> System.out.println(email));
        System.out.println();
//      使用接受Lambda表达式的批量数据操作
        roster.stream().filter(p -> p.getGender() == Person.Sex.MALE && p.getAge() >= 18).map(p -> p.getEmailAddress()).forEach(email -> System.out.println(email));
        System.out.println();
/**
 *      按年龄排序。Java 8 之前需要实现 Comparator 接口
 *      接口比较器是一个功能接口。因此，可以使用lambda表达式来代替定义并创建一个实现了Comparator的类的新实例:
 */
        Person[] rosterArrary = roster.toArray(new Person[roster.size()]);
        Arrays.sort(rosterArrary, (a, b) -> Person.compareAge(a, b));
        for (Person p : roster) {
            p.printPerson();
        }
    }
}
