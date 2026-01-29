# Architecture Decision Records (ADR)

## ADR-001: Robot behavior when attempting to move outside workspace

**Status:** Decided

**Context:**
The challenge specification doesn't define what should happen when a robot attempts to move outside the workspace boundaries.

**Decision:**
Robot will ignore movements that would take it outside workspace bounds and remain in its current position.

**Alternatives Considered:**
1. Throw exception (IllegalStateException or custom RobotOutOfBoundsException)
2. Ignore movement silently (CHOSEN)
3. Log warning and ignore movement

**Rationale:**
- More robust: doesn't break execution for multiple robots
- More realistic: physical robot would stop at boundary
- Challenge doesn't specify, so defensive approach is safer

**Consequences:**
- Need to document this behavior in README
- Tests must verify this behavior

---

## ADR-002: Robot mutability (Aggregate Root)

**Status:** Decided

**Context:**
Should Robot be immutable (functional) or mutable (OOP/DDD)?

**Decision:**
Robot is mutable - position changes when executing commands.

**Rationale:**
- Aggregate Roots in DDD are typically mutable entities with lifecycle
- More natural representation of real-world robot
- More memory efficient
- Value Objects (Position, Coordinate) remain immutable

---

## ADR-003: Movement logic responsibility

**Status:** Decided

**Context:**
Where should the logic for calculating next coordinate based on orientation live?

**Decision:**
Movement logic lives in Position value object, not in Orientation or Coordinate.

**Alternatives Considered:**
1. Orientation.getNextCoordinate(Coordinate) - violates SRP
2. Coordinate.move(Orientation) - creates coupling
3. Position.moveForward() - CHOSEN

**Rationale:**
- Position has both coordinate and orientation, natural responsibility
- Keeps Coordinate and Orientation focused on their own concerns
- More cohesive design

---

## ADR-004: Rich Value Objects with behavior

**Status:** Decided

**Context:**
Should domain enums and value objects have behavior or be anemic?

**Decision:**
Implement rich value objects with behavior:
- Instructions as value object encapsulating list of commands
- Position with turnLeft(), turnRight(), moveForward()
- Orientation with turnLeft(), turnRight() and directional vectors (dx, dy)

**Rationale:**
- Better encapsulation and validation
- More expressive domain model
- Follows DDD principles

---

## ADR-005: Robot-Workspace coupling

**Status:** Decided (Keep coupled)

**Context:**
Should Robot have a direct reference to Workspace, or should boundary validation be external?

**Decision:**
Robot maintains reference to Workspace and validates boundaries internally.

**Alternatives Considered:**
1. External validation in service layer
2. Inject boundary validator interface
3. Keep Workspace in Robot (CHOSEN)

**Rationale:**
- Workspace is essential context for robot operation
- Challenge explicitly states "robot operates in a workspace"
- Workspace is a Value Object, not a problematic dependency
- Keeps validation logic cohesive within the Aggregate
- Simpler design without over-engineering

**Trade-offs:**
- Robot depends on Workspace (acceptable coupling)
- Testing requires providing Workspace (minimal impact)

---

## ADR-006: Parsing in Infrastructure layer (not Domain)

**Status:** Decided

**Context:**
Initially, Instructions had a static parse() method. Should parsing logic live in domain or infrastructure?

**Decision:**
Move all parsing logic to infrastructure layer:
- CommandParser: char → Command
- InstructionsParser: String → Instructions
- PositionParser: String → Position
- WorkspaceParser: String → Workspace

**Alternatives Considered:**
1. Static methods in domain objects (Instructions.parse())
2. Parsers in infrastructure layer (CHOSEN)

**Rationale:**
- Domain should be pure, without String parsing concerns
- Parsing is an infrastructure concern (input adaptation)
- Follows Hexagonal Architecture principles
- Parsers are reusable and testable independently
- Domain objects remain focused on business logic

**Consequences:**
- Domain layer has no parsing logic
- Infrastructure depends on domain (correct direction)
- Use case receives domain objects, not strings
- More classes but better separation of concerns

---

## ADR-007: Output Ports for decoupling output

**Status:** Decided

**Context:**
How to handle output (console, file, etc.) without coupling Main to specific implementations?

**Decision:**
Create OutputPort interface with adapters:
- OutputPort (interface in application layer)
- ConsoleOutputAdapter (writes to stdout)
- FileOutputAdapter (writes to file)
- PositionFormatter (converts Position → String)

**Alternatives Considered:**
1. System.out.println directly in Main
2. OutputPort abstraction (CHOSEN)

**Rationale:**
- Follows Hexagonal Architecture (ports & adapters)
- Main is testable without capturing System.out
- Easy to add new output destinations (HTTP, database, etc.)
- Formatter separated from adapter (SRP)

**Consequences:**
- More flexible and testable
- Slightly more code but better design
- Clear separation: formatting vs writing

---

## ADR-008: Orientation with directional vectors (dx, dy)

**Status:** Decided

**Context:**
Position.moveForward() needed to calculate next coordinate based on orientation. This required a switch statement duplicating orientation logic.

**Decision:**
Store directional vectors (dx, dy) in Orientation enum:
```java
N(0, 1), E(1, 0), S(0, -1), W(-1, 0)
```

**Alternatives Considered:**
1. Calculate dx/dy in Position with switch (duplicates logic)
2. Store dx/dy in Orientation (CHOSEN)

**Rationale:**
- Eliminates switch duplication
- More expressive: orientation "knows" its direction
- Better encapsulation: direction is intrinsic to orientation
- Simpler Position.moveForward() implementation

**Consequences:**
- Orientation is more than just an enum, it's a rich value object
- No switch statements for movement calculation
- More maintainable code

---

## ADR-009: Coordinate as mathematical primitive (allows negative values)

**Status:** Decided

**Context:**
Should Coordinate validate that x and y are non-negative, or allow any integer values including negatives?

**Decision:**
Coordinate is a mathematical primitive that accepts any integer values, including negatives. No validation in constructor.

**Alternatives Considered:**
1. Validate x >= 0 && y >= 0 in Coordinate constructor (reject negatives)
2. Allow any integer values (CHOSEN)

**Rationale:**
- **Separation of concerns**: Coordinate represents a mathematical point in 2D space, not a business concept
- **Flexibility**: Allows intermediate calculations without exceptions
  - Example: Position.moveForward() may calculate (-1, 0) before Workspace validates
- **Single Responsibility**: Validation of "within workspace" is Workspace's responsibility, not Coordinate's
- **Reusability**: Coordinate can be used in other contexts where negatives are valid

**Consequences:**
- Coordinate can represent points outside workspace (e.g., -1, -1)
- Workspace.isWithinBounds() is responsible for business rule validation
- Position.moveForward() can create coordinates with negative values without throwing exceptions
- Robot.move() validates using Workspace before accepting new position
- More flexible design, easier to extend

**Example:**
```java
// This is valid (mathematical primitive):
Coordinate coord = new Coordinate(-1, -1);  // ✅ No exception

// Business rule validation happens at Workspace level:
Workspace workspace = new Workspace(new Coordinate(5, 5));
workspace.isWithinBounds(coord);  // returns false

// Robot at (0, 0) facing West:
Position position = new Position(new Coordinate(0, 0), Orientation.W);
Position next = position.moveForward();  // Creates Coordinate(-1, 0) ✅
// Robot.move() then validates with Workspace and rejects the movement
```

**Trade-offs:**
- Pro: More flexible, follows mathematical semantics
- Pro: Simpler Coordinate class (no validation logic)
- Pro: Validation centralized in Workspace
- Con: Possible to create "invalid" coordinates (mitigated by Workspace validation)
