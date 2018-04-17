package ru.cg.cat_framework.business;

import ru.cg.cat_framework.framework.annnotation.Bean;
import ru.cg.cat_framework.framework.annnotation.InjectRandomName;

/**
 * @author Rinat Suleymanov
 */
@Bean
public class SpeakerImpl implements Speaker {

  @InjectRandomName
  private String name;

  @Override
  public void notify(String message) {
    System.out.println(name + ": " + message);
  }
}
