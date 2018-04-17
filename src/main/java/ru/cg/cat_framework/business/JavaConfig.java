package ru.cg.cat_framework.business;

import java.util.HashMap;
import java.util.Map;

import ru.cg.cat_framework.framework.Config;

/**
 * @author Rinat Suleymanov
 */
public class JavaConfig implements Config {

  private Map<Class, Class> implementations = new HashMap<>();

  public JavaConfig() {
    implementations.put(Engine.class, ElectricalEngine.class);
  }

  @Override
  public <T> Class<? extends T> getRealisation(Class<T> type) {
    return implementations.get(type);
  }
}
