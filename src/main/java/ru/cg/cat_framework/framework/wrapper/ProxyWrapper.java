package ru.cg.cat_framework.framework.wrapper;

/**
 * @author Rinat Suleymanov
 */
public interface ProxyWrapper {

  <T> T wrap(T object);
}
