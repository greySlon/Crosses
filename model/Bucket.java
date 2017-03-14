package model;

import java.util.*;

import static model.DrawItem.*;
import static model.GameResult.WIN;

/**
 * Created by Sergii on 27.02.2017.
 */
public class Bucket {
    public static int count = 0;
    private Bucket parent;
    private DrawItem[] arr;
    private List<Bucket> list = new ArrayList<>();
    private List<Bucket> listToDelete = new ArrayList<>();

    public final int POS;
    public final DrawItem DRAW_ITEM;
    public final GameResult GAME_RESULT;


    public Bucket(Bucket parent, int pos, DrawItem[] arr, DrawItem drawItem) throws LostException {
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
                goNext();
        }
    }

    public int getStep() {
        if (list.size() == 0) {
            return -1;
        } else {
            for (int i = 0; i < list.size(); i++) {
                Bucket bucket = list.get(i);
                if (bucket.GAME_RESULT == WIN) {
                    return bucket.POS;
                }
            }
            return list.get(0).POS;
        }
    }

    private Bucket getBacket(int pos) {
        for (Bucket backet : list) {
            if (backet.POS == pos) {
                return backet;
            }
        }
        return null;
    }

    public void goNext() {
        try {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == EMPTY) {
                    DrawItem tmpDrawItem = (this.DRAW_ITEM == ZERO) ? CROSS : ZERO;
                    list.add(new Bucket(this, i, arr, tmpDrawItem));
                }

            }
        } catch (LostException e) {
            this.parent.addToDelete(this);
        }
        removeBad();
    }

    private void removeBad() {
        for (Bucket child : listToDelete) {
            this.list.remove(child);
            count--;
        }
    }

    public void addToDelete(Bucket child) {
        listToDelete.add(child);
    }

    public static void main(String[] a) throws LostException {
        DrawItem[] arr = new DrawItem[9];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = EMPTY;
        }
//        Bucket root = new Bucket(arr);
        Bucket root = new Bucket(null, 4, arr, CROSS);
        System.out.println(Bucket.count);
    }
}