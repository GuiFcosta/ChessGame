# ♟️ Chess - Advanced Programming Project

**A full-featured Chess Game developed in Java (JavaFX), focusing on robust Object-Oriented Design and Design Patterns.**

This project was developed as the final assignment for the **Advanced Programming (Programação Avançada)** course at the **Politécnico de Coimbra - Licenciatura em Engenharia Informática**. The goal was to build a modular, extensible application that strictly adheres to software engineering principles.

---

## Key Features

The application implements a complete chess engine with the following capabilities:

* **Complete Game Rules:** Fully implements movement validation, **Check**, **Checkmate**, and **Draw** detection.
* **Special Moves:** Supports complex moves such as **Castling**, **Pawn Promotion**, and **En Passant** capture.
* **Game Management:**
    * **Save & Load:** Persist game states using binary serialization.
    * **Import/Export:** Share game states via text-based import/export.
* **Learning Mode:** Includes an **Undo/Redo** system and valid move highlighting to assist learning.
* **Audio System:** Interactive sound effects and a **Narrator** (Portuguese and English) that describes moves sequentially.
* **Event Logging:** A real-time log system that records all game actions.

---

## Architecture & Design Patterns

The project follows a strict **MVC (Model-View-Controller)** architecture to ensure separation of concerns, alongside several GoF Design Patterns.

### Architectural Pattern: MVC
* **Model:** Encapsulates the game logic (`ChessGame`, `Board`, `Piece`). It knows nothing about the UI.
* **View:** The JavaFX interface (`ChessBoardCanvas`, `RootPane`). It listens for updates from the Manager.
* **Controller/Facade:** The `ChessGameManager` orchestrates the flow, managing the model and notifying the view.

### Design Patterns Implemented

| Pattern | Component | Usage Description |
| :--- | :--- | :--- |
| **Facade** | `ChessGameManager` | Acts as the single entry point for the UI to interact with the complex game logic, reducing coupling. |
| **Observer** | `PropertyChangeListener` | Enables the UI and Audio system to react automatically to game events (e.g., "boardState", "gameOver") without polling. |
| **Singleton** | `ModelLog`, `ImageManager` | Ensures a single global instance for the event logger and resource managers (images/sounds). |
| **Factory Method** | `PieceFactory` | Centralizes the logic for creating different chess pieces based on type and color. |
| **Template Method** | `Piece` (Abstract Class) | Defines the skeleton algorithm for piece movement, allowing subclasses (`Pawn`, `Rook`) to implement specific behaviors. |

---

## Class Structure Overview

* **`ChessGameManager`**: The core Facade that holds the `ChessGame` instance and manages `PropertyChangeSupport`.
* **`Board`**: Represents the 8x8 grid and holds `Piece` objects.
* **`Piece` (Abstract)**: The base class for all pieces (`King`, `Queen`, etc.).
* **`ChessBoardCanvas`**: The custom JavaFX component responsible for rendering the board and handling clicks.
* **`ModelLog`**: A Singleton that stores the history of match events.

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
    *(Note: Depending on your package structure, the main class might be named `ChessMain` ).*

---

## 👥 Authors

**Group 40 - 2024/25** 

* **Nuno André Teles Grilo Gonçalves** (2010016172)
* **Guilherme Farias Costa** (2022144234)
* **Duarte Machado Gois** (2022136610)

---

*Disclaimer: This project was created for educational purposes within the scope of the Computer Engineering degree at ISEC - Politécnico de Coimbra.*
