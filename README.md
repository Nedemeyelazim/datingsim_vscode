# Dating Sim - Visual Novel Engine
An interactive story set in China's Three Kingdoms period

## 📋 Project Information
- **School**: Werner von Siemens Schule Wetzlar
- **Class**: 12BG
- **Course**: Computer Science (Prin-E GK)
- **Team Members**:
  - Hasan Yavuz
  - Leonardo Dias Pompl
  - Danila Schneider

## 🎮 Features
- Multi-scene management with smooth transitions
- Advanced dialogue system with text progression
- Persistent fullscreen mode
- Responsive layout system
- Comprehensive debug system

## 🛠 Technical Details
- JavaFX UI Framework
- FXML-based scene layouts
- CSS styling for consistent design
- Event-based user interaction
- Thread-safe design

## 📦 Project Structure
```
datingsim/
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── App.java                    # Main application
│   │   │   ├── SceneTransition.java        # Transition effects
│   │   │   ├── dialogue/
│   │   │   │   └── DialogueManager.java    # Dialogue system
│   │   │   └── controllers/
│   │   │       ├── FirstSceneController.java
│   │   │       └── ...
│   │   └── resources/
│   │       ├── com/example/
│   │       │   ├── dialogue.txt            # Dialogue texts
│   │       │   ├── menu.css               # Styling
│   │       │   └── Images/                # Graphics
│   │       └── FXML/
│   │           ├── menu.fxml
│   │           └── ...
│   └── test/                              # Unit tests
└── pom.xml                                # Maven configuration
```

## 🚀 Installation & Launch

### Prerequisites
- Java JDK 24 or higher
- Maven 3.9+ 
- JavaFX SDK 24

### Setup
1. Clone repository:
```bash
git clone https://github.com/yourusername/datingsim.git
cd datingsim
```

2. Install dependencies:
```bash
mvn install
```

3. Start application:
```bash
mvn javafx:run
```

## 🎯 Game Controls
- **Space**: Progress dialogue
- **ESC**: Not available (disabled for fullscreen)
- **Back Button**: Return to previous scene

## 🔧 Development

### Build
```bash
mvn clean package
```

### Run Tests
```bash
mvn test
```

### Enable Debug Mode
Add the following VM options:
```
-Ddebug=true
```

## 📝 Dialogue System

### Format
Dialogues are defined in `dialogue.txt`:
```
[SceneName]
Dialog line 1
Dialog line 2
[End]
```

### Integration
New dialogues can be added by editing `dialogue.txt`

## 🎨 UI Customization
- Define styles in `menu.css`
- FXML layouts for each scene in respective `.fxml` files
- Store images in `Images/` directory

## 🏫 Academic Context
This project was developed as part of the Computer Science course at Werner von Siemens Schule Wetzlar. It demonstrates practical application of:
- Object-Oriented Programming
- UI/UX Design
- Event-Driven Programming
- Version Control
- Team Collaboration

## 📄 License
This project is licensed under the MIT License - see [LICENSE.md](LICENSE.md)

## ✨ Acknowledgments
- JavaFX Community
- Scene Builder Team
- All contributors and testers
- Our Computer Science teacher for guidance and support

## 📫 Contact
### Development Team
- **Hasan Yavuz**
- **Leonardo Dias Pompl**
- **Danila Schneider**

### School
Werner von Siemens Schule Wetzlar  
Computer Science Department  
12BG Class, 2025