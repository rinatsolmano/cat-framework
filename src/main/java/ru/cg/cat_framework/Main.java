package ru.cg.cat_framework;

import ru.cg.cat_framework.business.Engine;
import ru.cg.cat_framework.business.JavaConfig;
import ru.cg.cat_framework.framework.CatFramework;

/**
 * @author Rinat Suleymanov
 */
public class Main {

  public static void main(String[] args) {

    CatFramework.configure("ru.cg.cat_framework", new JavaConfig());
    CatFramework.getInstance().createBeans();

    Engine bean = CatFramework.getInstance().getBean(Engine.class);
    bean.start();
  }

}
