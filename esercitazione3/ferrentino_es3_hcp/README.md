# Differenze rispetto alle specifiche

### Grammatica di riferimento
```
S -> Program EOF 
Program -> Program ; Stmt | Stmt
Stmt -> IF Expr THEN Stmt END IF
    | IF Expr THEN Stmt ELSE Stmt END IF
    | ID ASSIGN Expr
    | WHILE Epr LOOP Stmt END LOOP
Expr -> Expr RELOP Expr
Expr -> ID | NUMBER
```

### Grammatica modificata
```
S -> Program EOF
Program -> Stmt MoreStmt
MoreStmt -> ; Stmt MoreStmt | ε
Stmt -> IF Expr THEN Stmt StmtTail
     | ID ASSIGN Expr
     | WHILE Expr LOOP Stmt END LOOP
StmtTail -> END IF
     | ELSE Stmt END IF
Expr -> ID MoreExpr | NUMBER MoreExpr
MoreExpr -> RELOP IdOrNumber MoreExpr | ε
IdOrNumber -> ID | NUMBER
```