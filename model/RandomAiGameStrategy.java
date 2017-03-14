package model;

import java.util.Arrays;

import static model.DrawItem.CROSS;
import static model.DrawItem.EMPTY;
import static model.DrawItem.ZERO;

/**
 * Created by Sergii on 14.03.2017.
 */
public class RandomAiGameStrategy implements AiGameStrategy {
    private Bucket bucketRoot;
    private DrawItem[] field = new DrawItem[9];

    @Override
    public int aiMakeTurn(int[] field) {
        Arrays.fill(this.field, EMPTY);
        mnemonicTransform(field);
        int zeroPos = -1;
        findNonlostCombinations();
        zeroPos = bucketRoot.getStep();
        return anyIfNoStep(zeroPos);
    }

    private void mnemonicTransform(int[] field) {
        for (int i = 0; i < field.length; i++) {
            int tmp = field[i];
            switch (tmp) {
                case 1:
                    this.field[i] = CROSS;
                    break;
                case -1:
                    this.field[i] = ZERO;
                    break;
            }
        }

    }

    private void findNonlostCombinations() {
        try {
            int p = -1;
            for (int i = 0; i < field.length; i++) {
                if (field[i] == CROSS) {
                    p = i;
                    break;
                }
            }
            Bucket.count = 0;
            bucketRoot = new Bucket(null, p, field, CROSS);
            System.out.println(bucketRoot.count);
        } catch (LostException e) {
        }
    }

    private int anyIfNoStep(int pos) {
        if (pos == -1) {
            for (int i = 0; i < field.length; i++) {
                if (field[i] == EMPTY) {
                    return i;
                }
            }
            return -1;
        } else {
            return pos;
        }
    }
}
