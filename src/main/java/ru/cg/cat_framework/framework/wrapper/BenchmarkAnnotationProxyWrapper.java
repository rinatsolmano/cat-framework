package ru.cg.cat_framework.framework.wrapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import ru.cg.cat_framework.framework.annnotation.Benchmark;

/**
 * @author Rinat Suleymanov
 */
public class BenchmarkAnnotationProxyWrapper implements ProxyWrapper {
  @Override
  public <T> T wrap(T object) {
    Class<?> type = object.getClass();
    if (type.isAnnotationPresent(Benchmark.class)) {
      Object proxy = Proxy.newProxyInstance(type.getClassLoader(), type.getInterfaces(), new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
          System.out.println("------------Benchmark------------");
          long start = System.currentTimeMillis();
          Object retVal = method.invoke(object, args);
          long finish = System.currentTimeMillis();
          System.out.println("Метод " + method.getName() + " выполнялся " + (finish - start) + " милисекунд");
          System.out.println("------------Benchmark------------");
          return retVal;
        }
      });
      return (T) proxy;
    }
    return object;
  }
}
