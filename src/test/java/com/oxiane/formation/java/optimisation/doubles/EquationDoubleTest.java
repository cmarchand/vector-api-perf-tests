package com.oxiane.formation.java.optimisation.doubles;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

public class EquationDoubleTest {

  @Test
  @DisplayName("Check if 2 equals Equation are effectively equals")
  public void test1() {
    // Given
    double a = 1d, b = 2d, c = 3d;
    EquationDouble equationDouble1 = new EquationDouble(a, b, c);
    EquationDouble equationDouble2 = new EquationDouble(a, b, c);
    // When

    // Then
    Assertions
        .assertThat(equationDouble1)
        .isEqualTo(equationDouble2);
  }

  @Test
  @DisplayName("Check if an value can be retrieved from a Map via a different instance of key")
  public void test2() {
    // Given
    double a = 1d, b = 2d, c = 3d;
    EquationDouble equationDouble1 = new EquationDouble(a, b, c);
    EquationDouble equationDouble2 = new EquationDouble(a, b, c);
    ConcurrentHashMap<EquationDouble, Object> map = new ConcurrentHashMap<>();
    Object value = new Object();
    map.put(equationDouble1, value);
    // When
    Object actual = map.get(equationDouble2);
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
