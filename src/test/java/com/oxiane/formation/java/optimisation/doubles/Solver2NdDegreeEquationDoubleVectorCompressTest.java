package com.oxiane.formation.java.optimisation.doubles;


import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Solver2NdDegreeEquationDoubleVectorCompressTest {

  @Test
  @DisplayName("Check that solutions have same order of equations")
  public void testOrder() {
    // Given
    List<EquationDouble> equationDoubles = getEquations();
    // When
    List<EquationSolutionDouble> actual = new Solver2NdDegreeEquationDoubleVectorCompress().solve(equationDoubles);
    // Then
    List<EquationDouble> equationsFromSolutions = actual.stream()
                                                        .map(EquationSolutionDouble::equation)
                                                        .toList();
    Assertions
        .assertThat(equationsFromSolutions)
        .hasSameElementsAs(equationDoubles);
  }

  @Test
  @DisplayName("Check that discrimants are correctly calculated")
  public void test1() {
    // Given
    List<EquationDouble> equationDoubles = getEquations();
    List<EquationSolutionDouble> expected = new Solver2NdDegreeEquationDoubleRegular()
        .solve(equationDoubles);
    // When
    List<EquationSolutionDouble> actual = new Solver2NdDegreeEquationDoubleVectorCompress()
        .solve(equationDoubles);
    // Then
    SoftAssertions softly = new SoftAssertions();
    for (int i = 0; i < actual.size(); i++) {
      EquationSolutionDouble solution = actual.get(i);
      EquationSolutionDouble expectedSolution = expected.get(i);
      softly
          .assertThat(solution.discriminant())
          .isEqualTo(expectedSolution.discriminant());
      softly.assertThat(solution.discriminant())
            .isEqualTo(expectedSolution.discriminant());
    }
    softly.assertAll();
  }

  @Test
  @DisplayName("Checks roots for discriminent 0")
  public void test2() {
    // Given
    List<EquationDouble> equationDoubles = getEquations();
    List<EquationSolutionDouble> expected = new Solver2NdDegreeEquationDoubleRegular()
        .solve(equationDoubles);
    // When
    List<EquationSolutionDouble> actual = new Solver2NdDegreeEquationDoubleVectorCompress()
        .solve(equationDoubles);
    // Then
    SoftAssertions softly = new SoftAssertions();
    softly
        .assertThat(actual.getFirst()
                          .discriminant())
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
    List<EquationDouble> equationDoubles = getEquations();
    List<EquationSolutionDouble> expected = new Solver2NdDegreeEquationDoubleRegular()
        .solve(equationDoubles);
    // When
    List<EquationSolutionDouble> actual = new Solver2NdDegreeEquationDoubleVectorCompress()
        .solve(equationDoubles);
    // Then
    SoftAssertions softly = new SoftAssertions();
    for (int i = 0; i < actual.size(); i++) {
      EquationSolutionDouble solution = actual.get(i);
      EquationSolutionDouble expectedSolution = expected.get(i);
      if (solution.discriminant() > 0) {
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
    ArrayList<EquationDouble> equationDoubles = new ArrayList<>(nbElementsToHaveInList);
    equationDoubles.addAll(getEquations().subList(0, nbElementsToHaveInList - 1));
    equationDoubles.add(new EquationDouble(2d, 5d, 2d));
    Assertions.assertThat(equationDoubles)
              .hasSize(nbElementsToHaveInList);
    double d = 9d;
    double r1 = -2d;
    double r2 = -0.5d;
    // When
    EquationSolutionDouble lastEquationSolutionDouble =
        new Solver2NdDegreeEquationDoubleVectorCompress()
            .solve(equationDoubles)
            .getLast();
    // Then
    Assertions.assertThat(lastEquationSolutionDouble.discriminant())
              .isEqualTo(d);
    Assertions.assertThat(lastEquationSolutionDouble.solutions())
              .hasSize(2);
    Assertions.assertThat(lastEquationSolutionDouble.solutions()
                                                    .first())
              .isEqualTo(r1);
    Assertions.assertThat(lastEquationSolutionDouble.solutions()
                                                    .last())
              .isEqualTo(r2);
  }

  @Test
  @DisplayName("Testing 2 roots in the corrective loop with (a+c)!=(ac)")
  public void test5() {
    // Given
    int nbElementsToHaveInList = 512 / (Double.BYTES * 8) + 1;
    ArrayList<EquationDouble> equationDoubles = new ArrayList<>(nbElementsToHaveInList);
    equationDoubles.addAll(getEquations().subList(0, nbElementsToHaveInList - 1));
    equationDoubles.add(new EquationDouble(4d / 3d, 5d, 3d));
    Assertions.assertThat(equationDoubles)
              .hasSize(nbElementsToHaveInList);
    double d = 9d;
    double r1 = -3d;
    double r2 = -0.75d;
    // When
    EquationSolutionDouble lastEquationSolutionDouble =
        new Solver2NdDegreeEquationDoubleVectorCompress()
            .solve(equationDoubles)
            .getLast();
    // Then
    Assertions.assertThat(lastEquationSolutionDouble.discriminant())
              .isEqualTo(d);
    Assertions.assertThat(lastEquationSolutionDouble.solutions())
              .hasSize(2);
    Assertions.assertThat(
                  lastEquationSolutionDouble
                      .solutions()
                      .first())
              .isEqualTo(r1);
    Assertions.assertThat(
                  lastEquationSolutionDouble
                      .solutions()
                      .last())
              .isEqualTo(r2);
  }

  private static List<EquationDouble> getEquations() {
    List<EquationDouble> equationDoubles = new ArrayList<>(20);
    Random random = new Random(123456789l);
    equationDoubles.add(new EquationDouble(1d, 4d, 4d)); // this one, discriminent is 0. Must be first element
    equationDoubles.add(new EquationDouble(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    equationDoubles.add(new EquationDouble(
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000),
        random.nextDouble(-1000, 1000)));
    return equationDoubles;
  }
}