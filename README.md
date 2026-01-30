# VW Cleaning Robot

[![Build and Test](https://github.com/springerd/vw-cleaning-robot-challenge/actions/workflows/build.yml/badge.svg)](https://github.com/springerd/vw-cleaning-robot-challenge/actions/workflows/build.yml)

**Author:** Javier Teodoro Mediano Serrano                                                                                                                                                                                                                                                                                  
**Date:** January 2026

Cleaning robot that navigates in a rectangular space following movement instructions.

## Requirements

- Java 21+
- Maven 3.9+

## Build

```bash
mvn clean package
```

## Execution

```bash
java -jar target/vw-cleaning-robot-1.0.0.jar < input.txt
```

Or with Maven:
```bash
mvn exec:java -Dexec.mainClass="com.vw.cleaningrobot.Main" < input.txt
```

## Input Format

```
5 5          # Workspace size (X Y)
1 2 N        # Robot initial position (X Y Orientation)
LMLMLMLMM    # Instructions (L=left, R=right, M=move)
3 3 E        # Next robot...
MMRMMRMRRM
```

## Example

**Input:**
```
5 5
1 2 N
LMLMLMLMM
3 3 E
MMRMMRMRRM
```

**Output:**
```
1 3 N
5 1 E
```

## DDD Analysis

### Ubiquitous Language
- **Workspace**: Rectangular space where the robot operates
- **Position**: Robot's location and orientation (coordinate + orientation)
- **Coordinate**: Point in the plane (x, y)
- **Orientation**: Cardinal direction (N, E, S, W)
- **Command**: Individual instruction (L=turn left, R=turn right, M=move forward)
- **Instructions**: Sequence of commands to execute
- **Robot**: Aggregate that executes instructions and maintains its state

### Aggregates

**Robot** (Aggregate Root)
- Mutable entity that encapsulates robot state
- Responsible for validating movements within workspace
- Executes commands and instructions
- Invariant: Robot always within workspace boundaries

### Value Objects

- **Coordinate**: Mathematical primitive (x, y), accepts negatives
- **Position**: Immutable, combines coordinate + orientation
- **Workspace**: Immutable, defines space boundaries (validates non-negative)
- **Instructions**: Immutable, validated list of commands
- **Orientation**: Enum with rotation logic and directional vectors (dx, dy)
- **Command**: Simple enum

### Business Rules

1. **Out-of-bounds movement**: Robot ignores command and maintains position
2. **Rotation**: Always successful, changes orientation without moving coordinates
3. **Workspace validation**: Does not allow negative coordinates in boundaries
4. **Immutability**: Value objects are immutable, Robot is mutable

### Responsibilities

- **Position**: Knows how to calculate next position (moveForward, turnLeft, turnRight)
- **Orientation**: Knows directional vectors (dx, dy) and rotations
- **Robot**: Decides if a movement is valid according to workspace
- **Workspace**: Validates if a coordinate is within bounds

## Architecture

### Hexagonal Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                  Infrastructure Layer                       │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────┐       ┌─────────────────────────┐    │
│  │   Input Adapters    │       │    Output Adapters      │    │
│  ├─────────────────────┤       ├─────────────────────────┤    │
│  │ • PositionParser    │       │ • ConsoleOutputAdapter  │    │
│  │ • InstructionsParser│       │ • FileOutputAdapter     │    │
│  │ • WorkspaceParser   │       │ • PositionFormatter     │    │
│  │ • CommandParser     │       │                         │    │
│  └─────────────────────┘       └─────────────────────────┘    │
│           ↓                              ↑                 │
└───────────┼──────────────────────────────┼─────────────────┘
            ↓                              ↑
┌───────────┼──────────────────────────────┼─────────────────┐
│           ↓      Application Layer       ↑                 │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌──────────────────────────────────────────────────────┐  │
│  │         ExecuteRobotInstructionsUseCase              │  │
│  │  (Orchestrates domain, returns Position)             │  │
│  └──────────────────────────────────────────────────────┘  │
│           ↓                              ↑                 │
│  ┌─────────────────┐           ┌─────────────────┐        │
│  │   Input Port    │           │   Output Port   │        │
│  │  (interface)    │           │  (interface)    │        │
│  └─────────────────┘           └─────────────────┘        │
│                                                             │
└───────────┼─────────────────────────────────────────────────┘
            ↓
┌───────────┼─────────────────────────────────────────────────┐
│           ↓          Domain Layer                           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │   Entities   │  │ Value Objects│  │  Aggregates  │     │
│  ├──────────────┤  ├──────────────┤  ├──────────────┤     │
│  │ • Robot      │  │ • Position   │  │ • Robot      │     │
│  │              │  │ • Coordinate │  │   (root)     │     │
│  │              │  │ • Workspace  │  │              │     │
│  │              │  │ • Instructions│  │              │     │
│  │              │  │ • Orientation│  │              │     │
│  │              │  │ • Command    │  │              │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
│                                                             │
│  Pure business logic, no external dependencies             │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Project Structure

```
domain/
  └── model/           # Entities, Value Objects, Aggregates
application/
  ├── port/
  │   ├── input/       # Use cases (interfaces)
  │   └── output/      # Output ports
  └── usecase/         # Use case implementations
infrastructure/
  ├── input/
  │   └── parser/      # Parsers (String → Domain Objects)
  └── output/
      ├── adapter/     # Adapters (Console, File)
      └── formatter/   # Formatters (Domain Objects → String)
```

### Layers

- **Domain**: Pure business logic, no external dependencies
- **Application**: Orchestrates domain, defines ports
- **Infrastructure**: Adapters for input/output, parsing

See [ADR.md](ADR.md) for detailed architecture decisions.

## Tests

```bash
mvn test
```

**Coverage**: 59 test methods, 117 test executions
- Parameterized tests to reduce duplication
- Domain tests without external dependencies
- Integration tests for Main and adapters

## Key Decisions

- Robot ignores out-of-bounds movements (doesn't fail)
- Coordinate accepts negative values (mathematical primitive)
- Workspace validates non-negative boundaries
- Parsing separated from domain (in infrastructure)
- Rich domain model: logic in value objects, not in services
