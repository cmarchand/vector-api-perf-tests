package com.oxiane.formation.java.optimisation;

/**
 * Models a 2nd degree quadratic equation.
 * <p>
 * A second degree quadratic equation is defined
 * by {@code ax2 + bx + c = 0}, where {@code x} models
 * the solutions to find.
 *
 * @param a square x coefficient
 * @param b x coefficient
 * @param c constant
 */
public record Equation(double a, double b, double c) {
}
