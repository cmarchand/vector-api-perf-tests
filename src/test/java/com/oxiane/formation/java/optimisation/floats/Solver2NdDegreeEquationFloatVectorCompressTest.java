package com.oxiane.formation.java.optimisation.floats;


import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Solver2NdDegreeEquationFloatVectorCompressTest {

  @Test
  @DisplayName("Check that solutions have same order of equations")
  public void testOrder() {
    // Given
    List<EquationFloat> equations = getEquations();
    // When
    List<EquationSolutionFloat> actual = new Solver2NdDegreeEquationFloatVectorCompress().solve(equations);
    // Then
    List<EquationFloat> equationsFromSolutions = actual.stream()
                                                        .map(EquationSolutionFloat::equation)
                                                        .toList();
    Assertions
        .assertThat(equationsFromSolutions)
        .hasSameElementsAs(equations);
  }

  @Test
  @DisplayName("Check that discrimants are correctly calculated")
  public void test1() {
    // Given
    List<EquationFloat> equations = getEquations();
    List<EquationSolutionFloat> expected = new Solver2NdDegreeEquationFloatRegular()
        .solve(equations);
    // When
    List<EquationSolutionFloat> actual = new Solver2NdDegreeEquationFloatVectorCompress()
        .solve(equations);
    // Then
    SoftAssertions softly = new SoftAssertions();
    for (int i = 0; i < actual.size(); i++) {
      EquationSolutionFloat solution = actual.get(i);
      EquationSolutionFloat expectedSolution = expected.get(i);
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
    List<EquationFloat> equationFloats = getEquations();
    List<EquationSolutionFloat> expected = new Solver2NdDegreeEquationFloatRegular()
        .solve(equationFloats);
    // When
    List<EquationSolutionFloat> actual = new Solver2NdDegreeEquationFloatVectorCompress()
        .solve(equationFloats);
    // Then
    SoftAssertions softly = new SoftAssertions();
    softly
        .assertThat(actual.getFirst()
                          .discriminant())
        .isEqualTo(0f);
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
    List<EquationFloat> equations = getEquations();
    List<EquationSolutionFloat> expected = new Solver2NdDegreeEquationFloatRegular()
        .solve(equations);
    // When
    List<EquationSolutionFloat> actual = new Solver2NdDegreeEquationFloatVectorCompress()
        .solve(equations);
    // Then
    SoftAssertions softly = new SoftAssertions();
    for (int i = 0; i < actual.size(); i++) {
      EquationSolutionFloat solution = actual.get(i);
      EquationSolutionFloat expectedSolution = expected.get(i);
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
    int nbElementsToHaveInList = 512 / (Float.BYTES * 8) + 1;
    ArrayList<EquationFloat> equations = new ArrayList<>(nbElementsToHaveInList);
    equations.addAll(getEquations().subList(0, nbElementsToHaveInList - 1));
    equations.add(new EquationFloat(2f, 5f, 2f));
    Assertions.assertThat(equations)
              .hasSize(nbElementsToHaveInList);
    float d = 9f;
    float r1 = -2f;
    float r2 = -0.5f;
    // When
    EquationSolutionFloat lastEquationSolutionFloat =
        new Solver2NdDegreeEquationFloatVectorCompress()
            .solve(equations)
            .getLast();
    // Then
    Assertions.assertThat(lastEquationSolutionFloat.discriminant())
              .isEqualTo(d);
    Assertions.assertThat(lastEquationSolutionFloat.solutions())
              .hasSize(2);
    Assertions.assertThat(lastEquationSolutionFloat.solutions()
                                                    .first())
              .isEqualTo(r1);
    Assertions.assertThat(lastEquationSolutionFloat.solutions()
                                                    .last())
              .isEqualTo(r2);
  }

  @Test
  @DisplayName("Testing 2 roots in the corrective loop with (a+c)!=(ac)")
  public void test5() {
    // Given
    int nbElementsToHaveInList = 512 / (Float.BYTES * 8) + 1;
    ArrayList<EquationFloat> equationFloats = new ArrayList<>(nbElementsToHaveInList);
    equationFloats.addAll(getEquations().subList(0, nbElementsToHaveInList - 1));
    equationFloats.add(new EquationFloat(4f / 3f, 5f, 3f));
    Assertions.assertThat(equationFloats)
              .hasSize(nbElementsToHaveInList);
    float d = 9f;
    float r1 = -3f;
    float r2 = -0.75f;
    // When
    EquationSolutionFloat lastEquationSolutionFloat =
        new Solver2NdDegreeEquationFloatVectorCompress()
            .solve(equationFloats)
            .getLast();
    // Then
    Assertions.assertThat(lastEquationSolutionFloat.discriminant())
              .isEqualTo(d);
    Assertions.assertThat(lastEquationSolutionFloat.solutions())
              .hasSize(2);
    Assertions.assertThat(
                  lastEquationSolutionFloat
                      .solutions()
                      .first())
              .isEqualTo(r1);
    Assertions.assertThat(
                  lastEquationSolutionFloat
                      .solutions()
                      .last())
              .isEqualTo(r2);
  }

  private static List<EquationFloat> getEquations() {
    List<EquationFloat> equationFloats = new ArrayList<>(20);
    Random random = new Random(123456789l);
    equationFloats.add(new EquationFloat(1f, 4f, 4f)); // this one, discriminent is 0. Must be first element
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    equationFloats.add(new EquationFloat(
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000),
        random.nextFloat(-1000, 1000)));
    return equationFloats;
  }
}