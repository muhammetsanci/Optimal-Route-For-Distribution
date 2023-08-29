# Optimal Route For Package Distribution

An algorithm that finds the optimal route for cargo package distribution using genetic algorithms.

## Problem

Suppose a cargo delivery vehicle has `n` packages to deliver and therefore `n` stops to go. The most optimal route for this vehicle to deliver packages depends on the distances between stops and also on the current load weight in the vehicle as it travels between the stops. In other words, the shortest path may not always be the most optimal solution, the packages that have more weight in the vehicle and restrict the vehicle speed may need to be distributed with higher priority. This algorithm creates the best time-dependent route based on the distance between stops and the total weight of packages available between the stops.

## Setting Up

> This algorithm uses genetic algorithms. If you don't know about it, please check the README.md at my [Genetic-Algorithms](https://github.com/muhammetsanci/Genetic-Algorithms) repository.

- **Genes** represents stops.
- **Chromosome** contains sequence of stops.
- **Fitness Value** is total time for distribution of all packages.
- **Termination Cause** is a limit number of generations which declared at the beginning.

## Constraints

- Number of packages fixed to 10 and maximum weight is 500 kilograms.
- Package weights and coordinates of stops created randomly at beginning.
- The locations of the package distribution center and the stations to be distributed will be determined by the coordinate system.
- Coordinates will be shown with a value between 0 and 1000.
- The location of the package distribution center is always the point `(0, 0)`.
- Each 1 unit in the coordinate system corresponds to 100 meters. For example the distance between points `(0, 0)` and `(0, 3)` is 300 meters.
- Speed availability is as follows:
<img width="1004" alt="Speed" src="https://github.com/muhammetsanci/Optimal-Route-For-Distribution/assets/77257193/ae79bbdb-8635-46e7-8c8a-5a5e5d91d359">

---

> If you want to set up coordinates and weights manually, you can uncomment the code block at the beginning to use. If you use it, remember to comment the code block that declares randomize packages. 
