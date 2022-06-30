![](Pasted%20image%2020220630111420.png)
## Team GC23

- 10686518 Martino Piaggi martino.piaggi@mail.polimi.it
- 10610426 Amrit Singh amrit.singh@mail.polimi.it
- 10676180 Lorenzo Perini  lorenzo1.perini@mail.polimi.it

## Implemented Functionalities

- Complete rules
- CLI
- GUI
- Rmi
- [AF] All 12 character cards
- [AF] 4 players game
- [AF] Multiple games

## Test cases

**Coverage criteria: code lines.**
  
| Package | Coverage |
|:-----------------------|:------------------:|
| Controller | 92.5% (37/40) |
|model| 89.8% (686/764)|
|model.cards| 100% (3/3)|
| model.cards.assistantcard| 100% (3/3)|
|model.cards.charactercard| 81.6% (142/174)|
|model.deck| 83.3% (10/12)|
|model.deck.assistantcard| 89.5% (17/19)|
|model.deck.characterdeck| 100% (11/11)|
|model.enumerations|100% (42/42)|
|model.tiles|100% (37/37)|

## Running the game

| Command |Role |
|:-----------------------|:------------------|
| ``java -jar Eriantys.jar -server`` | Server listening on port 23023  |
| ``java -jar Eriantys.jar -cli [ip]`` | CLI interface |
| ``java -jar Eriantys.jar -gui [ip]`` | GUI interface |