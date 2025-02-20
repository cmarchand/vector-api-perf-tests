package com.oxiane.formation.java.optimisation.floats;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Solver2NdDegreeEquationFloatRegularTest {

  @Test
  @DisplayName("given (1, 2, 3) checks that discriminent is -8")
  public void test10() {
    // Given
    EquationFloat equation = new EquationFloat(1f, 2f, 3f);
    float expected = -8.0f;
    // When
    EquationSolutionFloat actual = Solver2NdDegreeEquationFloatRegular.solve(equation);
    // Then
    Assertions.assertThat(actual.discriminant()).isEqualTo(expected);
  }

  @Test
  @DisplayName("given (1, 2, 3) checks that solution has no root")
  public void test11() {
    // Given
    EquationFloat equation = new EquationFloat(1f, 2f, 3f);
    int expected = 0;
    // When
    EquationSolutionFloat actual = Solver2NdDegreeEquationFloatRegular.solve(equation);
    // Then
    Assertions.assertThat(actual.solutions()).hasSize(expected);
  }

  @Test
  @DisplayName("given (1, 7, 1) checks that discriminent is 45")
  public void test20() {
    // Given
    EquationFloat equation = new EquationFloat(1, 7, 1f);
    float expected = 45f;
    // When
    EquationSolutionFloat actual = Solver2NdDegreeEquationFloatRegular.solve(equation);
    // Then
    Assertions.assertThat(actual.discriminant()).isEqualTo(expected);
  }
  @Test
  @DisplayName("given (1, 7, 1) checks that solution has 2 roots")
  public void test21() {
    // Given
    EquationFloat equation = new EquationFloat(1f, 7f, 1f);
    int expected = 2;
    // When
    EquationSolutionFloat actual = Solver2NdDegreeEquationFloatRegular.solve(equation);
    // Then
    Assertions.assertThat(actual.solutions()).hasSize(expected);
  }

  @Test
  @DisplayName("given (1, 7, 1) checks that solution are -6.854 and -0.146")
  public void test22() {
    // Given
    EquationFloat equation = new EquationFloat(1f, 7f, 1f);
    float expected1 = -6.854f;
    float expected2 = -0.146f;
    // When
    EquationSolutionFloat actual = Solver2NdDegreeEquationFloatRegular.solve(equation);
    // Then
    Assertions.assertThat(actual.solutions().getFirst()).isCloseTo(expected1, Offset.offset(0.002f));
    Assertions.assertThat(actual.solutions().getLast()).isCloseTo(expected2, Offset.offset(0.002f));
  }

  @Test
  @DisplayName("given (1, 4, 4) checks that discriminent is 0")
  public void test30() {
    // Given
    EquationFloat equation = new EquationFloat(1f, 4f, 4f);
    float expected = 0f;
    // When
    EquationSolutionFloat actual = Solver2NdDegreeEquationFloatRegular.solve(equation);
    // Then
    Assertions.assertThat(actual.discriminant()).isEqualTo(expected);
  }
  @Test
  @DisplayName("given (1, 4, 4) checks that solution has exactly 1 root")
  public void test31() {
    // Given
    EquationFloat equation = new EquationFloat(1f, 4f, 4f);
    int expected = 1;
    // When
    EquationSolutionFloat actual = Solver2NdDegreeEquationFloatRegular.solve(equation);
    // Then
    Assertions.assertThat(actual.solutions()).hasSize(expected);
  }

  @Test
  @DisplayName("given (1, 4, 4) checks that solution is 8")
  public void test32() {
    // Given
    EquationFloat equation = new EquationFloat(1f, 4f, 4f);
    float expected = -2f;
    // When
    EquationSolutionFloat actual = Solver2NdDegreeEquationFloatRegular.solve(equation);
    // Then
    Assertions.assertThat(actual.solutions().getFirst()).isEqualTo(expected);
  }

}