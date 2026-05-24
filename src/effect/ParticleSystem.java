package effect;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import game.GameEngine;

/** Owns lightweight particles for future polish. */
public class ParticleSystem {
    private final List<Particle> particles = new ArrayList<Particle>();

    public void spawn(double x, double y, Color color) {
        particles.add(new Particle(x, y, 0.5, color));
    }

    public void update(double dt) {
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            particle.update(dt);
            if (particle.isFinished()) {
                iterator.remove();
            }
        }
    }

    public void draw(GameEngine engine) {
        for (Particle particle : particles) {
            particle.draw(engine);
        }
    }
}
