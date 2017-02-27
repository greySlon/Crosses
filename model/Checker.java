package model;

import static model.DrawItem.EMPTY;
import static model.DrawItem.ZERO;
import static model.GameResult.INDEFINITE;
import static model.GameResult.LOSE;
import static model.GameResult.WIN;

/**
 * Created by Sergii on 27.02.2017.
 */
public class Checker {
    public static GameResult checkResult(DrawItem[] arr, DrawItem currentDrawItem) {
        if ((arr[0] == arr[1] && arr[1] == arr[2] && arr[0] != EMPTY) ||
                (arr[3] == arr[4] && arr[4] == arr[5] && arr[3] != EMPTY) ||
                (arr[6] == arr[7] && arr[7] == arr[8] && arr[6] != EMPTY) ||
                (arr[0] == arr[3] && arr[3] == arr[6] && arr[0] != EMPTY) ||
                (arr[1] == arr[4] && arr[4] == arr[7] && arr[1] != EMPTY) ||
                (arr[2] == arr[5] && arr[5] == arr[8] && arr[2] != EMPTY) ||
                (arr[0] == arr[4] && arr[4] == arr[8] && arr[0] != EMPTY) ||
                (arr[2] == arr[4] && arr[4] == arr[6] && arr[2] != EMPTY)) {
            if (currentDrawItem == ZERO) {
                return WIN;
            } else {
                return LOSE;
            }
        } else {
            return INDEFINITE;
        }
    }
}
