# Technical challenge - Graphical Editor Simulator (ges)

Graphical editors allow users to edit images in the same way text editors let us modify documents. Images are represented as an M x N array of pixels with each pixel given colour.

Produce a program that simulates a simple interactive graphical editor.

**Input:**
The input consists of a line containing a sequence of commands. Each command is represented by a single capital letter at the start of the line. Arguments to the command are separated by spaces and follow the command character.

Pixel co-ordinates are represented by a pair of integers: 1) a column number between 1 and M, and 2) a row number between 1 and N. Where 1 <= M, N <= 250. The origin sits in the upper-left of the table. Colours are specified by capital letters.

**Commands**
The editor supports 8 commands:
1. I M N​. Create a new M x N image with all pixels coloured white (O).
2. C​. Clears the table, setting all pixels to white (O).
3. L X Y C​. Colours the pixel (X,Y) with colour C.
4. V X Y1 Y2 C​. Draw a vertical segment of colour C in column X between rows Y1 and Y2 (inclusive).
5. H X1 X2 Y C​. Draw a horizontal segment of colour C in row Y between columns X1 and X2 (inclusive).
6. F X Y C​. Fill the region R with the colour C. R is defined as: Pixel (X,Y) belongs to R. Any other pixel which is the same colour as (X,Y) and shares a common side with any pixel in R also belongs to this region.
7. S​. Show the contents of the current image
8. X​. Terminate the session

**Example**
In the example below, > denotes input, => denotes program output.

```
> I 5 6
> L 2 3 A
> S
=>
OOOOO
OOOOO
OAOOO
OOOOO
OOOOO
OOOOO
```

```
> F 3 3 J
> V 2 3 4 W
> H 3 4 2 Z
> S
=>
JJJJJ
JJZZJ
JWJJJ
JWJJJ
JJJJJ
JJJJJ
```

## Architecture and assumptions

- `editor.clj` has independent functions to transform image. If function return `nil` it means user input not valid data. For example outside scope of image.
- `core.clj` is command line interface to use `editor.clj`. `(start-session)` run independent session for image editing. Simulation to open many images in editor.
- Tests check intentions of the user (functional tests). I decided to not check each function (unit tests). There is no need to test compiler.

## Next steps

I decided to not spent extra resources to do improvements below at that moment. Performance is not an issue for purpose of this application. There is no need to write help, because target users will know commands and how to use application. Application wouldn't be run on production, so there is no need to implement logs and catch exceptions. However it is worth to mention about it.

- There is no output information for user about not valid parameters. For example `I 0 2` will generate corrupted image. Application is resistant, but doesn't communicate to user. It could by solve by `clojure.spec` with custom readable errors.
- There is no validation for colour string. For example user can use "foobar" instead of one character string like "O".
- Add logs, metrics and catch exceptions
- Optimise performance bucket fill function. Here are more optimised algorithm to use https://en.wikipedia.org/wiki/Flood_fill
- Instruction how to use application when run application and after `help` command.
- After not valid command show suggestion to use `help`.

## How to run application

### REPL

```bash
lein repl
```

```clojure
(use 'ges.core)
(start-session)
```

### JAR
```bash
java -cp target/ges-0.1.0-SNAPSHOT-standalone.jar clojure.main -m ges.core
```
