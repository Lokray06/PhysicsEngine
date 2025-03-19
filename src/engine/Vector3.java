package engine;

public class Vector3 implements Vector {
    public final double x;
    public final double y;
    public final double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int dimension() {
        return 3;
    }

    @Override
    public double get(int index) {
        switch (index) {
            case 0: return x;
            case 1: return y;
            case 2: return z;
            default: throw new IndexOutOfBoundsException("Vector3 only has indices 0, 1, and 2.");
        }
    }

    @Override
    public Vector add(Vector other) {
        if (other.dimension() != 3) {
            throw new IllegalArgumentException("Dimension mismatch: expected 3, got " + other.dimension());
        }
        return new Vector3(x + other.get(0), y + other.get(1), z + other.get(2));
    }

    @Override
    public Vector add(double scalar) {
        return new Vector3(x + scalar, y + scalar, z + scalar);
    }

    @Override
    public Vector sub(Vector other) {
        if (other.dimension() != 3) {
            throw new IllegalArgumentException("Dimension mismatch: expected 3, got " + other.dimension());
        }
        return new Vector3(x - other.get(0), y - other.get(1), z - other.get(2));
    }

    @Override
    public Vector sub(double scalar) {
        return new Vector3(x - scalar, y - scalar, z - scalar);
    }

    @Override
    public Vector mul(double scalar) {
        return new Vector3(x * scalar, y * scalar, z * scalar);
    }

    @Override
    public Vector mul(Vector other) {
        if (other.dimension() != 3) {
            throw new IllegalArgumentException("Dimension mismatch: expected 3, got " + other.dimension());
        }
        return new Vector3(x * other.get(0), y * other.get(1), z * other.get(2));
    }

    @Override
    public Vector div(double scalar) {
        if (scalar == 0) {
            throw new ArithmeticException("Division by zero.");
        }
        return new Vector3(x / scalar, y / scalar, z / scalar);
    }

    @Override
    public Vector div(Vector other) {
        if (other.dimension() != 3) {
            throw new IllegalArgumentException("Dimension mismatch: expected 3, got " + other.dimension());
        }
        double ox = other.get(0);
        double oy = other.get(1);
        double oz = other.get(2);
        if (ox == 0 || oy == 0 || oz == 0) {
            throw new ArithmeticException("Division by zero component.");
        }
        return new Vector3(x / ox, y / oy, z / oz);
    }

    @Override
    public double dot(Vector other) {
        if (other.dimension() != 3) {
            throw new IllegalArgumentException("Dimension mismatch: expected 3, got " + other.dimension());
        }
        return x * other.get(0) + y * other.get(1) + z * other.get(2);
    }

    @Override
    public Vector cross(Vector other) {
        if (other.dimension() != 3) {
            throw new IllegalArgumentException("Dimension mismatch: expected 3, got " + other.dimension());
        }
        double cx = y * other.get(2) - z * other.get(1);
        double cy = z * other.get(0) - x * other.get(2);
        double cz = x * other.get(1) - y * other.get(0);
        return new Vector3(cx, cy, cz);
    }

    @Override
    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public Vector normalize() {
        double mag = magnitude();
        if (mag == 0) {
            throw new ArithmeticException("Cannot normalize a zero vector.");
        }
        return new Vector3(x / mag, y / mag, z / mag);
    }

    @Override
    public Vector copy() {
        return new Vector3(x, y, z);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector3)) return false;
        Vector3 other = (Vector3) obj;
        return Double.compare(x, other.x) == 0 &&
               Double.compare(y, other.y) == 0 &&
               Double.compare(z, other.z) == 0;
    }

    @Override
    public int hashCode() {
        int result = 17;
        long temp = Double.doubleToLongBits(x);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
