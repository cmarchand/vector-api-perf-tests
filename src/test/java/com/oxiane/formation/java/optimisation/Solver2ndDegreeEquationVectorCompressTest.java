package com.oxiane.formation.java.optimisation;


import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Solver2ndDegreeEquationVectorCompressTest {

  @Test
  @DisplayName("Check that solutions have same order of equations")
  public void testOrder() {
    // Given
    List<Equation> equations = getEquations();
    // When
    List<EquationSolution> actual = new Solver2ndDegreeEquationVectorCompress().solve(equations);
    // Then
    List<Equation> equationsFromSolutions = actual.stream()
                                                  .map(EquationSolution::equation)
                                                  .toList();
    Assertions
        .assertThat(equationsFromSolutions)
        .hasSameElementsAs(equations);
  }

  @Test
  @DisplayName("Check that discrimants are correctly calculated")
  public void test1() {
    // Given
    List<Equation> equations = getEquations();
    List<EquationSolution> expected = new Solver2ndDegreeEquationRegular()
        .solve(equations);
    // When
    List<EquationSolution> actual = new Solver2ndDegreeEquationVectorCompress()
        .solve(equations);
    // Then
    SoftAssertions softly = new SoftAssertions();
    for (int i = 0; i < actual.size(); i++) {
      EquationSolution solution = actual.get(i);
      EquationSolution expectedSolution = expected.get(i);
      softly
          .assertThat(solution.discriminent())
          .isEqualTo(expectedSolution.discriminent());
      softly.assertThat(solution.discriminent())
            .isEqualTo(expectedSolution.discriminent());
    }
    softly.assertAll();
  }

  @Test
  @DisplayName("Checks roots for discriminent 0")
  public void test2() {
    // Given
    List<Equation> equations = getEquations();
    List<EquationSolution> expected = new Solver2ndDegreeEquationRegular()
        .solve(equations);
    // When
    List<EquationSolution> actual = new Solver2ndDegreeEquationVectorCompress()
        .solve(equations);
    // Then
    SoftAssertions softly = new SoftAssertions();
    softly
        .assertThat(actual.getFirst()
                          .discriminent())
        .isEqualTo(0d);
    softly
        .assertThat(actual
            .getFirst()
            .solutions())
        .hasSize(1);
    softly
        .assertThat(actual
            .getFirst()
            .solutions()
            .first())
        .isEqualTo(expected
            .getFirst()
            .solutions()
            .first());
    softly.assertAll();
  }

  @Test
  @DisplayName("Check that roots are correctly calculated")
  public void test3() {
    // Given
    List<Equation> equations = getEquations();
    List<EquationSolution> expected = new Solver2ndDegreeEquationRegular()
        .solve(equations);
    // When
    List<EquationSolution> actual = new Solver2ndDegreeEquationVectorCompress()
        .solve(equations);
    // Then
    SoftAssertions softly = new SoftAssertions();
    for (int i = 0; i < actual.size(); i++) {
      EquationSolution solution = actual.get(i);
      EquationSolution expectedSolution = expected.get(i);
      if (solution.discriminent() > 0) {
        softly
            .assertThat(solution.solutions())
            .hasSize(2);
        softly
            .assertThat(solution
                .solutions()
                .first())
            .isEqualTo(expectedSolution
                .solutions()
                .first());
        softly
            .assertThat(solution
                .solutions()
                .last())
            .isEqualTo(expectedSolution
                .solutions()
                .last());
      }
    }
    softly.assertAll();
  }

  @Test
  @DisplayName("Testing 2 roots in the corrective loop")
  public void test4() {
    // Given
    int nbElementsToHaveInList = 512 / (Double.BYTES * 8) + 1;
    ArrayList<Equation> equations = new ArrayList<>(nbElementsToHaveInList);
    equations.addAll(getEquations().subList(0, nbElementsToHaveInList - 1));
    equations.add(new Equation(2d, 5d, 2d));
    Assertions.assertThat(equations)
              .hasSize(nbElementsToHaveInList);
    double d = 9d;
    double r1 = -2d;
    double r2 = -0.5d;
    // When
    EquationSolution lastEquationSolution =
        new Solver2ndDegreeEquationVectorCompress()
            .solve(equations)
            .getLast();
    // Then
    Assertions.assertThat(lastEquationSolution.discriminent())
              .isEqualTo(d);
    Assertions.assertThat(lastEquationSolution.solutions())
              .hasSize(2);
    Assertions.assertThat(lastEquationSolution.solutions()
                                              .first())
              .isEqualTo(r1);
    Assertions.assertThat(lastEquationSolution.solutions()
                                              .last())
              .isEqualTo(r2);
  }

  @Test
  @DisplayName("Testing 2 roots in the corrective loop with (a+c)!=(ac)")
  public void test5() {
    // Given
    int nbElementsToHaveInList = 512 / (Double.BYTES * 8) + 1;
    ArrayList<Equation> equations = new ArrayList<>(nbElementsToHaveInList);
    equations.addAll(getEquations().subList(0, nbElementsToHaveInList - 1));
    equations.add(new Equation(4d / 3d, 5d, 3d));
    Assertions.assertThat(equations)
              .hasSize(nbElementsToHaveInList);
    double d = 9d;
    double r1 = -3d;
    double r2 = -0.75d;
    // When
    EquationSolution lastEquationSolution =
        new Solver2ndDegreeEquationVectorCompress()
            .solve(equations)
            .getLast();
    // Then
    Assertions.assertThat(lastEquationSolution.discriminent())
              .isEqualTo(d);
    Assertions.assertThat(lastEquationSolution.solutions())
              .hasSize(2);
    Assertions.assertThat(
                  lastEquationSolution
                      .solutions()
                      .first())
              .isEqualTo(r1);
    Assertions.assertThat(
                  lastEquationSolution
                      .solutions()
                      .last())
              .isEqualTo(r2);
  }

  private static List<Equation> getEquations() {
    List<Equation> equations = new ArrayList<>(20);
    Random random = new Random(123456789l);
    equations.add(new Equation(1d, 4d, 4d)); // this one, discriminent is 0. Must be first element
    equations.add(new Equation(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equations.add(new Equation(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equations.add(new Equation(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equations.add(new Equation(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equations.add(new Equation(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equations.add(new Equation(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equations.add(new Equation(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equations.add(new Equation(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equations.add(new Equation(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equations.add(new Equation(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equations.add(new Equation(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equations.add(new Equation(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    return equations;
  }
}