TODO NEXT:

- app architecture with Navigation and Room DB
- menu: tab for filterable list of all puzzles, tab for "settings" and stats

TODO LATER:

- replace mirror with hat? row of 6 on top, two side columns with 3 each
- insight system: gain X insight when solved in Y words (Y:X | 5:1, 4:3, 3:9, 2:27, 1:81)
- insight system: reveal first special word after gaining 20 insight, then second special word after 50 insight
- medal system: bronze medal after 20 insight, silver medal after 50 insight, gold medal after 100 insight
- italicize solution words already submitted
- verify generated puzzles can be solved in multiple ways, up to 100 insight
- code to analyze how difficult a puzzle is based on amount of vowels, high-frequency letters, etc.
- UI improvements
- optimizations - hashmap or binary search for dictionary, line drawing cache, etc.
- abstractions (maybe) - ViewModel, data layer, dependency injection, etc.
- puzzle curation

board encoding:

```
type|letters|special,words
```

example of board encoding:

```
box|ebfnaitucvld|abdicate,eventful
```

This encoding would create:
- A square (box) with 3 letters per side.
- Top side = `ebf`. Left side = `nai`. Right side = `tuc`. Bottom side = `vld`.
- This puzzle can be solved with special words `abdicate` then `eventful`.
