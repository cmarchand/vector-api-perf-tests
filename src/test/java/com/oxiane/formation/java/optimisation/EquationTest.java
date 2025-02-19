package com.oxiane.formation.java.optimisation;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

public class EquationTest {

  @Test
  @DisplayName("Check if 2 equals Equation are effectively equals")
  public void test1() {
    // Given
    double a = 1d, b = 2d, c = 3d;
    Equation equation1 = new Equation(a, b, c);
    Equation equation2 = new Equation(a, b, c);
    // When

    // Then
    Assertions
        .assertThat(equation1)
        .isEqualTo(equation2);
  }

  @Test
  @DisplayName("Check if an value can be retrieved from a Map via a different instance of key")
  public void test2() {
    // Given
    double a = 1d, b = 2d, c = 3d;
    Equation equation1 = new Equation(a, b, c);
    Equation equation2 = new Equation(a, b, c);
    ConcurrentHashMap<Equation, Object> map = new ConcurrentHashMap<>();
    Object value = new Object();
    map.put(equation1, value);
    // When
    Object actual = map.get(equation2);
    // Then
    SoftAssertions softly = new SoftAssertions();
    softly
        .assertThat(map)
            .hasSize(1);
    softly
        .assertThat(actual)
        .isNotNull();
    softly
        .assertThat(actual)
        .isEqualTo(value);
    softly.assertAll();
  }
}
