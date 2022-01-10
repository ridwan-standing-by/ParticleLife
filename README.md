# ParticleLife

A particle simulation Android app, designed to illustrate life-like behaviour emerging from particles obeying a set of simple rules.

Each particle exerts a force on other nearby particles, depending on their species (represented by the different colours). This can either be a repulsion or an attraction, and is not necessarily symmetric. For example, red can be attracted to green, but green could be repulsed by red, which would result in red "chasing" green.

To avoid particles speeding up forever, a global friction is applied that slows particles down. Additionally, all particles repel each other if they get too close for comfort, like a kind of degeneracy pressure.

Play around with different settings to discover which parameters result in the most "life-like" creatures.
