package ru.cg.cat_framework.framework.configurator;

import java.lang.reflect.Field;

import lombok.SneakyThrows;
import org.fluttercode.datafactory.impl.DataFactory;

import ru.cg.cat_framework.framework.annnotation.InjectRandomName;

/**
 * @author Rinat Suleymanov
 */
public class InjectRandomValueObjectConfigurator implements ObjectConfigurator {

  private DataFactory factory = new DataFactory();

  @SneakyThrows
  @Override
  public void configure(Object object) {
    Class<?> type = object.getClass();

    for (Field field : type.getDeclaredFields()) {
      if (field.isAnnotationPresent(InjectRandomName.class)) {
        field.setAccessible(true);
        field.set(object, factory.getName());
      }
    }
  }
}
