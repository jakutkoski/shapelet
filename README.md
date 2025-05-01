TODO:

- better highlighting of current word being worked on
- make Toast look better
- generate all puzzles
- UI improvements
- customizable letter highlighting in buttons - vowels by default
- optimizations - hashmap or binary search for dictionary, line drawing cache, etc.
- abstractions (maybe) - ViewModel, data layer, dependency injection, etc.
- Room DB?
- wisdom system: gain X wisdom when solved in Y words (Y:X | 5:1, 4:3, 3:9, 2:27, 1:81)
- wisdom system: reveal first special word after gaining 27 wisdom, then second special word after 54 wisdom
- menu: tab for filterable list of all puzzles, tab for "settings" and stats
- puzzle curation

board encoding:

```
type|letters|special,words
```

example of board encoding:

```
box3|ebfnaitucvld|abdicate,eventful
```

This encoding would create:
- A square with 3 letters per side.
- Top side = `ebf`. Left side = `nai`. Right side = `tuc`. Bottom side = `vld`.
- This puzzle can be solved with special words `abdicate` then `eventful`.
