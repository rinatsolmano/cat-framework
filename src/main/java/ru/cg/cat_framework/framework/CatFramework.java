package ru.cg.cat_framework.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import lombok.SneakyThrows;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import ru.cg.cat_framework.framework.annnotation.AfterInit;
import ru.cg.cat_framework.framework.annnotation.Bean;
import ru.cg.cat_framework.framework.configurator.ObjectConfigurator;
import ru.cg.cat_framework.framework.wrapper.ProxyWrapper;

/**
 * @author Rinat Suleymanov
 */
public class CatFramework {
  private static CatFramework ourInstance;

  private Reflections scanner;
  private Config config;

  private List<ObjectConfigurator> configurators = new ArrayList<>();
  private List<ProxyWrapper> wrappers = new ArrayList<>();

  private Map<Class, Object> beans = new HashMap<>();

  public static CatFramework getInstance() {
    if (ourInstance == null) {
      throw new RuntimeException("Фреймворк не сконфигурирован");
    }
    return ourInstance;
  }

  public static void configure(String packageToScan, Config config) {
    ourInstance = new CatFramework(packageToScan, config);
  }

  @SneakyThrows
  private CatFramework(String packageToScan, Config config) {
    printLogo();
    scanner = new Reflections(packageToScan);
    this.config = config;
    for (Class<? extends ObjectConfigurator> aClass : scanner.getSubTypesOf(ObjectConfigurator.class)) {
      if (!Modifier.isAbstract(aClass.getModifiers())) {
        configurators.add(aClass.newInstance());
      }
    }

    for (Class<? extends ProxyWrapper> aClass : scanner.getSubTypesOf(ProxyWrapper.class)) {
      if (!Modifier.isAbstract(aClass.getModifiers())) {
        wrappers.add(aClass.newInstance());
      }
    }

  }

  private void printLogo() {
    System.out.println("______________$$___________________$$_____________");
    System.out.println("______________$$$____________________$$___________");
    System.out.println("__$______$$$$$$$$$$___________________$$__________");
    System.out.println("__$$__$$$$$$$$$$$$$$___________________$$_________");
    System.out.println("__$$$$$$$$$$$$$$$$$$____________________$$________");
    System.out.println("___$$$$$$$$$$$$$$$$____$$$$$$$$$$$$______$$_______");
    System.out.println("___$$$$$$$$$$$$$$$___$$$$$$$$$$$$$$$$____$$_______");
    System.out.println("____$$$$$$$$$$$$$__$$$$$$$$$$$$$$$$$$$__$$$_______");
    System.out.println("_______$$$$$$$$$$_$$$$$$$$$$$$$$$$$$$$$_$$$_______");
    System.out.println("__________$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$_______");
    System.out.println("____________$$$$$$$$$$$$$$$$$$$$$$$$$$$$$_________");
    System.out.println("_____________$$$$$$$$$$_________$$$$$$$$__________");
    System.out.println("______________$$$$$$$_____________$$$$$$$_________");
    System.out.println("_______________$$$$$_______________$$$$$$_________");
    System.out.println("________________$$$$________________$$$$$_________");
    System.out.println("________________$$$$_________________$$$$_________");
    System.out.println("_________________$$$__________________$$$_________");
    System.out.println("_______________$$$$$________________$$$$$_________");
    System.out.println("__________________________________________________");
  }

  public void createBeans() {
    for (Class<?> aClass : scanner.getTypesAnnotatedWith(Bean.class)) {
      getObject(aClass);
    }
  }

  @SneakyThrows
  public <T> T getObject(Class<T> type) {

    if (getBean(type) == null) {
      type = resolveType(type); // 1
      T t = type.newInstance(); // 2
      configure(t); // 3
      startInitMethods(type, t);
      t = wrap(t); // 4
      beans.put(type, t);
    }
    return getBean(type);
  }

  private <T> void startInitMethods(Class<T> type, T t) throws IllegalAccessException, InvocationTargetException {
    for (Method method : ReflectionUtils.getAllMethods(type)) {
      if (method.isAnnotationPresent(AfterInit.class)) {
        method.invoke(t);
      }
    }
  }

  private <T> T wrap(T t) {
    for (ProxyWrapper wrapper : wrappers) {
      t = wrapper.wrap(t);
    }
    return t;
  }

  private <T> void configure(T t) {
    configurators.forEach(config->config.configure(t));
  }


  private <T> Class<T> resolveType(Class<T> type) {
    if (type.isInterface()) {
      Set<Class<? extends T>> realizations = scanner.getSubTypesOf(type);
      if (realizations.size() > 1) {
        Class<? extends T> realisation = config.getRealisation(type);
        if (realisation == null) {
          throw new RuntimeException("Не знаю какую реализацию использовать");
        }
        type = (Class<T>)realisation;
      }
      else if (realizations.size() == 0) {
        throw new RuntimeException("Нет реализаций");
      }
      else {
        type = (Class<T>) realizations.iterator().next();
      }
    }
    return type;
  }

  public <T> T getBean(Class<T> type) {

    if (type.isInterface()) {
      List<T> beanList = new ArrayList<>();

      for (Class<? extends T> aClass : scanner.getSubTypesOf(type)) {
        Object o = beans.get(aClass);
        if (o != null) {
          beanList.add((T) o);
        }
      }
      if (beanList.size() == 0) {
        return null;
      }
      else if (beanList.size() > 1) {
        Class<? extends T> realisation = config.getRealisation(type);
        if (realisation == null) {
          throw new RuntimeException("Не знаю какую реализацию использовать");
        }
        for (T t : beanList) {
          if (t.getClass().equals(realisation)) {
            return t;
          }
        }
      }
      else {
        return beanList.get(0);
      }
    }
    return (T)beans.get(type);
  }
}
