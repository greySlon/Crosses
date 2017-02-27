package model;

import java.util.*;

import static model.DrawItem.*;
import static model.GameResult.*;

/**
 * Created by Sergii on 27.02.2017.
 */
public class Backet {
    public static int count = 0;
    private Backet parent;
    private DrawItem[] arr;
    private List<Backet> list = new ArrayList<>();
    private List<Backet> listToDelete = new ArrayList<>();

    public final int POS;
    public final DrawItem DRAW_ITEM;
    public final GameResult GAME_RESULT;


    public Backet(Backet parent, int pos, DrawItem[] arr, DrawItem drawItem) throws LostException {
        count++;
        this.parent = parent;
        this.POS = pos;
        this.arr = Arrays.copyOf(arr, arr.length);
        this.DRAW_ITEM = drawItem;
        this.arr[pos] = drawItem;
        this.GAME_RESULT = Checker.checkResult(this.arr, this.DRAW_ITEM);

        switch (GAME_RESULT) {
            case LOSE:
                throw new LostException();
            case INDEFINITE:
                list = new LinkedList<>();
                goFurther();
        }
    }

    public int getStep(List<Integer> stepSequense) {
        if (stepSequense == null || stepSequense.size() == 0) {
            return (list.size() == 0) ? -1 : list.get(0).POS;
        } else {
            Backet backet = getBacket(stepSequense.get(0));
            if (backet == null) {
                return -1;
            }
            int toIndex = stepSequense.size();
            if (toIndex == 1) {
                return backet.getStep(null);
            } else {
                return backet.getStep(stepSequense.subList(1, toIndex));
            }
        }
    }

    private Backet getBacket(int pos) {
        for (Backet backet : list) {
            if (backet.POS == pos) {
                return backet;
            }
        }
        return null;
    }

    public void goFurther() {
        try {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == EMPTY) {
                    DrawItem tmpDrawItem = (this.DRAW_ITEM == ZERO) ? CROSS : ZERO;
                    list.add(new Backet(this, i, arr, tmpDrawItem));
                }

            }
        } catch (LostException e) {
            this.parent.addToDelete(this);
        }
        removeBad();
    }

    private void removeBad() {
        for (Backet child : listToDelete) {
            this.list.remove(child);
            count--;
        }
    }

    public void addToDelete(Backet child) {
        listToDelete.add(child);
    }

    /*private GameResult checkResult(DrawItem[] arr) {
        if ((arr[0] == arr[1] && arr[1] == arr[2] && arr[0] != EMPTY) ||
                (arr[3] == arr[4] && arr[4] == arr[5] && arr[3] != EMPTY) ||
                (arr[6] == arr[7] && arr[7] == arr[8] && arr[6] != EMPTY) ||
                (arr[0] == arr[3] && arr[3] == arr[6] && arr[0] != EMPTY) ||
                (arr[1] == arr[4] && arr[4] == arr[7] && arr[1] != EMPTY) ||
                (arr[2] == arr[5] && arr[5] == arr[8] && arr[2] != EMPTY) ||
                (arr[0] == arr[4] && arr[4] == arr[8] && arr[0] != EMPTY) ||
                (arr[2] == arr[4] && arr[4] == arr[6] && arr[2] != EMPTY)) {
            if (DRAW_ITEM == ZERO) {
                return WIN;
            } else {
                return LOSE;
            }
        } else {
            return INDEFINITE;
        }
    }*/

    public static void main(String[] a) throws LostException {
        DrawItem[] arr = new DrawItem[9];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = EMPTY;
        }
        Backet root = new Backet(null, 4, arr, CROSS);
        System.out.println(Backet.count);
    }
}