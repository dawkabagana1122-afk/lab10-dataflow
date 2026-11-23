# AI-ийн гаргасан bonus() DU хүснэгт (нэмэлт даалгавар)

AI: Claude (Claude Code-ийн тусдаа агент). Бидний README, тестийг харуулалгүй зөвхөн `DataFlowTargets.java`-ийн 31–57-р мөрийг (мөрийн дугаартай) өгсөн.

Prompt: "Produce the complete table of all definition-use (DU) pairs for this Java method ... for p-use list each outgoing edge T/F separately ... Treat the for-each at line 38 as: predicate = attendance has a next element (p-use of attendance), step = def of a. Then list (1) def-use combinations that are NOT DU pairs because every path is killed, and (2) infeasible DU pairs, with reasons."

Доорх нь AI-ийн хариу. Хүснэгтийн агуулгыг өөрчлөлгүй хуулсан.

## Modelling

- Line 31 defines both parameters, `attendance` and `threshold`.
- Line 32 is a single decision. True goes to 33, false goes to 35. Both `attendance` and `threshold` are p-used there.
- Line 38: the predicate "attendance has a next element" is a p-use of `attendance` (true goes to 39, false goes to 49); the step assigns the next element to `a` (def of `a`).
- The other predicate edges are 39 (true to 40, false to 46), 42 (true to 43, false back to 38), 50 (true to 51, false to 53) and 53 (true to 54, false to 56).
- Lines 40 and 41 each use the variable and then redefine it on the same line.

## DU pairs

| # | Variable | Def line | Use line | c-use / p-use | Example def-clear path (input) |
|---|---|---|---|---|---|
| 1 | attendance | 31 | (32,T) | p-use | 31,32,33 (attendance=null) |
| 2 | attendance | 31 | (32,F) | p-use | 31,32,35 (att=[], thr=0) |
| 3 | attendance | 31 | (38,T) | p-use | 31,32,35,36,37,38,39 (att=[1]) |
| 4 | attendance | 31 | (38,F) | p-use | 31,32,35,36,37,38,49 (att=[]) |
| 5 | threshold | 31 | (32,T) | p-use | 31,32,33 (att=[], thr=-1) |
| 6 | threshold | 31 | (32,F) | p-use | 31,32,35 (att=[], thr=0) |
| 7 | threshold | 31 | (50,T) | p-use | 31,32,35,36,37,38,49,50,51 (att=[], thr=0) |
| 8 | threshold | 31 | (50,F) | p-use | 31,32,35,36,37,38,49,50,53 (att=[], thr=1) |
| 9 | present | 35 | 40 | c-use | 35,36,37,38,39,40 (att=[1]) |
| 10 | present | 35 | (50,T) | p-use | 35,36,37,38,49,50,51 (att=[], thr=0) |
| 11 | present | 35 | (50,F) | p-use | 35,36,37,38,49,50,53 (att=[0], thr=1) |
| 12 | present | 40 | 40 | c-use | 40,41,42,43,38,39,40 (att=[1,1]) |
| 13 | present | 40 | (50,T) | p-use | 40,41,42,43,38,49,50,51 (att=[1], thr=1) |
| 14 | present | 40 | (50,F) | p-use | 40,41,42,43,38,49,50,53 (att=[1], thr=2) |
| 15 | streak | 36 | 41 | c-use | 36,37,38,39,40,41 (att=[1]) |
| 16 | streak | 41 | 41 | c-use | 41,42,43,38,39,40,41 (att=[1,1]) |
| 17 | streak | 41 | (42,T) | p-use | 41,42,43 (att=[1]) |
| 18 | streak | 41 | (42,F) | p-use | 41,42,38 (att=[1,0,1], 3rd iteration: streak=1, best=1) |
| 19 | streak | 41 | 43 | c-use | 41,42,43 (att=[1]) |
| 20 | streak | 46 | 41 | c-use | 46,38,39,40,41 (att=[0,1]) |
| 21 | best | 37 | (42,T) | p-use | 37,38,39,40,41,42,43 (att=[1]) |
| 22 | best | 37 | (42,F) | p-use | 37,38,39,40,41,42,38: **infeasible** |
| 23 | best | 37 | (53,T) | p-use | 37,38,49,50,53,54: **infeasible** |
| 24 | best | 37 | (53,F) | p-use | 37,38,49,50,51,53,56 (att=[], thr=0) |
| 25 | best | 43 | (42,T) | p-use | 43,38,39,40,41,42,43 (att=[1,1]) |
| 26 | best | 43 | (42,F) | p-use | 43,38,39,46,38,39,40,41,42,38 (att=[1,0,1]) |
| 27 | best | 43 | (53,T) | p-use | 43,38,49,50,51,53,54 (att=[1,1,1,1,1], thr=0) |
| 28 | best | 43 | (53,F) | p-use | 43,38,49,50,51,53,56 (att=[1], thr=0) |
| 29 | a | 38 | (39,T) | p-use | 38,39,40 (att=[1]) |
| 30 | a | 38 | (39,F) | p-use | 38,39,46 (att=[0]) |
| 31 | score | 49 | 54 | c-use | 49,50,53,54 (att=[1,1,1,1,1], thr=6) |
| 32 | score | 49 | 56 | c-use | 49,50,53,56 (att=[], thr=1) |
| 33 | score | 51 | 54 | c-use | 51,53,54 (att=[1,1,1,1,1], thr=0) |
| 34 | score | 51 | 56 | c-use | 51,53,56 (att=[], thr=0) |
| 35 | score | 54 | 56 | c-use | 54,56 (att=[1,1,1,1,1], thr=0) |

On the line-32 short-circuit: when `attendance == null`, `threshold < 0` is never evaluated. So for pair 5 the test case must have a non-null array and a negative threshold.

## (1) Def-use combinations that are not DU pairs (every path is killed)

These six combinations are not DU pairs, because every path from the def to the use goes through line 41, which redefines `streak`:

- `streak` 36 → (42,T), (42,F), 43
- `streak` 46 → (42,T), (42,F), 43

No other variable has a def-use combination that is completely killed.

## (2) Infeasible DU pairs

- **#22, `best` 37 → (42,F):** on any path that has not been through 43, `best` is still 0 and `streak` is at least 1 after line 41, so `streak > best` is always true and the false edge cannot be taken.
- **#23, `best` 37 → (53,T):** a path that never goes through 43 reaches line 53 with `best` = 0, so `best >= 5` is always false.

## Totals

There are 35 DU pairs: 33 feasible and 2 infeasible (#22, #23). There are also 6 killed combinations that are not counted as DU pairs.

By variable: attendance 4, threshold 4, present 6, streak 6, best 8, a 2, score 5.

These totals depend on the modelling choices above. If you split line 32's short-circuit into two separate decisions, or also count the for-each step as a c-use of `attendance`, the counts change.
