package com.github.bishoybasily.springframework.commons.core.utils;

import java.util.Random;

/**
 * @author bishoybasily
 * @since 2021-03-27
 */
public class NumberUtils {

  public static Integer random() {
    int min = 111111;
    int max = 999999;
    return random(min, max);
  }

  public static Integer random(int min, int max) {
    return new Random().nextInt(max - min + 1) + min;
  }

  public static Double[][] ranges(double min, double step, int count, double fraction) {

    Double[][] result = new Double[count][2];

    result[0][0] = null;
    result[0][1] = min - fraction;

    for (int i = 1; i < count - 1; i++) {
      result[i][0] = result[i - 1][1] + fraction;
      result[i][1] = min + (step * i) - fraction;
    }

    result[count - 1][0] = (step * (count - 1)) - fraction;
    result[count - 1][1] = null;

    return result;
  }

}
