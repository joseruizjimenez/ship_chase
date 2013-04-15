% RULES
wall(X, Y) :- X < 10; X > 670; Y < 10; Y > 470.
danger(A, B, D) :- A = B, D < 300.
danger(A, B, D) :- oposite(A, B), D < 300.
hit(D) :- D < 80.
shootLarboard(A, B, D) :- heeled(A, B), D < 400.
shootStarboard(A, B, D) :- heeled(A, B), D < 400.


% Solutions
heeled(A, B) :- A > 10, (A - 8) =:= B.
heeled(A, B) :- A > 10, (A - 10) =:= B.
heeled(A, B) :- (A + 1) =:= B; (A - 1) =:= B.
heeled(A, B) :- (A + 11) =:= B; (A + 9) =:= B.


% FACTS
oposite(1, 10).
oposite(2, 11).
oposite(3, 12).
oposite(4, 13).
oposite(5, 14).
oposite(6, 15).
oposite(7, 16).
oposite(8, 17).
oposite(9, 18).
oposite(10, 1).
oposite(11, 2).
oposite(12, 3).
oposite(13, 4).
oposite(14, 5).
oposite(15, 6).
oposite(16, 7).
oposite(17, 8).
oposite(18, 9).
