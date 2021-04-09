package com.github.bishoybasily.springframework.commons.core.utils;

import java.util.Random;

/**
 * @author bishoybasily
 * @since 2021-03-27
 */
public class NumberUtils {

  public static Integer random(int min, int max) {
    return new Random().nextInt((max - min) + 1) + min;
  }

}
