### State of this project

I don't want to work on this anymore.
Here is a list of things I would work on if I was still working on it:

- set up app architecture with Navigation and Room DB
- use the Room DB to store puzzles and player solutions
- create a list page to list out all the puzzles (right now the game just picks a random one upon opening the app)
- create a settings page
- determine other puzzle types
- many UI improvements
- performance profiling and enhancements
- more effort into puzzle curation
- code to analyze how difficult a puzzle is based on vowel placement, high-frequency letters (e.g. R and S), etc.

### Basic Information

board encoding:

```
type|letters|key,words
```

example of board encoding:

```
box|ebfnaitucvld|abdicate,eventful
```

This encoding would create:
- A square (box) with 3 letters per side.
- Top side = `ebf`. Left side = `nai`. Right side = `tuc`. Bottom side = `vld`.
- This puzzle can be solved with key words `abdicate` then `eventful`.

### Screenshots

<p float="left">
  <img src="/screenshots/screenshot1.png" width=30% height=30% />
  <img src="/screenshots/screenshot2.png" width=30% height=30% /> 
</p>
