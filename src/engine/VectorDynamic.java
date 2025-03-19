package engine;

import java.util.Arrays;

public class VectorDynamic
{
    public final double[] data;

    // Constructor that takes a variable number of components.
    public VectorDynamic(double... values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("A vector must have at least one dimension.");
        }
        this.data = values.clone();
    }

    // Returns the number of dimensions.
    public int dimension() {
        return data.length;
    }

    // Returns the value at a specific index.
    public double get(int index) {
        return data[index];
    }

    // Sets the value at a specific index.
    public void set(int index, double value) {
        data[index] = value;
    }

    // --- In-Place Addition ---
    // Adds another vector to this one (component-wise).
    public VectorDynamic add(VectorDynamic other) {
        checkDimensions(other);
        for (int i = 0; i < dimension(); i++) {
            this.data[i] += other.data[i];
        }
        return this;
    }

    // Adds a scalar to each component.
    public VectorDynamic add(double scalar) {
        for (int i = 0; i < dimension(); i++) {
            this.data[i] += scalar;
        }
        return this;
    }

    // --- In-Place Subtraction ---
    // Subtracts another vector from this one (component-wise).
    public VectorDynamic sub(VectorDynamic other) {
        checkDimensions(other);
        for (int i = 0; i < dimension(); i++) {
            this.data[i] -= other.data[i];
        }
        return this;
    }

    // Subtracts a scalar from each component.
    public VectorDynamic sub(double scalar) {
        for (int i = 0; i < dimension(); i++) {
            this.data[i] -= scalar;
        }
        return this;
    }

    // --- In-Place Multiplication ---
    // Multiplies each component by a scalar.
    public VectorDynamic mul(double scalar) {
        for (int i = 0; i < dimension(); i++) {
            this.data[i] *= scalar;
        }
        return this;
    }

    // Component-wise (Hadamard) multiplication.
    public VectorDynamic mul(VectorDynamic other) {
        checkDimensions(other);
        for (int i = 0; i < dimension(); i++) {
            this.data[i] *= other.data[i];
        }
        return this;
    }

    // --- In-Place Division ---
    // Divides each component by a scalar.
    public VectorDynamic div(double scalar) {
        for (int i = 0; i < dimension(); i++) {
            this.data[i] /= scalar;
        }
        return this;
    }

    // Component-wise division.
    public VectorDynamic div(VectorDynamic other) {
        checkDimensions(other);
        for (int i = 0; i < dimension(); i++) {
            if (other.data[i] == 0) {
                throw new ArithmeticException("Division by zero at index " + i);
            }
            this.data[i] /= other.data[i];
        }
        return this;
    }

    // --- Dot Product ---
    // Returns the dot product of this vector with another.
    public double dot(VectorDynamic other) {
        checkDimensions(other);
        double sum = 0;
        for (int i = 0; i < dimension(); i++) {
            sum += this.data[i] * other.data[i];
        }
        return sum;
    }

    // --- In-Place Cross Product (only for 3D vectors) ---
    public VectorDynamic cross(VectorDynamic other) {
        if (this.dimension() != 3 || other.dimension() != 3) {
            throw new UnsupportedOperationException("Cross product is only defined for 3D vectors.");
        }
        double x = this.data[1] * other.data[2] - this.data[2] * other.data[1];
        double y = this.data[2] * other.data[0] - this.data[0] * other.data[2];
        double z = this.data[0] * other.data[1] - this.data[1] * other.data[0];
        this.data[0] = x;
        this.data[1] = y;
        this.data[2] = z;
        return this;
    }

    // --- Utility Methods ---
    // Returns the magnitude (length) of the vector.
    public double magnitude() {
        return Math.sqrt(this.dot(this));
    }

    // In-place normalization: scales the vector to have a magnitude of 1.
    public VectorDynamic normalize() {
        double mag = magnitude();
        if (mag == 0) {
            throw new ArithmeticException("Cannot normalize a zero vector.");
        }
        return this.div(mag);
    }

    // Checks that the dimensions of two vectors are the same.
    private void checkDimensions(VectorDynamic other) {
        if (this.dimension() != other.dimension()) {
            throw new IllegalArgumentException("Vectors must have the same number of dimensions.");
        }
    }
    public VectorDynamic copy() {
        return new VectorDynamic(this.data);
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof VectorDynamic)) return false;
        VectorDynamic other = (VectorDynamic) obj;
        return Arrays.equals(this.data, other.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
