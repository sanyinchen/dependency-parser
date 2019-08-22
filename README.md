# dependency-parser

## Description
Dependency-parser is a java class dependency parser based on jd-core and antlr4.
You can get what class has been missed from jars .  
For example:  
There are two lib:lib-a and lib-b  
lib-a includes:  
a_A.java  -> b_A.java(a_A.java depends b_A.java) 

a_B.java  -> b_B.java  

a_C.java  -> b_C.java 

lib-b includes:  
b_A.java  
b_B.java  
b_C.java  

and the gradle of lib-a is :
```
dependencies {
    testCompile group: 'junit', name: 'junit', version: '4.12'
    compileOnly project(':lib-b')
}

```
So , lib-a depend lib-b but not be compiled in lib-a (compileOnly), if you run
some method of lib-a or load lib-a's method in a new classLoader . Maybe you will get find class error .  

Based on this case , we can use Dependency-parser to parse what class has been missed .

1. Test just lib-a :
```
public class Foo {
    public static void main(String[] args) {
        CodeDependencyParser codeDependencyParser = new CodeDependencyParser();
        codeDependencyParser.parse("./input-test/lib-a-1.0-SNAPSHOT.jar");
    }
}
```
It will print :
```
scanned 3 .class file
com/testa/demo/a_C.class finished in Thread[Thread-2,5,main]
com/testa/demo/a_A.class finished in Thread[Thread-1,5,main]
com/testa/demo/a_B.class finished in Thread[Thread-0,5,main]
parser finished
you need to check : 3 group
import com.test.b_A;
import com.test.b_B;
import com.test.b_C;
```
so ,you need to check com.test.b_A,com.test.b_B and com.test.b_C;

2. After test lib-a, we add the jar of lib-b ;
```
public class FooFullJar {
    public static void main(String[] args) {
        CodeDependencyParser codeDependencyParser = new CodeDependencyParser();
        codeDependencyParser.parse("./input-test/lib-a-1.0-SNAPSHOT.jar","./input-test/lib-b-1.0-SNAPSHOT.jar");
    }
}
```
you will find :

```
com/testa/demo/a_A.class finished in Thread[Thread-7,5,main]
com/testa/demo/a_C.class finished in Thread[Thread-8,5,main]
com/test/b_A.class finished in Thread[Thread-5,5,main]
com/test/b_B.class finished in Thread[Thread-3,5,main]
com/test/b_C.class finished in Thread[Thread-4,5,main]
com/testa/demo/a_C.class finished in Thread[Thread-2,5,main]
com/testa/demo/a_A.class finished in Thread[Thread-1,5,main]
com/testa/demo/a_B.class finished in Thread[Thread-0,5,main]
com/testa/demo/a_B.class finished in Thread[Thread-6,5,main]
parser finished
you need to check : 0 group
```
'0 group' is needed to check . So if you add lib-a , you must need to add lib-b;



## How to use dependency-parser ?
you need to clone this project and add 'lib' to your own project.

