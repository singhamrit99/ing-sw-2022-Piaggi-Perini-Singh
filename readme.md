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
  
| Package |Tested Class | Coverage |
|:-----------------------|:------------------|:------------------------------------:|
| Model | Global Package | 89.5% 958/1080 |
| Controller | Controller | 93.8% (30/32) |

## Running the game

| Command |Role |
|:-----------------------|:------------------|
| ``java -jar Eryantis.jar -server`` | Server listening on port 23023  |
| ``java -jar Eryantis.jar -cli [ip]`` | CLI interface |
| ``java -jar Eryantis.jar -gui [ip]`` | GUI interface |