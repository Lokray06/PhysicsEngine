package engine;

public class Vector2 implements Vector {
    public double x;
    public double y;
    
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public int dimension() {
        return 2;
    }
    
    @Override
    public double get(int index) {
        switch (index) {
            case 0: return x;
            case 1: return y;
            default: throw new IndexOutOfBoundsException("Vector2 only has indices 0 and 1.");
        }
    }
    
    @Override
    public Vector add(Vector other) {
        if (other.dimension() != 2) {
            throw new IllegalArgumentException("Dimension mismatch: expected 2, got " + other.dimension());
        }
        return new Vector2(x + other.get(0), y + other.get(1));
    }
    
    @Override
    public Vector add(double scalar) {
        return new Vector2(x + scalar, y + scalar);
    }
    
    @Override
    public Vector sub(Vector other) {
        if (other.dimension() != 2) {
            throw new IllegalArgumentException("Dimension mismatch: expected 2, got " + other.dimension());
        }
        return new Vector2(x - other.get(0), y - other.get(1));
    }
    
    @Override
    public Vector sub(double scalar) {
        return new Vector2(x - scalar, y - scalar);
    }
    
    @Override
    public Vector mul(double scalar) {
        return new Vector2(x * scalar, y * scalar);
    }
    
    @Override
    public Vector mul(Vector other) {
        if (other.dimension() != 2) {
            throw new IllegalArgumentException("Dimension mismatch: expected 2, got " + other.dimension());
        }
        return new Vector2(x * other.get(0), y * other.get(1));
    }
    
    @Override
    public Vector div(double scalar) {
        if (scalar == 0) {
            throw new ArithmeticException("Division by zero.");
        }
        return new Vector2(x / scalar, y / scalar);
    }
    
    @Override
    public Vector div(Vector other) {
        if (other.dimension() != 2) {
            throw new IllegalArgumentException("Dimension mismatch: expected 2, got " + other.dimension());
        }
        double ox = other.get(0);
        double oy = other.get(1);
        if (ox == 0 || oy == 0) {
            throw new ArithmeticException("Division by zero component.");
        }
        return new Vector2(x / ox, y / oy);
    }
    
    @Override
    public double dot(Vector other) {
        if (other.dimension() != 2) {
            throw new IllegalArgumentException("Dimension mismatch: expected 2, got " + other.dimension());
        }
        return x * other.get(0) + y * other.get(1);
    }
    
    @Override
    public Vector cross(Vector other) {
        throw new UnsupportedOperationException("Cross product is not defined for 2D vectors.");
    }
    
    @Override
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }
    
    @Override
    public Vector normalize() {
        double mag = magnitude();
        if (mag == 0) {
            throw new ArithmeticException("Cannot normalize a zero vector.");
        }
        return new Vector2(x / mag, y / mag);
    }
    
    @Override
    public Vector copy() {
        return new Vector2(x, y);
    }
    
    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector2)) return false;
        Vector2 other = (Vector2) obj;
        return Double.compare(x, other.x) == 0 &&
               Double.compare(y, other.y) == 0;
    }
    
    @Override
    public int hashCode() {
        int result = 17;
        long temp = Double.doubleToLongBits(x);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
