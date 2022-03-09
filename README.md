# jason-util

## A Jason architecture realising incremental agent state logging.

Located in `arch`.

## Some examples

Run examples with `gradle` or `gradle [...]`.

## How to run the code

Download the correct jazzer binary file, depending on your operating system, from [here](https://github.com/CodeIntelligenceTesting/jazzer/releases/tag/v0.10.0). Place it in the root folder of this project. 

Gradle tasks available:
- `gradle toJar` generates a .jar of the MAS application needed by Jazzer
- `gradle jazzer` runs Jazzer which is going to run the MAS application many times until a bug is found; then it halts
- `gradle cleanLogs` deletes every debugging file from both MAS and Jazzer logs
