TODO:

- puzzle generation and solving
- explore some UI improvements
- explore optimizations - hashmap dictionary or binary search, line drawing cache, etc.
- abstractions - ViewModel, data layer, dependency injection
- explore more UI improvements
- create point system and account memory
- implement hints
- create menu, settings, etc.

board encoding:

```
type|letters|usageBonuses|usageLimits|largeWord
```

example of board encoding:

```
box3|jxzrcaueloin|101000000000|000300000003|journalize
```

This encoding would create:
- A square with 3 letters per side.
- Top side = jxz. Left side = rca. Right side = uel. Bottom side = oin.
- Letters j and z would grant bonus points every time they are used.
- Letters r and n would be limited to 3 uses.
- The valid large word in mind by the puzzle-maker is journalize.
- First hint = the fist letter of journalize.
- Second hint = the last letter of journalize.
