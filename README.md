TODO:

- UI improvements
- optimizations - hashmap or binary search for dictionary, line drawing cache, etc.
- abstractions - ViewModel, data layer, dependency injection, etc.
- point system and account memory
- special word hints
- menu, settings, etc.

board encoding:

```
type|letters|usageBonuses|usageLimits|specialWord1|specialWord2
```

example of board encoding:

```
box3|ebfnaitucvld|000000000100|000020000000|abdicate|eventful
```

This encoding would create:
- A square with 3 letters per side.
- Top side = ebf. Left side = nai. Right side = tuc. Bottom side = vld.
- Letter v would grant bonus points every time it is used.
- Letter a would be limited to 2 uses.
- This puzzle can be solved with word sequence abdicate - eventful.
