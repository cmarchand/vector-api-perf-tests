package com.oxiane.formation.java.optimisation;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Solver2ndDegreeEquationRegularTest {

  @Test
  @DisplayName("given (1, 2, 3) checks that discriminent is -8")
  public void test10() {
    // Given
    Equation equation = new Equation(1d, 2d, 3d);
    double expected = -8.0;
    // When
    EquationSolution actual = Solver2ndDegreeEquationRegular.solve(equation);
    // Then
    Assertions.assertThat(actual.discriminant()).isEqualTo(expected);
  }

  @Test
  @DisplayName("given (1, 2, 3) checks that solution has no root")
  public void test11() {
    // Given
    Equation equation = new Equation(1d, 2d, 3d);
    int expected = 0;
    // When
    EquationSolution actual = Solver2ndDegreeEquationRegular.solve(equation);
    // Then
    Assertions.assertThat(actual.solutions()).hasSize(expected);
  }

  @Test
  @DisplayName("given (1, 7, 1) checks that discriminent is 45")
  public void test20() {
    // Given
    Equation equation = new Equation(1d, 7d, 1d);
    double expected = 45;
    // When
    EquationSolution actual = Solver2ndDegreeEquationRegular.solve(equation);
    // Then
    Assertions.assertThat(actual.discriminant()).isEqualTo(expected);
  }
  @Test
  @DisplayName("given (1, 7, 1) checks that solution has 2 roots")
  public void test21() {
    // Given
    Equation equation = new Equation(1d, 7d, 1d);
    int expected = 2;
    // When
    EquationSolution actual = Solver2ndDegreeEquationRegular.solve(equation);
    // Then
    Assertions.assertThat(actual.solutions()).hasSize(expected);
  }

  @Test
  @DisplayName("given (1, 7, 1) checks that solution are -6.854 and -0.146")
  public void test22() {
    // Given
    Equation equation = new Equation(1d, 7d, 1d);
    double expected1 = -6.854;
    double expected2 = -0.146;
    // When
    EquationSolution actual = Solver2ndDegreeEquationRegular.solve(equation);
    // Then
    Assertions.assertThat(actual.solutions().getFirst()).isCloseTo(expected1, Offset.offset(0.002));
    Assertions.assertThat(actual.solutions().getLast()).isCloseTo(expected2, Offset.offset(0.002));
  }

  @Test
  @DisplayName("given (1, 4, 4) checks that discriminent is 0")
  public void test30() {
    // Given
    Equation equation = new Equation(1d, 4d, 4d);
    double expected = 0d;
    // When
    EquationSolution actual = Solver2ndDegreeEquationRegular.solve(equation);
    // Then
    Assertions.assertThat(actual.discriminant()).isEqualTo(expected);
  }
  @Test
  @DisplayName("given (1, 4, 4) checks that solution has exactly 1 root")
  public void test31() {
    // Given
    Equation equation = new Equation(1d, 4d, 4d);
    int expected = 1;
    // When
    EquationSolution actual = Solver2ndDegreeEquationRegular.solve(equation);
    // Then
    Assertions.assertThat(actual.solutions()).hasSize(expected);
  }

  @Test
  @DisplayName("given (1, 4, 4) checks that solution is 8")
  public void test32() {
    // Given
    Equation equation = new Equation(1d, 4d, 4d);
    double expected = -2d;
    // When
    EquationSolution actual = Solver2ndDegreeEquationRegular.solve(equation);
    // Then
    Assertions.assertThat(actual.solutions().getFirst()).isEqualTo(expected);
  }

}