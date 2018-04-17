package ru.cg.cat_framework.framework;

/**
 * @author Rinat Suleymanov
 */
public interface Config {
  <T> Class<? extends T> getRealisation(Class<T> type);
}
