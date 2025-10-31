# AI Story Generator — Choose Your Own Adventure  

## Overview
The AI Story Generator turns narrative storytelling into a personalized adventure. Build a character, shape a world, choose your genre, and watch your story adapt dynamically to the decisions you make. Whether it becomes a whimsical fantasy quest or a suspenseful mystery thriller, your path is never the same twice. When your journey is complete, save and favorite your story, organize it in your personal library, and export it to share with others.

## Example Story Flow

| Step | Action |
|------|--------|
| Genre Selection | **Fantasy** → Start |
| Character | Elowen — Brave, Curious — Exiled princess |
| World | Whispering Forest — Magic forbidden — War-torn history |
| Story Start | Narrative text is generated |
| Player Choice | **A)** Follow trail • **B)** Call out |

> Each choice redirects the story into a new scene.


## Features
- Build your own hero and world
- Pick a genre to set the mood (Fantasy, Sci-Fi, Mystery, Romance, Horror)
- Shape the story through impactful decisions
- Customize how long, deep, or descriptive the story should feel
- Save your adventures to a personal Story Library
- Export stories to share or keep forever
- Optional: Let AI generate matching illustrations



## Design Patterns

| Pattern | Role in Project |
|--------|-----------------|
| **MVC** | Separates UI, logic, and data for maintainability |
| **Strategy Pattern** | Enables multiple genre writing styles without changing core logic |
| **Factory Pattern** | Chooses the correct genre strategy at runtime |
| **Singleton** | Ensures only one shared OpenAI client instance |



## Team Members

| Name | Role | Responsibilities |
|------|------|----------------|
| **Rebecca Smith** | Model & Story System Engineer | Story data structures, world/character models, genre strategy logic |
| **Viet Nguyen** | UI/UX Developer | Panels, layout flow, rendering of scenes and choices |
| **Tanya Patel** | Controller & AI Integration | OpenAI prompt building, save/load system, exporting |


## Proposed File Structure (WIP)
```
src/main/java/
├── Main.java
│
├── model/ // Story & Model Engineer
│ ├── story/
│ │ ├── Scene.java
│ │ ├── Choice.java
│ │ ├── StoryState.java
│ │ ├── StoryModel.java
│ │ ├── Character.java
│ │ └── World.java
│ │
│ ├── strategy/
│ │ ├── AIStrategy.java
│ │ ├── FantasyStrategy.java
│ │ ├── SciFiStrategy.java
│ │ ├── MysteryStrategy.java
│ │ ├── RomanceStrategy.java
│ │ ├── HorrorStrategy.java
│ │ └── StrategyFactory.java
│ │
│ └── library/
│ ├── StoryRecord.java
│ └── LibraryModel.java
│
├── controller/ // Controller & AI Engineer
│ ├── IGameController.java
│ ├── GameController.java
│ ├── CharacterController.java
│ ├── WorldController.java
│ └── LibraryController.java
│
├── view/ // UI/UX Engineer
│ ├── MainFrame.java
│ ├── GenrePanel.java
│ ├── CharacterPanel.java
│ ├── WorldPanel.java
│ ├── ControlsPanel.java
│ ├── StoryPanel.java
│ ├── ChoicePanel.java
│ ├── LibraryPanel.java
│ └── components/
│ ├── LoadingIndicator.java
│ └── ErrorDialog.java
│
└── service/ // Controller & AI Engineer
  ├── OpenAIClient.java // Singleton
  ├── OpenAIService.java
  ├── PromptBuilder.java
  ├── LibraryStorage.java
  ├── ExportService.java
  └── ImageGenService.java // OPTIONAL

```

## Next Steps
- Integrate OpenAI text generation for dynamic storytelling
- Add progress indicator UI


