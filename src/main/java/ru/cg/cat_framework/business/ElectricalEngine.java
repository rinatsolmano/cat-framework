package ru.cg.cat_framework.business;

import ru.cg.cat_framework.framework.annnotation.Bean;
import ru.cg.cat_framework.framework.annnotation.Benchmark;
import ru.cg.cat_framework.framework.annnotation.InjectRandomName;

/**
 * @author Rinat Suleymanov
 */
@Bean
@Benchmark
public class ElectricalEngine implements Engine {

  @InjectRandomName
  private String name;

  @Override
  public void start() {
    System.out.println(name + ": УУуууууууууууууииииииииии!!");
  }
}
