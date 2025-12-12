# AI Story Generator — Choose Your Own Adventure

## Setup
1. Get API key from [OpenAI](https://platform.openai.com/api-keys)
2. Set environment variable: `export OPENAI_API_KEY="your-key-here"`
3. Or add to `src/main/resources/config.properties`: `OPENAI_API_KEY=your-key-here`
4. Run `Main.java` to launch the application

## Features
- [x] Interactive character creation (name, traits, backstory)
- [x] Dynamic world building (location, rules, history)
- [x] Genre-adaptive storytelling (Fantasy, Sci-Fi, Mystery, Romance, Horror)
- [x] AI-powered chapter generation with branching A/B/C choices
- [x] 10-chapter structured narrative with final ending
- [x] Save/load system using JSON files stored in /saves
- [x] Consistent UI with Swing panels, vertical choice buttons, loading overlay
- [x] Robust error handling with friendly rate-limit messages
- [x] Configurable story length, complexity, and style
- [x] Async AI calls using SwingWorker (non-blocking UI)
- [x] Automatic scene illustration generation (with safe placeholder fallback)
- [x] Chapter-based image caching to reduce API usage and avoid rate limits

## Design Patterns
- **MVC Architecture**  
  Clear separation between UI panels (view), story and image models (model), and application flow logic (controller).

- **Singleton Pattern**  
  OpenAIClient provides a single shared HTTP client responsible for authentication, retries, and timeouts, ensuring consistent API usage across the application.

- **Builder Pattern**  
  PromptBuilder constructs complex story prompts from character details, world data, genre rules, prior choices, and active story mode without cluttering the controller.

- **Strategy Pattern**  
  The StoryModeStrategy interface allows the application to dynamically switch storytelling behavior at runtime.  
  AdultMode and ChildFriendlyMode apply different tone, vocabulary, and content rules based on user-selected complexity without modifying core logic.

- **Factory Pattern**  
  Image prompt creation is handled using the Factory pattern.  
  ImagePromptFactoryProvider selects the appropriate ImagePromptFactory implementation (e.g., SceneMomentPromptFactory or CoverArtPromptFactory) based on the requested image type.  
  This cleanly separates image prompt logic from the controller and allows new image types to be added without changing existing code.

- **Observer-Style UI Updates**  
  The controller updates the UI when new scenes or images are generated, allowing views to react without directly managing application state.


## Main Architecture
```
src/main/java/
├── controller/
│   └── MainController.java                  # Core application logic, async story + image generation
│
├── model/
│   ├── story/                               # Story domain models
│   │   ├── CharacterModel.java
│   │   ├── ChoiceModel.java
│   │   ├── ChoiceRecordModel.java
│   │   ├── SavedStoryModel.java
│   │   ├── SceneModel.java
│   │   ├── StoryModel.java
│   │   ├── StoryStateModel.java
│   │   └── WorldModel.java
│   │
│   ├── strategy/                            # Strategy Pattern
│   │   ├── StoryModeStrategy.java
│   │   ├── AdultMode.java
│   │   └── ChildFriendlyMode.java
│   │
│   ├── image/                               # Factory Pattern (Image Prompts)
│   │   ├── ImagePromptFactory.java
│   │   ├── ImagePromptFactoryProvider.java
│   │   ├── ImagePromptContext.java
│   │   ├── ImageRequestType.java
│   │   ├── SceneMomentPromptFactory.java
│   │   └── CoverArtPromptFactory.java
│   │
│   └── OpenAIClient.java                    # Singleton HTTP client
│
├── service/
│   ├── OpenAIService.java                   # Story generation via OpenAI
│   ├── PromptBuilder.java                   # Builds story prompts
│   ├── ImageGenerationService.java          # Image prompt generation + placeholder fallback
│   ├── StoryLibrary.java
│   └── StorySaveSystem.java
│
├── view/
│   ├── components/
│   │   ├── ErrorDialog.java
│   │   └── LoadingIndicator.java
│   │
│   ├── panels/
│   │   ├── CharacterPanel.java
│   │   ├── ChoicePanel.java
│   │   ├── ControlsPanel.java
│   │   ├── GenrePanel.java
│   │   ├── ImagePanel.java                  # Left-side illustration panel
│   │   ├── LibraryPanel.java
│   │   ├── StoryPanel.java                  # Story text + image layout
│   │   └── WorldPanel.java
│   │
│   └── MainFrame.java
│
└── Main.java                             # Application entry point
```
## JUnit Testing

The JUnit testing structure mirrors the main project layout and validates the most important parts of the application.

- Model tests verify that characters, scenes, story state, and chapter progression behave correctly.
- Controller tests use a FakeMainFrame to confirm story flow, choice handling, and error scenarios without launching Swing UI.
- Service tests validate save/load behavior and prompt construction without calling real APIs.

## JUnit Testing Architecture

```
src/test/java/
├── controller/
│   ├── FakeMainFrame.java               # UI test double (no real Swing UI)
│   └── MainControllerTest.java          # Tests controller flow, choices, strategy switching, and error handling
│
├── model/
│   ├── image/
│   │   └── ImagePromptFactoryTest.java  # Tests Factory Pattern for image prompt creation
│   │
│   ├── story/
│   │   ├── CharacterModelTest.java
│   │   ├── ChoiceRecordModelTest.java
│   │   ├── SavedStoryModelTest.java
│   │   ├── SceneModelTest.java
│   │   ├── StoryModelTest.java
│   │   ├── StoryStateModelTest.java
│   │   └── WorldModelTest.java
│   │
│   └── OpenAIClientTest.java             # Tests config loading, JSON escaping, retry logic (no real API calls)
│
└── service/
    └── StorySaveSystemTest.java          # Tests save/load serialization and file handling
```

## Demo
[[Video demonstration](https://www.youtube.com/watch?v=u6xPhQpRZ9Y)]


