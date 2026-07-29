package io.github.brenoepics.fourier.core;

/**
 * One rotating vector of the Fourier series for a single frame: the circle it sweeps and the point
 * it ends at. {@code radius} keeps its sign (triangle waves alternate it); renderers should draw
 * the circle with {@code Math.abs(radius)}.
 */
public record Epicycle(double centerX, double centerY, double radius, double endX, double endY) {}
