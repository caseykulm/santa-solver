# Secret Santa Solver

Included is a solver that uses [ChocoSolver](https://choco-solver.org/) Contraint Satisfaction Solver library to generate 
all possible solutions. 

To execute this run the tests with test/real data to generate all solutions, or a random solution.

Example Output:

```
Total solution count: 80

Solution #0
=================
Gifter Beavis is assigned to Giftee Cat
Gifter Butthead is assigned to Giftee Dog
Gifter Cat is assigned to Giftee Jerry
Gifter Dog is assigned to Giftee Tom
Gifter Jerry is assigned to Giftee Butthead
Gifter Tom is assigned to Giftee Beavis

...


Solution #79
=================
Gifter Beavis is assigned to Giftee Tom
Gifter Butthead is assigned to Giftee Dog
Gifter Cat is assigned to Giftee Jerry
Gifter Dog is assigned to Giftee Butthead
Gifter Jerry is assigned to Giftee Beavis
Gifter Tom is assigned to Giftee Cat


Randomly chosen solution is #21
=================
Gifter Beavis is assigned to Giftee Dog
Gifter Butthead is assigned to Giftee Cat
Gifter Cat is assigned to Giftee Jerry
Gifter Dog is assigned to Giftee Tom
Gifter Jerry is assigned to Giftee Butthead
Gifter Tom is assigned to Giftee Beavis
```