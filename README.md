# Hide-and-seek platform

This is a platform for the Hide-and-seek search game simulation.

## Environment requirement

Require an environment with Java 1.8 or above version.

To build the package, it may required a stable network connection and Apache Maven 3.3 or above version.

## How to build the package

A sample shell script is provided in `/resources/build.sh`. It can be executed in any unix/linux machine.

## How to execute the simulations

Sepecified your simulation in `config.json`. Below is a sample.

```json
{
  "eachSimulationRepeat": 100,
  "vertexWeightUpperBound": 50,
  "edgeWeightUpperBound": 10,
  "simulations": [
    {
      "rounds": 1,
      "graph": {
        "topology": "random",
        "vertex": 100,
        "hideNumber": 5,
        "canKnowCostInAdvance": false
      },
      "hider": {
        "type": "normal",
        "strategy": "hAllLocation",
        "portion": 0.2
      },
      "seeker": {
        "type": "normal",
        "strategy": "sBacktrackGreedyAll"
      }
    }
  ]
}
```

- `eachSimulationRepeat` specify how many times that the simulation will be repeated
- `vertexWeightUpperBound` specify the upper bound of the vertex weight
- `edgeWeightUpperBound` specify the upper bound of the edge weight
- `simulations` decribes the simulations which will be executed
  - `rounds` determines the round of a single simulation
  - `graph` describe the physical graph in the simulation
    - `topology` could be `random`, `scalefree` or `ring`
    - `vertex` determines the size of nodes
    - `hideNumber` determines the number of hidden locations
    - `canKnowCostInAdvance` decide whether agents can know the node cost in advance
  - `hider` and `seeker` describe the cost 
    - `type` can only be `normal` in this version. Which represent normal agents.
    - `strategy` is the strategy used by the normal agent
    - `portion` is the portion of graph exploration in stratgies with node preference

Call the Java class `ac.kcl.inf.has.simulation.Simulation` to run the simulations. A sample shell script is provided in `resources/run.sh`.

## Acknowledgement

This project is inspired by the [HANDS](https://github.com/martinchapman/hands) platfrom developed by [Dr Martin Chapman](https://martinchapman.co.uk/) from king's College London.
