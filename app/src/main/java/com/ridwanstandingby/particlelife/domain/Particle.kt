package com.ridwanstandingby.particlelife.domain

data class Particle(var x: Double, var y: Double, val species: ParticleSpecies)

data class ParticleSpecies(val color: Int)
