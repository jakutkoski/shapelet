TODO:

- generate all puzzles
- UI improvements
- optimizations - hashmap or binary search for dictionary, line drawing cache, etc.
- abstractions (maybe) - ViewModel, data layer, dependency injection, etc.
- insight system: reveal first word after 8 insight, then second word after 16 insight
- alternate names: wisdom, knowledge, expertise, brilliance
- menu: tab for filterable list of all puzzles, tab for "settings" and stats

board encoding:

```
type|letters|working,words
```

example of board encoding:

```
box3|ebfnaitucvld|abdicate,eventful
```

This encoding would create:
- A square with 3 letters per side.
- Top side = ebf. Left side = nai. Right side = tuc. Bottom side = vld.
- This puzzle can be solved with words abdicate - eventful.
