# ChineseChessAI
This application is built based on Minimax algorithm.
Demo: https://chinese-chess-ai.herokuapp.com/
## Mini max pseudo-code 
```function minimax(node, depth, maximizingPlayer) is
    if depth = 0 or node is a terminal node then
        return the heuristic value of node
    if maximizingPlayer then
        value := −∞
        for each child of node do
            value := max(value, minimax(child, depth − 1, FALSE))
        return value
    else (* minimizing player *)
        value := +∞
        for each child of node do
            value := min(value, minimax(child, depth − 1, TRUE))
        return value
```
## Document (Vietnamese)
https://utcedu-my.sharepoint.com/:w:/g/personal/nam171200791_st_utc_edu_vn/ETatgjK4k1lBpQhwQm8m5icBrT826fuo8QW-IdAjiz_TEw?e=iAYe25
