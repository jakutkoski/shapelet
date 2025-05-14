TODO NEXT:

- app architecture with Navigation and Room DB (do this in a new git branch)
- menu: tab for filterable list of all puzzles, tab for settings/stats

TODO LATER:

- determine third puzzle type
- star system: earn 1-5 stars depending on puzzle solutions
- (solving in fewer words should be rewarded, but also solving it in many different ways should be rewarded)
- reveal system: reveal the original "special words" solution after earning certain amount of stars
- italicize solution sequences that were already submitted
- verify generated puzzles can be solved in multiple ways
- code to analyze how difficult a puzzle is based on amount of vowels, high-frequency letters, etc.
- TBD UI improvements
- add music elements? hit in-key notes when letters are tapped? adjust music based on submitted words?
- change color of activation lines as more words are submitted (green to yellow to red, where red indicates beyond 5 words)
- optimizations - hashmap or binary search for dictionary, line drawing cache, etc.
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
