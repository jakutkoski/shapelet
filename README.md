TODO:

- UI improvements
- optimizations - hashmap or binary search for dictionary, line drawing cache, etc.
- abstractions - ViewModel, data layer, dependency injection, etc.
- account memory for best solve of each puzzle
- menu, settings, etc.

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
