package ru.cg.cat_framework.framework.configurator;

import java.lang.reflect.Field;

import lombok.SneakyThrows;

import ru.cg.cat_framework.framework.CatFramework;
import ru.cg.cat_framework.framework.annnotation.InjectByType;

/**
 * @author Rinat Suleymanov
 */
public class InjectByTypeAnnotationObjectConfigurator implements ObjectConfigurator {

  @SneakyThrows
  @Override
  public void configure(Object object) {
    Class<?> type = object.getClass();
    for (Field field : type.getDeclaredFields()) {
      if (field.isAnnotationPresent(InjectByType.class)) {
        field.setAccessible(true);
        field.set(object, CatFramework.getInstance().getObject(field.getType()));
      }
    }
  }
}
