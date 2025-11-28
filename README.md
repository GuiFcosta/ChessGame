# ♟️ PA Chess - Advanced Programming Project

**A full-featured Chess Game developed in Java (JavaFX), focusing on robust Object-Oriented Design and Design Patterns.**

[cite_start]This project was developed as the final assignment for the **Advanced Programming (Programação Avançada)** course at the **Politécnico de Coimbra - Licenciatura em Engenharia Informática**[cite: 2, 3, 4]. [cite_start]The goal was to build a modular, extensible application that strictly adheres to software engineering principles[cite: 14].

---

## Key Features

The application implements a complete chess engine with the following capabilities:

* [cite_start]**Complete Game Rules:** Fully implements movement validation, **Check**, **Checkmate**, and **Draw** detection[cite: 16].
* [cite_start]**Special Moves:** Supports complex moves such as **Castling**, **Pawn Promotion**, and **En Passant** capture[cite: 16].
* **Game Management:**
    * [cite_start]**Save & Load:** Persist game states using binary serialization[cite: 16].
    * [cite_start]**Import/Export:** Share game states via text-based import/export[cite: 16, 67].
* [cite_start]**Learning Mode:** Includes an **Undo/Redo** system and valid move highlighting to assist learning[cite: 63, 64].
* [cite_start]**Audio System:** Interactive sound effects and a **Narrator** (Portuguese and English) that describes moves sequentially[cite: 65, 66].
* [cite_start]**Event Logging:** A real-time log system that records all game actions[cite: 17, 53].

---

## Architecture & Design Patterns

[cite_start]The project follows a strict **MVC (Model-View-Controller)** architecture to ensure separation of concerns, alongside several GoF Design Patterns[cite: 18, 26].

### Architectural Pattern: MVC
* **Model:** Encapsulates the game logic (`ChessGame`, `Board`, `Piece`). [cite_start]It knows nothing about the UI[cite: 33].
* **View:** The JavaFX interface (`ChessBoardCanvas`, `RootPane`). [cite_start]It listens for updates from the Manager[cite: 35].
* [cite_start]**Controller/Facade:** The `ChessGameManager` orchestrates the flow, managing the model and notifying the view[cite: 38].

### Design Patterns Implemented

| Pattern | Component | Usage Description |
| :--- | :--- | :--- |
| **Facade** | `ChessGameManager` | [cite_start]Acts as the single entry point for the UI to interact with the complex game logic, reducing coupling[cite: 42]. |
| **Observer** | `PropertyChangeListener` | [cite_start]Enables the UI and Audio system to react automatically to game events (e.g., "boardState", "gameOver") without polling[cite: 46]. |
| **Singleton** | `ModelLog`, `ImageManager` | [cite_start]Ensures a single global instance for the event logger and resource managers (images/sounds)[cite: 52, 128]. |
| **Factory Method** | `PieceFactory` | [cite_start]Centralizes the logic for creating different chess pieces based on type and color[cite: 151]. |
| **Template Method** | `Piece` (Abstract Class) | [cite_start]Defines the skeleton algorithm for piece movement, allowing subclasses (`Pawn`, `Rook`) to implement specific behaviors[cite: 176]. |

---

## Class Structure Overview

* [cite_start]**`ChessGameManager`**: The core Facade that holds the `ChessGame` instance and manages `PropertyChangeSupport`[cite: 221].
* [cite_start]**`Board`**: Represents the 8x8 grid and holds `Piece` objects[cite: 221].
* [cite_start]**`Piece` (Abstract)**: The base class for all pieces (`King`, `Queen`, etc.)[cite: 221].
* [cite_start]**`ChessBoardCanvas`**: The custom JavaFX component responsible for rendering the board and handling clicks[cite: 223].
* [cite_start]**`ModelLog`**: A Singleton that stores the history of match events[cite: 221].

---

## How to Run

### Prerequisites
* **Java JDK 8+** (JavaFX support required).
* An IDE (IntelliJ, Eclipse) or Maven/Gradle build tool.

### Execution
1.  Clone the repository.
2.  Navigate to the source folder.
3.  Run the main class:
    ```java
    // Main entry point
    src/pt/isec/pa/chess/ui/gui/MainJFX.java
    ```
    [cite_start]*(Note: Depending on your package structure, the main class might be named `ChessMain` [cite: 224]).*

---

## 👥 Authors

[cite_start]**Group 40 - 2024/25** [cite: 6, 9]

* **Nuno André Teles Grilo Gonçalves** (2010016172)
* **Guilherme Farias Costa** (2022144234)
* **Duarte Machado Gois** (2022136610)

---

*Disclaimer: This project was created for educational purposes within the scope of the Computer Engineering degree at ISEC - Politécnico de Coimbra.*
