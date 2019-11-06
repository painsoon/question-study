## Supplier惰性求值

用于需要延迟计算的场景，比如某些运算很耗资源运算出来却又用不到。在Java里，我们有一个  
设计模式几乎就是为了处理这种情形而生的：Proxy。但是今天要说的是Supplier。
概念不多说撸代码才是硬道理。

**Supplier定义:**
```
public interface Supplier{
    T get();
}
```
把耗资源的运算放到get()方法里，我们传递的是Supplier对象，直到调用get()方法运算才会执行。


```
public class Main {

	public static void main(String[] args) {
		Supplier<SupplierTest> test = SupplierTest :: new;
		System.out.println("---------------");
		test.get();		//直到调用get才是真正的初始化

        System.out.println("---------------");
        Supplier supplierTest = new Supplier() {
            @Override
            public Object get() {
                return new SupplierTest();
            }
        };
        
        /**
         * 当需要创建终极目标时只需supplierTest.get()
         * 使用Class对象cast方法，防止对象转换时出现的警告
         */
        SupplierTest temp = SupplierTest.class.cast(supplierTest.get());
	}
	
}

class SupplierTest {
	private int num = 10;
	public SupplierTest() {
		System.out.println(num);
	}
}

```

通常实现 Proxy 模式，我们只会计算一次。而这样get()每次都会运算一次，你会想到把结果保存 
下来，下次再调用时直接返回。我们可以使用Guava提供的方法。
```
memorizedUltimateAnswerSupplier = Suppliers.memoize(ultimateAnswerSupplier);
```
memoize() 函数帮我打点了前面所说的那些事情：第一次 get() 的时候，它会调用真正Supplier，得到结果并保存下来，下次再访问就返回这个保存下来的值。
有时候，这个值只在一段时间内是有效的，你知道我要说什么了，Guava还给我们提供了一个另一个函数，让我们可以设定过期时间：
```
expirableUltimateAnswerSupplier = memoizeWithExpiration(target, 100, NANOSECONDS);
```



**关于new object()和Object::new的区别**
```
public class Main {

    public static void main(String[] args) {
        
        /**-------------new Object() 和  object::new 的区别----------------------**/
        System.out.println("=======================================");
        Thread thread1 = new Thread(new Thread1(),"1");
        thread1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("---------------");
        Thread thread2 = new Thread(Thread2::new,"2");
        thread2.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("---------------");
        Thread2 t2 = new Thread2();
        Thread threadt2 = new Thread(t2::run,"3");
        threadt2.start();
        
        
        Thread thread3 = new Thread(()->{
            
        },"4") ;
        thread3.start();
    }
    
}

class Thread1 implements Runnable{
    
    public Thread1() {
        System.out.println("构造方法1输出");
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}

class Thread2 implements Runnable{
    public Thread2() {
        System.out.println("构造方法2输出");
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}

```
A::B B代表方法，如果B是静态或构造，A必须是类名。如果B是实例方法，A必须是对象。

同时上面new Thread()参数需要的是实现接口的对象，接口只有一个方法，所以可以用lambda表达式，要保证这唯一的方法与替代的他的
方法参数一样。显然：：这种只能用于无参。除了用：：，还能用（）->{ },（）与方法的（）一样，这里表示无参，有参这样（s）->{}
即：new Thread(（）->{ }, "4").start();