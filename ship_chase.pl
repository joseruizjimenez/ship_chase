% RULES
wall(X, Y) :- X < 20; X > 750; Y < 20; Y > 550.
nearWall(X, Y) :- X < 150; X > 620; Y < 150; Y > 420.
danger(A, B, D) :- A = B, D < 300.
danger(A, B, D) :- oposite(A, B), D < 300.
hit(D) :- D < 80.
shootLarboard(A, B, D) :- heeled(A, B), D < 400.
shootStarboard(A, B, D) :- heeled(A, B), D < 400.
onShootingPath(XA, YA, XB, YB, XC, YC, XP, YP) :- triangleOrientation(XA, YA, XB, YB, XC, YC, OT),
                                                  OT > 0,
                                                  triangleOrientation(XA, YA, XB, YB, XP, YP, OA),
                                                  OA > 0,
                                                  triangleOrientation(XA, YA, XP, YP, XC, YC, OB),
                                                  OB > 0,
                                                  triangleOrientation(XP, YP, XB, YB, XC, YC, OC),
                                                  OC > 0.
onShootingPath(XA, YA, XB, YB, XC, YC, XP, YP) :- triangleOrientation(XA, YA, XB, YB, XC, YC, OT),
                                                  OT < 0,
                                                  triangleOrientation(XA, YA, XB, YB, XP, YP, OA),
                                                  OA < 0,
                                                  triangleOrientation(XA, YA, XP, YP, XC, YC, OB),
                                                  OB < 0,
                                                  triangleOrientation(XP, YP, XB, YB, XC, YC, OC),
                                                  OC < 0.


% Solutions
triangleOrientation(XA, YA, XB, YB, XC, YC, R) :- R is ((XA-XC)*(YB-YC)-(YA-YC)*(XB-XC)).
heeled(A, B) :- A > 10, (A - 8) =:= B.
heeled(A, B) :- A > 10, (A - 10) =:= B.
heeled(A, B) :- (A + 1) =:= B; (A - 1) =:= B.
heeled(A, B) :- (A + 11) =:= B; (A + 9) =:= B.
g(XA, YA, XB, YB, XC, YC, XP, YP, P) :- onShootingPath(XA, YA, XB, YB, XC, YC, XP, YP), P is 1.
g(XA, YA, XB, YB, XC, YC, XP, YP, P) :- P is 5.
h('1', '2', P) :- P is 1.
h('1', '3', P) :- P is 2.
h('1', '4', P) :- P is 3.
h('1', '5', P) :- P is 4.
h('1', '6', P) :- P is 5.
h('1', '7', P) :- P is 6.
h('1', '8', P) :- P is 7.
h('1', '9', P) :- P is 8.
h('1', '10', P) :- P is 9.
h('1', '11', P) :- P is 10.
h('1', '12', P) :- P is 11.
h('1', '13', P) :- P is 12.
h('1', '14', P) :- P is 13.
h('1', '15', P) :- P is 14.
h('1', '16', P) :- P is 15.
h('1', '17', P) :- P is 16.
h('1', '18', P) :- P is 17.
h('2', '3', P) :- P is 1.
h('2', '4', P) :- P is 2.
h('2', '5', P) :- P is 3.
h('2', '6', P) :- P is 4.
h('2', '7', P) :- P is 5.
h('2', '8', P) :- P is 6.
h('2', '9', P) :- P is 7.
h('2', '10', P) :- P is 8.
h('2', '11', P) :- P is 9.
h('2', '12', P) :- P is 10.
h('2', '13', P) :- P is 11.
h('2', '14', P) :- P is 12.
h('2', '15', P) :- P is 13.
h('2', '16', P) :- P is 14.
h('2', '17', P) :- P is 15.
h('2', '18', P) :- P is 16.
h('3', '4', P) :- P is 1.
h('3', '5', P) :- P is 2.
h('3', '6', P) :- P is 3.
h('3', '7', P) :- P is 4.
h('3', '8', P) :- P is 5.
h('3', '9', P) :- P is 6.
h('3', '10', P) :- P is 7.
h('3', '11', P) :- P is 8.
h('3', '12', P) :- P is 9.
h('3', '13', P) :- P is 10.
h('3', '14', P) :- P is 11.
h('3', '15', P) :- P is 12.
h('3', '16', P) :- P is 13.
h('3', '17', P) :- P is 14.
h('3', '18', P) :- P is 15.
h('4', '5', P) :- P is 1.
h('4', '6', P) :- P is 2.
h('4', '7', P) :- P is 3.
h('4', '8', P) :- P is 4.
h('4', '9', P) :- P is 5.
h('4', '10', P) :- P is 6.
h('4', '11', P) :- P is 7.
h('4', '12', P) :- P is 8.
h('4', '13', P) :- P is 9.
h('4', '14', P) :- P is 10.
h('4', '15', P) :- P is 11.
h('4', '16', P) :- P is 12.
h('4', '17', P) :- P is 13.
h('4', '18', P) :- P is 14.
h('5', '6', P) :- P is 1.
h('5', '7', P) :- P is 2.
h('5', '8', P) :- P is 3.
h('5', '9', P) :- P is 4.
h('5', '10', P) :- P is 5.
h('5', '11', P) :- P is 6.
h('5', '12', P) :- P is 7.
h('5', '13', P) :- P is 8.
h('5', '14', P) :- P is 9.
h('5', '15', P) :- P is 10.
h('5', '16', P) :- P is 11.
h('5', '17', P) :- P is 12.
h('5', '18', P) :- P is 13.
h('6', '7', P) :- P is 1.
h('6', '8', P) :- P is 2.
h('6', '9', P) :- P is 3.
h('6', '10', P) :- P is 4.
h('6', '11', P) :- P is 5.
h('6', '12', P) :- P is 6.
h('6', '13', P) :- P is 7.
h('6', '14', P) :- P is 8.
h('6', '15', P) :- P is 9.
h('6', '16', P) :- P is 10.
h('6', '17', P) :- P is 11.
h('6', '18', P) :- P is 12.
h('7', '8', P) :- P is 1.
h('7', '9', P) :- P is 2.
h('7', '10', P) :- P is 3.
h('7', '11', P) :- P is 4.
h('7', '12', P) :- P is 5.
h('7', '13', P) :- P is 6.
h('7', '14', P) :- P is 7.
h('7', '15', P) :- P is 8.
h('7', '16', P) :- P is 9.
h('7', '17', P) :- P is 10.
h('7', '18', P) :- P is 11.
h('8', '9', P) :- P is 1.
h('8', '10', P) :- P is 2.
h('8', '11', P) :- P is 3.
h('8', '12', P) :- P is 4.
h('8', '13', P) :- P is 5.
h('8', '14', P) :- P is 6.
h('8', '15', P) :- P is 7.
h('8', '16', P) :- P is 8.
h('8', '17', P) :- P is 9.
h('8', '18', P) :- P is 10.
h('9', '10', P) :- P is 1.
h('9', '11', P) :- P is 2.
h('9', '12', P) :- P is 3.
h('9', '13', P) :- P is 4.
h('9', '14', P) :- P is 5.
h('9', '15', P) :- P is 6.
h('9', '16', P) :- P is 7.
h('9', '17', P) :- P is 8.
h('9', '18', P) :- P is 9.
h('10', '11', P) :- P is 1.
h('10', '12', P) :- P is 2.
h('10', '13', P) :- P is 3.
h('10', '14', P) :- P is 4.
h('10', '15', P) :- P is 5.
h('10', '16', P) :- P is 6.
h('10', '17', P) :- P is 7.
h('10', '18', P) :- P is 8.
h('11', '12', P) :- P is 1.
h('11', '13', P) :- P is 2.
h('11', '14', P) :- P is 3.
h('11', '15', P) :- P is 4.
h('11', '16', P) :- P is 5.
h('11', '17', P) :- P is 6.
h('11', '18', P) :- P is 7.
h('12', '13', P) :- P is 1.
h('12', '14', P) :- P is 2.
h('12', '15', P) :- P is 3.
h('12', '16', P) :- P is 4.
h('12', '17', P) :- P is 5.
h('12', '18', P) :- P is 6.
h('13', '14', P) :- P is 1.
h('13', '15', P) :- P is 2.
h('13', '16', P) :- P is 3.
h('13', '17', P) :- P is 4.
h('13', '18', P) :- P is 5.
h('14', '15', P) :- P is 1.
h('14', '16', P) :- P is 2.
h('14', '17', P) :- P is 3.
h('14', '18', P) :- P is 4.
h('15', '16', P) :- P is 1.
h('15', '17', P) :- P is 2.
h('15', '18', P) :- P is 3.
h('16', '17', P) :- P is 1.
h('16', '18', P) :- P is 2.
h('17', '18', P) :- P is 1.


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