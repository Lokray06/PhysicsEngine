package engine;

public interface Vector {
    // Returns the number of dimensions.
    int dimension();
    
    // Gets the component at the specified index.
    double get(int index);
    
    // Returns a new vector with the components of this vector added to the corresponding components of the other vector.
    Vector add(Vector other);
    // Returns a new vector with each component increased by the given scalar.
    Vector add(double scalar);
    
    // Returns a new vector with the components of this vector subtracted by the corresponding components of the other vector.
    Vector sub(Vector other);
    // Returns a new vector with each component decreased by the given scalar.
    Vector sub(double scalar);
    
    // Returns a new vector with each component multiplied by the scalar.
    Vector mul(double scalar);
    // Returns a new vector with the components multiplied component-wise.
    Vector mul(Vector other);
    
    // Returns a new vector with each component divided by the scalar.
    Vector div(double scalar);
    // Returns a new vector with the components divided component-wise.
    Vector div(Vector other);
    
    // Returns the dot product of this vector with the other.
    double dot(Vector other);
    
    // Returns the cross product (only defined for 3D vectors).
    Vector cross(Vector other);
    
    // Returns the magnitude (length) of the vector.
    double magnitude();
    
    // Returns a new vector that is the normalized (unit-length) version of this vector.
    Vector normalize();
    
    // Returns a copy of the vector.
    Vector copy();
}
