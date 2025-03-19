package engine;

import engine.bodies.Body;

import java.util.ArrayList;
import java.util.List;

public class Scene
{
    public List<Body> bodies = new ArrayList<>();
    public double timeStep = 0d;

    public void add(Body bodyToAdd) {
        bodies.add(bodyToAdd);
    }

    public void add(List<Body> bodiesToAdd) {
        bodies.addAll(bodiesToAdd);
    }
}
