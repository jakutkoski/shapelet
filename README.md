TODO:

- decide how to do hints
- implement rest of board types
- explore some UI improvements
- explore optimizations - hashmap dictionary or binary search, line drawing cache, etc.
- abstractions - ViewModel, data layer, dependency injection
- explore more UI improvements
- create point system and account memory
- create menu, settings, etc.
- create build your own puzzle?

board types:
- box2
- box3
- box4
- cup3
- cup4
- mirror4
- lane3
- lane4

board encoding:

```
type|letters|usageBonuses|usageLimits|largeWord|largeWordHint
```

example of board encoding:

```
box3|jxzrcaueloin|101000000000|000300000003|journalize|documenting events
```

This encoding would create:
- A square with 3 letters per side.
- Top side = jxz. Left side = rca. Right side = uel. Bottom side = oin.
- Letters j and z would grant bonus points every time they are used.
- Letters r and n would be limited to 3 uses.
- The valid large word in mind by the puzzle-maker is journalize.
- The hint for the large word is "documenting events".
- After earning the first hint, a second hint can be earned that reveals the first letter of journalize.
- After earning the second hint, a third hint can be earned that reveals the second letter of journalize.
