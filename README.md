TODO:

- generate all puzzles
- UI improvements
- optimizations - hashmap or binary search for dictionary, line drawing cache, etc.
- abstractions (maybe) - ViewModel, data layer, dependency injection, etc.
- account memory for best solve of each puzzle
- rating system (5 through 1 ribbons, letter count as well)
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

### Word Lists

There are 3 source dictionaries that were used to create 2 word lists used in the game.

The 3 source dictionaries:
1. https://github.com/dwyl/english-words/blob/master/words_alpha.txt
2. https://github.com/dolph/dictionary/blob/master/popular.txt
3. https://github.com/coffee-and-fun/google-profanity-words/blob/main/data/en.txt

The 2 word lists:
1. All Words (all English words - used for validating player entries)
2. Seed Words (popular English words - used to generate puzzles)

The All Words list was created by downloading Source 1 (full English dictionary),
filtering out 1-2 letter words,
and filtering out profane words as listed from Source 3.

The Seed Words list was created by downloading Source 2 (popular English words),
filtering out 1-2 letter words,
and filtering out profane words as listed from Source 3.
