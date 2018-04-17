package ru.cg.cat_framework.business;

import ru.cg.cat_framework.framework.annnotation.AfterInit;
import ru.cg.cat_framework.framework.annnotation.Bean;
import ru.cg.cat_framework.framework.annnotation.InjectByType;
import ru.cg.cat_framework.framework.annnotation.InjectRandomName;

/**
 * @author Rinat Suleymanov
 */
@Bean
public class Car {

  @InjectByType
  private Engine engine;
  @InjectByType
  private Speaker speaker;

  @InjectRandomName
  private String name;

  @AfterInit
  public void goToHome() {
    System.out.println("Я машина " + name);
    speaker.notify("Поехали");
    engine.start();
    speaker.notify("Мы приехали домой");
  }
}
